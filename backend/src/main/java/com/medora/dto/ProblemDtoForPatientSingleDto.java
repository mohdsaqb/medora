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
public class ProblemDtoForPatientSingleDto  implements Serializable {
	private Long problemid;
	private String problemName;
	private String problemDetail;
	private ProblemStatus problemStatus;
	private Long pId;
	// Boxed rather than primitive so a partial update may omit it. As an int,
	// Jackson rejected any payload without the field and the whole request
	// failed with 400 before reaching the service.
	private Integer status;
	private Date creationDate;

}
