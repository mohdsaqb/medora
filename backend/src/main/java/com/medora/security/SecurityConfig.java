package com.medora.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

/**
 * HTTP Basic authentication with role based access.
 *
 * Reads are public so the demo can be shared without credentials. Writes are
 * split by role, and deletes are admin only.
 *
 * Roles are listed explicitly on each rule rather than through a role
 * hierarchy, so the effective permissions are readable directly from this file.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final List<String> allowedOrigins;

	public SecurityConfig(@Value("${medora.cors.allowed-origins}") List<String> allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			// No browser sessions and no server side state, so CSRF tokens do
			// not apply. Credentials arrive on every request instead.
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				// Preflight requests carry no credentials.
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				// The error dispatch must stay reachable. Without this, any
				// unhandled exception is answered 401 instead of its real
				// status, which makes server faults look like auth failures.
				.requestMatchers("/error").permitAll()

				// Sign in check. Must sit above the public GET rule below,
				// because the first matching rule wins and this one has to
				// reject anonymous callers to be useful.
				.requestMatchers("/api/user/**").authenticated()

				// Deleting anything is admin only.
				.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

				// Staff records are managed by admins.
				.requestMatchers(HttpMethod.POST, "/api/staff/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/api/staff/**").hasRole("ADMIN")

				// Diagnoses and prescriptions are clinical work.
				.requestMatchers(HttpMethod.POST, "/api/problem/**", "/api/prescription/**")
					.hasAnyRole("DOCTOR", "ADMIN")
				.requestMatchers(HttpMethod.PUT, "/api/problem/**", "/api/prescription/**")
					.hasAnyRole("DOCTOR", "ADMIN")

				// Registering and updating patients is front desk work.
				.requestMatchers(HttpMethod.POST, "/api/patient/**")
					.hasAnyRole("RECEPTIONIST", "DOCTOR", "ADMIN")
				.requestMatchers(HttpMethod.PUT, "/api/patient/**")
					.hasAnyRole("RECEPTIONIST", "DOCTOR", "ADMIN")

				// Reads stay open so the deployed demo needs no login.
				.requestMatchers(HttpMethod.GET, "/api/**").permitAll()

				.anyRequest().authenticated())
			.httpBasic(basic -> basic.authenticationEntryPoint(
				// Plain 401 with no WWW-Authenticate header, otherwise the
				// browser shows its own credential dialog over the React app.
				(request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)));

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(allowedOrigins);
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		// Required for the browser to send the Basic credentials header.
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
