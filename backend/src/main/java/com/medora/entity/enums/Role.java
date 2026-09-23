package com.medora.entity.enums;

/**
 * Permission tiers for staff accounts.
 *
 * Stored as text via {@code @Enumerated(EnumType.STRING)}, so the constant
 * names are part of the database contract. Spring Security matches these with
 * a {@code ROLE_} prefix, which UserDetailsServiceImpl adds.
 */
public enum Role {

	/** Front desk. Registers and updates patients. */
	RECEPTIONIST,

	/** Clinician. Adds and edits diagnoses and prescriptions. */
	DOCTOR,

	/** Full access, including deletes and staff management. */
	ADMIN
}
