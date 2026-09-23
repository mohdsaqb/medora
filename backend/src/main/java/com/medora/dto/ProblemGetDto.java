package com.medora.dto;

import java.io.Serializable;
import java.util.Date;

import com.medora.entity.Patient;
import com.medora.entity.enums.ProblemStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProblemGetDto implements Serializable {
	private Long problemid;
	private String problemName;
	private String problemDetail;
	private ProblemStatus problemStatus;
	private Long pId;
	private Date creationDate;
	private int status;
	private PatientDtoForProblemGetDto patient;
}
