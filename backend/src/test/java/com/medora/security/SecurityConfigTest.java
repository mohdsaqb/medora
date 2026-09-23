package com.medora.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.medora.entity.User;
import com.medora.entity.enums.Role;
import com.medora.repository.UserRepository;

/**
 * Verifies the role rules in SecurityConfig.
 *
 * Where a role is allowed through, the assertion is that the response was not
 * 401 or 403. The request body is deliberately minimal, so the controller may
 * still answer 400. That is enough to prove authorization passed, without
 * building a valid payload for every endpoint.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

	private static final String PASSWORD = "test-password";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void seedAccounts() {
		for (Role role : Role.values()) {
			String username = role.name().toLowerCase();
			if (userRepository.findByUsername(username).isEmpty()) {
				User user = new User();
				user.setName(username);
				user.setUsername(username);
				user.setPassword(passwordEncoder.encode(PASSWORD));
				user.setRole(role);
				userRepository.save(user);
			}
		}
	}

	private int statusOf(String username, org.springframework.test.web.servlet.RequestBuilder request) throws Exception {
		return mockMvc.perform(request).andReturn().getResponse().getStatus();
	}

	@Test
	void signInReportsTheRole() throws Exception {
		mockMvc.perform(get("/api/user/me").with(httpBasic("doctor", PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("DOCTOR")));
	}

	@Test
	void wrongPasswordIsRejected() throws Exception {
		mockMvc.perform(get("/api/user/me").with(httpBasic("doctor", "wrong")))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void anonymousCannotReachTheSignInCheck() throws Exception {
		mockMvc.perform(get("/api/user/me"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void receptionistCannotWriteClinicalRecords() throws Exception {
		mockMvc.perform(post("/api/problem")
						.contentType(MediaType.APPLICATION_JSON).content("{}")
						.with(httpBasic("receptionist", PASSWORD)))
				.andExpect(status().isForbidden());
	}

	@Test
	void doctorCanWriteClinicalRecords() throws Exception {
		int status = statusOf("doctor", post("/api/problem")
				.contentType(MediaType.APPLICATION_JSON).content("{}")
				.with(httpBasic("doctor", PASSWORD)));

		assertThat(status).isNotIn(401, 403);
	}

	@Test
	void doctorCannotDelete() throws Exception {
		mockMvc.perform(delete("/api/patient/1").with(httpBasic("doctor", PASSWORD)))
				.andExpect(status().isForbidden());
	}

	@Test
	void adminCanDelete() throws Exception {
		int status = statusOf("admin", delete("/api/patient/1").with(httpBasic("admin", PASSWORD)));

		assertThat(status).isNotIn(401, 403);
	}

	@Test
	void onlyAdminsManageStaff() throws Exception {
		mockMvc.perform(post("/api/staff")
						.contentType(MediaType.APPLICATION_JSON).content("{}")
						.with(httpBasic("doctor", PASSWORD)))
				.andExpect(status().isForbidden());

		int adminStatus = statusOf("admin", post("/api/staff")
				.contentType(MediaType.APPLICATION_JSON).content("{}")
				.with(httpBasic("admin", PASSWORD)));

		assertThat(adminStatus).isNotIn(401, 403);
	}

	@Test
	void passwordsAreStoredHashed() {
		User admin = userRepository.findByUsername("admin").orElseThrow();

		assertThat(admin.getPassword()).startsWith("$2");
		assertThat(admin.getPassword()).isNotEqualTo(PASSWORD);
	}
}
