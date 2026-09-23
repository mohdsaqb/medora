package com.medora.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.medora.repository.UserRepository;

/**
 * Loads staff accounts for authentication.
 *
 * The Role enum stores bare names such as DOCTOR, while Spring Security matches
 * authorities with a ROLE_ prefix. That prefix is added here, so hasRole("DOCTOR")
 * in SecurityConfig lines up with a stored role of DOCTOR.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		com.medora.entity.User account = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("No account for username " + username));

		return User.withUsername(account.getUsername())
				.password(account.getPassword())
				.authorities(List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name())))
				.build();
	}
}
