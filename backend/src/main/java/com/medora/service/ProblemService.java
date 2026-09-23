package com.medora.service;

import com.medora.dto.ProblemDto;
import com.medora.dto.ProblemDtoForPatientSingleDto;
import com.medora.dto.ProblemGetDto;
import com.medora.entity.Patient;
import com.medora.entity.Problem;
import com.medora.exception.NotFoundException;
import com.medora.repository.PatientRepository;
import com.medora.repository.ProblemRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final ProblemRepository problemRepository;
	private final PatientRepository patientRepository;
	private final ModelMapper modelMapper;  
	private final Logger logger;

	public ProblemService(ProblemRepository problemRepository, PatientRepository patientRepository,
			ModelMapper modelMapper, Logger logger) {
		this.patientRepository = patientRepository;
		this.problemRepository = problemRepository;
		this.modelMapper = modelMapper;
		this.logger = logger;
	}

	public ProblemDtoForPatientSingleDto save(ProblemDto dto) throws NotFoundException {
		Optional<Patient> patient = patientRepository.findById(dto.getPId());
		if (!patient.isPresent()) {
			logger.error("Patient does already exist wtih patientid : " + dto.getPId());
			throw new NotFoundException("Patient does already exist with patientid : " + dto.getPId());
		}
		Problem problem = modelMapper.map(dto, Problem.class);
		problem.setPatient(patient.get());
		problem.setPatientid(patient.get().getPatientid());
		problemRepository.save(problem);
		ProblemDtoForPatientSingleDto getDto = modelMapper.map(problem, ProblemDtoForPatientSingleDto.class);
		return getDto;
	}

	public Boolean delete(Long problemid) throws NotFoundException {
		Optional<Problem> optional = problemRepository.findById(problemid);
		if (!optional.isPresent()) {
			logger.error("Problem does not exist wtih problemid : " + problemid);
			throw new NotFoundException("Problem does not exist with problemid : " + problemid);
		}
		optional.get().setStatus(0);
		problemRepository.save(optional.get());
		// problemRepository.delete(optional.get());
		return true;
	}

	public ProblemGetDto findByProblemid(Long problemid) throws NotFoundException {
		Optional<Problem> optional = problemRepository.findById(problemid);
		if (!optional.isPresent()) {
			logger.error("Problem does not exist wtih problemid : " + problemid);
			throw new NotFoundException("Problem does not exist with problemid : " + problemid);
		}
		ProblemGetDto dto = modelMapper.map(optional.get(), ProblemGetDto.class);
		return dto;
	}

	public Boolean update(Long problemid, @Valid ProblemDtoForPatientSingleDto dto) throws NotFoundException {
		Optional<Problem> optional = problemRepository.findById(problemid);
		if (!optional.isPresent()) {
			logger.error("Problem does not exist wtih problemid : " + problemid);
			throw new NotFoundException("Problem does not exist with problemid : " + problemid);
		}

		// Apply only the clinical fields, and only when supplied, so a partial
		// payload cannot blank the rest of the record. An earlier version
		// returned true here without saving anything, silently discarding the
		// caller's changes.
		Problem problem = optional.get();
		if (dto.getProblemName() != null) {
			problem.setProblemName(dto.getProblemName());
		}
		if (dto.getProblemDetail() != null) {
			problem.setProblemDetail(dto.getProblemDetail());
		}
		if (dto.getProblemStatus() != null) {
			problem.setProblemStatus(dto.getProblemStatus());
		}

		// patientid, creationDate and the soft delete flag are deliberately not
		// taken from the payload. Removal goes through the delete endpoint.
		problemRepository.save(problem);
		return true;
	}

	public List<ProblemDtoForPatientSingleDto> findAllByPatientid(Long patientid) {
		List<Problem> list = problemRepository.findByPatientidWithStatusOne(patientid);
		// A patient with no diagnoses yet is normal, not an error.
		if (list.isEmpty()) {
			return List.of();
		}
		return Arrays.asList(modelMapper.map(list, ProblemDtoForPatientSingleDto[].class));
	}

}
