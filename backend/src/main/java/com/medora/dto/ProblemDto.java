package com.medora.dto;

import java.io.Serializable;
import java.util.Date;

import com.medora.entity.Patient;
import com.medora.entity.Problem;
import com.medora.entity.enums.ProblemStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProblemDto implements Serializable {

	private String problemName;
	private String problemDetail;
	private ProblemStatus problemStatus;
	private Long pId;
	private int status;
	private Date creationDate;
}
