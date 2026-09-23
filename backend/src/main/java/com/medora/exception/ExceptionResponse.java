package com.medora.exception;

import java.util.Date;

import com.medora.entity.Patient;
import com.medora.entity.Problem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExceptionResponse {
	private Date date;
	private String message;
}
