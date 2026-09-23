package com.medora.exception;

import java.util.Date;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class IMExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Both not-found exceptions answer 404 with the message as written.
	 *
	 * An earlier version returned {@code ex.getMessage().split(":")[1]}, which
	 * threw ArrayIndexOutOfBoundsException for every message without a colon.
	 * None of the thrown messages contain one, so this handler failed on each
	 * call and the request fell through to the error dispatch.
	 */
	@ExceptionHandler({PatientNotFoundException.class, NotFoundException.class})
	public final ResponseEntity<ExceptionResponse> handleNotFound(RuntimeException ex, WebRequest request) {
		logger.error("Not found: " + ex.getMessage());
		return new ResponseEntity<>(new ExceptionResponse(new Date(), ex.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ValidationException.class)
	public final ResponseEntity<ExceptionResponse> handleValidation(ValidationException ex, WebRequest request) {
		logger.error("Rejected: " + ex.getMessage());
		return new ResponseEntity<>(new ExceptionResponse(new Date(), ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	/**
	 * A unique constraint rejection is the caller's problem, not a server
	 * fault. Without this it surfaced as a 500 carrying a Java stack trace and
	 * no usable message, so the UI had nothing to show the user.
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public final ResponseEntity<ExceptionResponse> handleDataIntegrity(DataIntegrityViolationException ex,
			WebRequest request) {

		String detail = ex.getMostSpecificCause().getMessage();
		String message = detail != null && detail.contains("email")
				? "A patient with this email address already exists"
				: "That value conflicts with an existing record";

		logger.error("Constraint violation: " + detail);
		return new ResponseEntity<>(new ExceptionResponse(new Date(), message), HttpStatus.CONFLICT);
	}
}
