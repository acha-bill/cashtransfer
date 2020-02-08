package com.cashtransfer.rest;

import com.cashtransfer.model.Authority;
import com.cashtransfer.model.User;
import com.cashtransfer.model.UserRoleName;
import com.cashtransfer.model.UserTokenState;
import com.cashtransfer.security.TokenHelper;
import com.cashtransfer.security.auth.JwtAuthenticationRequest;
import com.cashtransfer.service.UserService;
import com.cashtransfer.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	private final TokenHelper tokenHelper;
	@Lazy
	private final AuthenticationManager authenticationManager;
	private final CustomUserDetailsService userDetailsService;
	private final UserService userService;

	public AuthenticationController(UserService userService, TokenHelper tokenHelper, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService) {
		this.tokenHelper = tokenHelper;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.userService = userService;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
		User existingUser = userService.findByUsername(user.getUsername());
		if(existingUser != null){
			return ResponseEntity.status(409).body("Username already exist");
		}

		Authority authority = new Authority();
		authority.setName(UserRoleName.ROLE_USER);
		List<Authority> authorities = new ArrayList<Authority>(){{
			add(authority);
		}};
		user.setAuthorities(authorities);
		User newUser = userService.save(user);

		//login user
		JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest(newUser.getUsername(), newUser.getPassword());
		return createAuthenticationToken(jwtAuthenticationRequest);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest
	) throws AuthenticationException {

		// Perform the security
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						authenticationRequest.getUsername(),
						authenticationRequest.getPassword()
				)
		);

		// Inject into security context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// token creation
		User user = (User) authentication.getPrincipal();
		String jwt = tokenHelper.generateToken(user.getUsername());
		int expiresIn = tokenHelper.getExpiredIn();
		// Return the token
		return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public ResponseEntity<?> refreshAuthenticationToken(
			HttpServletRequest request,
			HttpServletResponse response,
			Principal principal
	) {

		String authToken = tokenHelper.getToken(request);

		if (authToken != null && principal != null) {

			// TODO check user password last update
			String refreshedToken = tokenHelper.refreshToken(authToken);
			int expiresIn = tokenHelper.getExpiredIn();

			return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
		} else {
			UserTokenState userTokenState = new UserTokenState();
			return ResponseEntity.accepted().body(userTokenState);
		}
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChanger passwordChanger) {
		userDetailsService.changePassword(passwordChanger.oldPassword, passwordChanger.newPassword);
		Map<String, String> result = new HashMap<>();
		result.put("result", "success");
		return ResponseEntity.accepted().body(result);
	}

	static class PasswordChanger {
		public String oldPassword;
		public String newPassword;
	}
}