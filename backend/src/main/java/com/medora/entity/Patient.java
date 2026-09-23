package com.medora.entity;

import com.medora.entity.enums.City;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "patient")
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_seq")
	@SequenceGenerator(sequenceName = "patient_seq", allocationSize = 1, name = "patient_seq")
	@Column(name = "patientid")
	private Long patientid;
	private String name;
	private String lastname;
	private String phoneNo;
	private Date bornDate;
	private String gender;
	
	@Enumerated(EnumType.STRING)
	private City city;

	@Column(name = "email", unique = true)
	private String email;
	
	private int status;

	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Problem> problems;

	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Admission> admissions;


	public Patient(String name, String lastname,Date bornDate, String gender, String age, City city, String email, int status) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.bornDate = bornDate;
		this.gender = gender;
		this.city = city;
		this.email = email;
		this.status = status;
	}
	public Patient(String name, String lastname, String gender,  City city, String email, int status) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.gender = gender;
		this.city = city;
		this.email = email;
		this.status = status;
	}

}
