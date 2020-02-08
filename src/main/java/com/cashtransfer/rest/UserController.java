package com.cashtransfer.rest;

import com.cashtransfer.model.User;
import com.cashtransfer.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(method = GET, value = "/user/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public User loadById(@PathVariable String userId) {
		return this.userService.findById(userId);
	}

	@RequestMapping(method = GET, value = "/user/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> loadAll() {
		return this.userService.findAll();
	}


	@RequestMapping("/whoami")
	public User user(Principal user) {
		return this.userService.findByUsername(user.getName());
	}
}
