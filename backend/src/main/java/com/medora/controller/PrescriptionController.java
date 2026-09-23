package com.medora.controller;

import com.medora.dto.PrescriptionDto;
import com.medora.exception.NotFoundException;
import com.medora.service.ProblemService;
import com.medora.service.PrescriptionService;
import com.medora.util.ApiPaths;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.PrescriptionCtrl.CTRL)
public class PrescriptionController {

	@Autowired
	ProblemService problemService;
	
	@Autowired
	PrescriptionService prescriptionService;

	@GetMapping("/find-all-by-problemid/{problemid}")
	public ResponseEntity<List<PrescriptionDto>> getPrescription(@PathVariable(name = "problemid", required = true) Long problemid) throws Exception {
		return ResponseEntity.ok(prescriptionService.findAllByProblemId(problemid));
	}

	@PostMapping
	public ResponseEntity<PrescriptionDto> savePrescription(@Valid @RequestBody PrescriptionDto dto) throws NotFoundException {
		return ResponseEntity.ok(prescriptionService.save(dto));
	}

//	@PutMapping("/{prescriptionid}")
//	public ResponseEntity<Boolean> updatePrescription(@PathVariable(name = "prescriptionid", required = true) Long problemid,
//			@Valid @RequestBody ProblemDtoForPatientSingleDto dto) throws Exception {
//		return ResponseEntity.ok(problemService.update(problemid, dto));
//	}

	@DeleteMapping("/{prescriptionid}")
	public ResponseEntity<Boolean> deletePrescription(@PathVariable(name = "prescriptionid", required = true) Long prescriptionid) throws Exception {
		return ResponseEntity.ok(prescriptionService.delete(prescriptionid));
	}

}
