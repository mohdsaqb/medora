package com.medora.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.medora.entity.Patient;
import com.medora.entity.enums.City;
import com.medora.exception.ValidationException;
import com.medora.service.PatientService;

/**
 * Covers the patient endpoints and the anonymous half of the access rules.
 * Role specific behaviour lives in {@link com.medora.security.SecurityConfigTest}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PatientService patientService;

	/**
	 * findAll throws PatientNotFoundException when no rows match, which the
	 * handler turns into a 404. So the list endpoint needs at least one patient
	 * before it answers 200.
	 */
	@BeforeEach
	void ensureAPatientExists() {
		// Email is unique and this runs before every test, so the address has
		// to differ each time.
		String email = "seed." + UUID.randomUUID() + "@example.com";
		Patient seed = new Patient("Seed", "Patient", "Female", City.MUMBAI, email, 1);
		// Phone is required on create and this constructor does not set it.
		seed.setPhoneNo("+91 90000 00000");
		patientService.save(seed);
	}

	@Test
	void anonymousCanListPatients() throws Exception {
		mockMvc.perform(get("/api/patient"))
				.andExpect(status().isOk());
	}

	@Test
	void anonymousCannotCreateAPatient() throws Exception {
		mockMvc.perform(post("/api/patient")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void anonymousCannotDeleteAPatient() throws Exception {
		mockMvc.perform(delete("/api/patient/1"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void serviceAssignsAnIdOnSave() {
		Patient patient = new Patient("Ananya", "Iyer", "Female", City.MUMBAI, "ananya.iyer@example.com", 1);
		patient.setPhoneNo("+91 90000 00001");

		Patient saved = patientService.save(patient);

		assertThat(saved.getPatientid()).isNotNull();
	}

	@Test
	void createRejectsAPatientWithoutAPhoneNumber() {
		Patient patient = new Patient("NoPhone", "Patient", "Male", City.DELHI,
				"nophone." + UUID.randomUUID() + "@example.com", 1);

		assertThatThrownBy(() -> patientService.save(patient))
				.isInstanceOf(ValidationException.class)
				.hasMessageContaining("phone number");
	}

	@Test
	void updateKeepsFieldsLeftOutOfThePayload() throws Exception {
		Patient patient = new Patient("Partial", "Update", "Female", City.PUNE,
				"partial." + UUID.randomUUID() + "@example.com", 1);
		patient.setPhoneNo("+91 90000 00002");
		Patient saved = patientService.save(patient);

		Patient changes = new Patient();
		changes.setLastname("Changed");
		patientService.update(saved.getPatientid(), changes);

		Patient after = patientService.findByEmail(saved.getEmail());
		assertThat(after.getLastname()).isEqualTo("Changed");
		// Omitted fields must survive the merge.
		assertThat(after.getPhoneNo()).isEqualTo("+91 90000 00002");
		assertThat(after.getStatus()).isEqualTo(1);
	}

	@Test
	void aPatientWithNoDiagnosesGetsAnEmptyListNotA404() throws Exception {
		Patient patient = new Patient("NoProblems", "Patient", "Male", City.KOCHI,
				"noproblems." + UUID.randomUUID() + "@example.com", 1);
		patient.setPhoneNo("+91 90000 00003");
		Patient saved = patientService.save(patient);

		mockMvc.perform(get("/api/problem/find-all-by-patientid/" + saved.getPatientid()))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}
}
