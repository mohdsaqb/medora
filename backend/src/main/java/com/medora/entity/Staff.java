package com.medora.entity;

import com.medora.entity.enums.City;
import com.medora.entity.enums.Department;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "staff")
public class Staff {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_seq")
	@SequenceGenerator(sequenceName = "staff_seq", allocationSize = 1, name = "staff_seq")
	@Column(name = "staffid")
	private Long staffid;
	private String staffname;
	private String stafflastname;
	private String gender;

	
	@Column(name = "email", unique = true)
	private String email;
	@Enumerated(EnumType.STRING)
	private City city;
	
	@Column(name = "department", length = 100)
	@Enumerated(EnumType.STRING)
	private Department department;
	
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date bornDate;
    
	@OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Admission> admissions;
	
	private int status;
}
