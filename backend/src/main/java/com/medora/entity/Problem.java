package com.medora.entity;

import com.medora.entity.enums.ProblemStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="problem")
public class Problem{
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "problem_seq")
    @SequenceGenerator(sequenceName = "problem_seq", allocationSize = 1, name = "problem_seq")
	@Column(name = "problemid")
	private Long problemid;
	private String problemName;
	private String problemDetail;

	@Enumerated(EnumType.STRING)
	private ProblemStatus problemStatus;
	private int status;
	private Long patientid;
	private Long admissionid;

    @Temporal(TemporalType.TIMESTAMP)
    Date creationDate;
	
	@NotNull
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;
	
	
	@OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Prescription> prescriptions;
}
