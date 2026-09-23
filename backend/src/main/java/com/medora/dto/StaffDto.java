package com.medora.dto;

import com.medora.entity.enums.City;
import com.medora.entity.enums.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StaffDto implements Serializable {
	
	private Long staffid;
	private String staffname;
	private String stafflastname;
	private String gender;
	private String email;
	private City city;
	private Department department;
    private Date bornDate;
}
