package com.medora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.medora.entity.Patient;
import com.medora.entity.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

	@Query("select p from Prescription p where p.status = 1 order by p.prescriptionid ASC")
	List<Prescription> findAllByStatusEquelsOne();
	
	@Query("select p from Prescription p where p.status = 1 and p.problemid=:problemid order by p.prescriptionid ASC")
	List<Prescription> findAllByProblemId(@Param("problemid") Long problemid);
}
