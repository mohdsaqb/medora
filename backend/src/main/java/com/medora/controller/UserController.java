package com.medora.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medora.util.ApiPaths;

@RestController
@RequestMapping(ApiPaths.UserCtrl.CTRL)
public class UserController {

	/**
	 * Returns the signed in account. The client calls this after collecting
	 * credentials: a 200 confirms they are valid and reports the role, while a
	 * 401 means the sign in failed. HTTP Basic has no login endpoint of its
	 * own, so this stands in for one.
	 */
	@GetMapping("/me")
	public ResponseEntity<Map<String, String>> currentUser(Authentication authentication) {
		String role = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.filter(authority -> authority.startsWith("ROLE_"))
				.findFirst()
				.map(authority -> authority.substring("ROLE_".length()))
				.orElse("");

		return ResponseEntity.ok(Map.of(
				"username", authentication.getName(),
				"role", role));
	}
}
