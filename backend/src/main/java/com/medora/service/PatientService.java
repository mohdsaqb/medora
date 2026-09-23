package com.medora.service;

import com.medora.dto.PatientDto;
import com.medora.dto.PatientSingleDto;
import com.medora.entity.Patient;
import com.medora.exception.PatientNotFoundException;
import com.medora.exception.ValidationException;
import com.medora.repository.PatientRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private final PatientRepository patientRepository;
	private final ModelMapper modelMapper;
	private final Logger logger;

	public PatientService(PatientRepository patientRepository, ModelMapper modelMapper, Logger logger) {
		this.patientRepository = patientRepository;
		this.modelMapper = modelMapper;
		this.logger = logger;
	}

	public List<PatientDto> findAll() throws Exception {
		try { 
			// List<Patient> patients = patientRepository.findAllByOrderByPatientidAsc();
			List<Patient> patients = patientRepository.findAllByStatusEquelsOne();
			if (patients.size() < 1) {
				logger.error("There is never patients ");
				throw new PatientNotFoundException("There is never patient ");
			}
			PatientDto[] dtos = modelMapper.map(patients, PatientDto[].class);
			List<PatientDto> patientDtos = Arrays.asList(dtos);
			patientDtos.forEach(patient->{
				patient.getProblems().forEach(problem->{
					problem.setPId(patient.getPatientid());
				});
			});
			return Arrays.asList(dtos);
		} catch (PatientNotFoundException e) {
			// Rethrow as-is. Wrapping it in a plain Exception hid the 404 and
			// produced a 500 instead.
			throw e;
		}
	}

	public List<PatientDto> findAllDeletedPatients() { 
		List<Patient> patients = patientRepository.findAllByStatusEquelsZero(); 
		if (patients.size() > 0) { 
			PatientDto[] authorDtos = modelMapper.map(patients, PatientDto[].class);
			return Arrays.asList(authorDtos);
		} else {
			logger.error("There is no deleted patient ");
			throw new PatientNotFoundException("There is no deleted patient "); 
		}
	}

	public Patient save(Patient patient) {
		requireCreateFields(patient);
		patient.setStatus(1);
		blankToNull(patient);
		patient = patientRepository.save(patient);
		if (patient.getPatientid() > -1)
			return patient;
		else{
			logger.error("A problem occurred during saving patient" );
			throw new PatientNotFoundException("A problem occurred during saving patient" );
		}
	}

	public Boolean delete(@Valid Long patientid) throws Exception {
		Optional<Patient> optPatient = patientRepository.findById(patientid);
		if (optPatient.isPresent()) { 
			optPatient.get().setStatus(0);
			optPatient.get().getProblems().forEach(p -> {
				p.setStatus(0);
			});
			patientRepository.save(optPatient.get());
			// patientRepository.delete(optpatient.get());
			return true;
		} else {
			logger.error("--Patient does not exist with this id " + patientid);
			throw new PatientNotFoundException("Patient does not exist with this id " + patientid); 
		}
	}

	public PatientSingleDto findByPatientId(Long patientid) throws Exception {
		Optional<Patient> optPatient = patientRepository.findById(patientid);
		if (optPatient.isPresent()) { 
			optPatient.get().getProblems().removeIf(problem -> problem.getStatus() == 0);
			PatientSingleDto dto = modelMapper.map(optPatient.get(), PatientSingleDto.class); 
			return dto;
		} else { 
			logger.error("--Patient does not exist with this id " + patientid);
			throw new PatientNotFoundException("Patient does not exist with this id " + patientid);
		}
	}

	public Patient findByEmail(String email) throws Exception {
		Optional<Patient> patient = patientRepository.findByEmail(email);
		if (patient.isPresent()) 
			return patient.get();
		else { 
			logger.error("--Patient does not exist with this email " + email);
			throw new PatientNotFoundException("Patient does not exist with this email " + email);
		}
	}

	public Boolean update(Long patientid, @Valid Patient patient) throws Exception {
		Optional<Patient> p = patientRepository.findById(patientid);
		if (!p.isPresent()) {
			logger.error("--Patient does not exist with this id " + patientid);
			throw new PatientNotFoundException("Patient does not exist with this id " + patientid);
		}

		// Merge onto the stored record rather than saving the request body
		// verbatim. Saving it whole meant any field the caller omitted was
		// overwritten with null, and because status is a primitive int it
		// defaulted to 0, the soft delete marker, so an update that left it
		// out made the patient disappear from every active listing.
		blankToNull(patient);

		Patient existing = p.get();
		if (patient.getName() != null) {
			existing.setName(patient.getName());
		}
		if (patient.getLastname() != null) {
			existing.setLastname(patient.getLastname());
		}
		if (patient.getPhoneNo() != null) {
			existing.setPhoneNo(patient.getPhoneNo());
		}
		if (patient.getBornDate() != null) {
			existing.setBornDate(patient.getBornDate());
		}
		if (patient.getGender() != null) {
			existing.setGender(patient.getGender());
		}
		if (patient.getCity() != null) {
			existing.setCity(patient.getCity());
		}
		if (patient.getEmail() != null) {
			existing.setEmail(patient.getEmail());
		}

		// status is never taken from the payload. Removal goes through the
		// delete endpoint, which sets it deliberately.
		patientRepository.save(existing);
		return true;
	}

	public List<Patient> findByName(String name) throws Exception { 
		List<Patient> patients = patientRepository.findByName(name);
		if (patients.size() > 0) { 
			return patients;
		} else { 
			logger.error("--Patient does not exist with this name " + name);
			throw new PatientNotFoundException("Patient does not exist with this name " + name);
		}
	}


	/**
	 * Turns blank optional fields into null.
	 *
	 * email carries a unique constraint, and an empty form field arrives as ""
	 * rather than null. The first patient saved with a blank email took the ""
	 * value, and every later one collided with it. Postgres allows any number
	 * of nulls in a unique index, so storing null is what "not supplied"
	 * should mean.
	 */
	private void blankToNull(Patient patient) {
		if (patient.getEmail() != null && patient.getEmail().isBlank()) {
			patient.setEmail(null);
		}
		if (patient.getPhoneNo() != null && patient.getPhoneNo().isBlank()) {
			patient.setPhoneNo(null);
		}
		if (patient.getGender() != null && patient.getGender().isBlank()) {
			patient.setGender(null);
		}
	}


	/**
	 * Enforces the fields the Add Patient form marks with an asterisk.
	 *
	 * Applied on create only. Update merges whatever it is given onto the
	 * stored record, so a partial payload there is valid by design.
	 */
	private void requireCreateFields(Patient patient) {
		List<String> missing = new ArrayList<>();
		if (isBlank(patient.getName())) {
			missing.add("name");
		}
		if (isBlank(patient.getLastname())) {
			missing.add("last name");
		}
		if (isBlank(patient.getPhoneNo())) {
			missing.add("phone number");
		}
		if (!missing.isEmpty()) {
			throw new ValidationException("Required: " + String.join(", ", missing));
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

}
