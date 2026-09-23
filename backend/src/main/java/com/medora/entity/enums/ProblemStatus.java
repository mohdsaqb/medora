package com.medora.entity.enums;

/**
 * Where a patient is being treated for a given diagnosis.
 *
 * Stored as text via {@code @Enumerated(EnumType.STRING)}, so these constant
 * names are part of the database contract.
 */
public enum ProblemStatus {

	/** Seen and sent home the same visit. */
	OUTPATIENT,

	/** Admitted to a ward. */
	INPATIENT,

	/** Casualty or emergency department. */
	EMERGENCY,

	/** In surgery. */
	OPERATION_THEATRE,

	/** Intensive care unit. */
	INTENSIVE_CARE
}
