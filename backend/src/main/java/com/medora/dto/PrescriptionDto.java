package com.medora.dto;

import java.util.Date;
import java.util.List;

import com.medora.entity.enums.City;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PrescriptionDto {
	private Long prescriptionid;
	
	private String detail;
	private String barcode;
	private String drug_detail;
	private String usage;
	private String delivery_date;
	private int status;
	private Long patientid;
	private Long problemid;
}
