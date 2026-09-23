package com.medora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="prescription")
public class Prescription {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prescription_seq")
    @SequenceGenerator(sequenceName = "prescription_seq", allocationSize = 1, name = "prescription_seq")
	@Column(name = "prescriptionid")
	private Long prescriptionid;
	
	private String detail;
	private String barcode;
	private String drug_detail;
	private String usage;
	private String delivery_date;
	private Long problemid;
	private Long patientid;
	private int status;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;
	
}
