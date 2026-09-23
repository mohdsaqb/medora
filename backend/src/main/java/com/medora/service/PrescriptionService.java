package com.medora.service;

import com.medora.dto.PrescriptionDto;
import com.medora.entity.Problem;
import com.medora.entity.Prescription;
import com.medora.exception.NotFoundException;
import com.medora.exception.PatientNotFoundException;
import com.medora.repository.ProblemRepository;
import com.medora.repository.PrescriptionRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {
	private final PrescriptionRepository prescriptionRepository;
	private final ModelMapper modelMapper;
	private final Logger logger;
	private final ProblemRepository problemRepository;

	public PrescriptionService(PrescriptionRepository prescriptionRepository, ModelMapper modelMapper, Logger logger,
			ProblemRepository problemRepository) {
		this.prescriptionRepository = prescriptionRepository;
		this.modelMapper = modelMapper;
		this.logger = logger;
		this.problemRepository = problemRepository;
	}

	public List<PrescriptionDto> getAll() throws Exception {
		try {
			List<Prescription> list = prescriptionRepository.findAllByStatusEquelsOne();
			if (list.size() > 0) {
				PrescriptionDto[] dtos = modelMapper.map(list, PrescriptionDto[].class);
				return Arrays.asList(dtos);
			} else {
				logger.error("there is no any prescription");
				throw new PatientNotFoundException("there is no any prescription");
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public void findByprescriptionId() {

	}

	public List<PrescriptionDto> findAllByProblemId(Long problemid) throws Exception {
		try {
			List<Prescription> list = prescriptionRepository.findAllByProblemId(problemid);
			if (list.size() > 0) {
				PrescriptionDto[] dtos = modelMapper.map(list, PrescriptionDto[].class);
				return Arrays.asList(dtos);
			} else {
				logger.info("This problem has no any prescription");
				throw new PatientNotFoundException("This problem has no any prescription");
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public PrescriptionDto save(PrescriptionDto dto) throws NotFoundException {
		Optional<Problem> opt = problemRepository.findById(dto.getProblemid());
		if (opt.isPresent()) {
			dto.setStatus(1);
			Prescription prescription = modelMapper.map(dto, Prescription.class);
			prescription.setProblem(opt.get());
			prescription.setProblemid(opt.get().getProblemid());
			prescription.setPatientid(opt.get().getPatientid());
			prescription = prescriptionRepository.save(prescription);
			if (prescription.getPrescriptionid() > -1) {
				dto.setPrescriptionid(prescription.getPrescriptionid());
				logger.info("Perfect.. Saving Prescription for related problem is ok");
				return dto;
			} else {
				logger.error("A problem occurred during saving prescription");
				throw new PatientNotFoundException("A problem occurred during saving prescription");
			}
		} else {
			logger.error("There is no such problem with problem id : " + dto.getProblemid());
			throw new NotFoundException("There is no such problem with problem id : " + dto.getProblemid());
		}

	}

	public boolean delete(Long prescriptionid) throws NotFoundException {
		Optional<Prescription> optional = prescriptionRepository.findById(prescriptionid);
		if (!optional.isPresent()) {
			logger.error("Prescription does not exist wtih prescriptionid : " + prescriptionid);
			throw new NotFoundException("Prescription does not exist with prescriptionid : " + prescriptionid);
		}
		optional.get().setStatus(0);
		prescriptionRepository.save(optional.get());
		logger.info("Prescription was deleted wtih prescriptionid : " + prescriptionid);
		return true;
	}
}
