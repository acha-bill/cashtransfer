package com.cashtransfer.rest;

import com.cashtransfer.model.Authority;
import com.cashtransfer.model.User;
import com.cashtransfer.model.UserRoleName;
import com.cashtransfer.model.UserTokenState;
import com.cashtransfer.security.TokenHelper;
import com.cashtransfer.security.auth.JwtAuthenticationRequest;
import com.cashtransfer.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
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
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Authentication API")
public class AuthenticationController {

	private final TokenHelper tokenHelper;
	@Lazy
	private final AuthenticationManager authenticationManager;
	private final UserService userService;

	public AuthenticationController(UserService userService, TokenHelper tokenHelper, AuthenticationManager authenticationManager) {
		this.tokenHelper = tokenHelper;
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ApiOperation(value = "Create a new user", notes = "It creates a new user and then logs in the user automatically")
	public UserTokenState createUser(@Valid @RequestBody User user) throws Exception {
		User existingUser = userService.findByUsername(user.getUsername());
		if (existingUser != null) {
			throw new Exception("Username already exist");
		}

		Authority authority = new Authority();
		authority.setName(UserRoleName.ROLE_USER);
		List<Authority> authorities = new ArrayList<Authority>() {{
			add(authority);
		}};
		user.setAuthorities(authorities);
		user.setEnabled(true);
		User newUser = userService.save(user);

		//login user
		JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest(newUser.getUsername(), newUser.getPassword());
		return createAuthenticationToken(jwtAuthenticationRequest);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ApiOperation(value = "Login")
	public UserTokenState createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
		// Perform the security
		final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		// Inject into security context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// token creation
		User user = (User) authentication.getPrincipal();
		String jwt = tokenHelper.generateToken(user.getUsername());
		int expiresIn = tokenHelper.getExpiredIn();
		// Return the token
		return new UserTokenState(jwt, expiresIn);
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	@ApiOperation(value = "Refresh a jwt")
	public UserTokenState refreshAuthenticationToken(HttpServletRequest request, Principal principal) {
		String authToken = tokenHelper.getToken(request);

		if (authToken != null && principal != null) {
			// TODO check user password last update
			String refreshedToken = tokenHelper.refreshToken(authToken);
			int expiresIn = tokenHelper.getExpiredIn();

			return new UserTokenState(refreshedToken, expiresIn);
		}
		return new UserTokenState();
	}
}