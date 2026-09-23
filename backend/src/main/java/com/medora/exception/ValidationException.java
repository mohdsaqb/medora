package com.medora.exception;

/**
 * A request was rejected because required values were missing or unusable.
 *
 * Kept separate from bean validation annotations on the entity: those would
 * also apply to PUT, where a partial payload is legitimate and omitted fields
 * are meant to be left untouched.
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
		super(message);
	}
}
