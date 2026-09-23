package com.medora.security;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.medora.entity.User;
import com.medora.entity.enums.Role;
import com.medora.repository.UserRepository;

/**
 * Creates one account per role the first time the application starts.
 *
 * Runs only when the users table is empty, so restarts never overwrite
 * credentials that have since been changed. Roles whose password is blank are
 * skipped, which is what keeps this inactive during tests.
 */
@Component
public class UserSeeder implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(UserSeeder.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final Map<Role, String> passwords;

	public UserSeeder(UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			@Value("${medora.seed.admin-password}") String adminPassword,
			@Value("${medora.seed.doctor-password}") String doctorPassword,
			@Value("${medora.seed.receptionist-password}") String receptionistPassword) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.passwords = Map.of(
				Role.ADMIN, adminPassword,
				Role.DOCTOR, doctorPassword,
				Role.RECEPTIONIST, receptionistPassword);
	}

	@Override
	public void run(String... args) {
		if (userRepository.count() > 0) {
			log.info("Users already present, skipping seed");
			return;
		}

		passwords.forEach((role, password) -> {
			if (password == null || password.isBlank()) {
				log.warn("No password configured for {}, account not created", role);
				return;
			}
			userRepository.save(newUser(role, password));
			log.info("Seeded {} account with username '{}'", role, role.name().toLowerCase());
		});
	}

	private User newUser(Role role, String rawPassword) {
		User user = new User();
		user.setName(role.name().charAt(0) + role.name().substring(1).toLowerCase());
		user.setUsername(role.name().toLowerCase());
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setRole(role);
		return user;
	}
}
