package com.medora.util;

public class ApiPaths {
	private static final String BASE_PATH = "/api";
	private static final String PATIENT_PATH = "/patient";
	private static final String PROBLEM_PATH = "/problem";
	private static final String PRESCRIPTION_PATH = "/prescription";
	private static final String STAFF_PATH = "/staff";
	private static final String USER_PATH = "/user";

	public static final class PatientCtrl {
		public static final String CTRL = BASE_PATH + PATIENT_PATH;
	}
	public static final class ProblemCtrl {
		public static final String CTRL = BASE_PATH + PROBLEM_PATH;
	}
	public static final class PrescriptionCtrl {
		public static final String CTRL = BASE_PATH + PRESCRIPTION_PATH;
	}
	public static final class StaffCtrl {
		public static final String CTRL = BASE_PATH + STAFF_PATH;
	}
	public static final class UserCtrl {
		public static final String CTRL = BASE_PATH + USER_PATH;
	}
}
