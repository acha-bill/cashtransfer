package com.cashtransfer.config;

import com.cashtransfer.model.Authority;
import com.cashtransfer.model.Country;
import com.cashtransfer.model.User;
import com.cashtransfer.model.UserRoleName;
import com.cashtransfer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultAdminConfig {
	@Autowired
	UserService userService;
	@Value("${admin.user.username}")
	private String username;
	@Value("${admin.user.password}")
	private String password;
	@Value("${admin.user.first_name}")
	private String firstName;
	@Value("${admin.user.last_name}")
	private String lastName;
	@Value("${admin.user.email}")
	private String email;
	@Value("${admin.user.country}")
	private String country;
	@Value("${admin.user.phone_number}")
	private String phoneNumber;

	public void setup() {
		User admin = userService.findByUsername(username);
		if (admin != null) {
			return;
		}

		admin = new User();
		admin.setUsername(username);
		admin.setFirstName(firstName);
		admin.setLastName(lastName);
		admin.setPassword(password);
		admin.setEmail(email);
		admin.setCountry(new Country("Cameroon", "CM"));
		admin.setPhoneNumber(phoneNumber);
		admin.setEnabled(true);

		Authority userAuthority = new Authority();
		userAuthority.setName(UserRoleName.ROLE_USER);
		Authority adminAuthority = new Authority();
		adminAuthority.setName(UserRoleName.ROLE_ADMIN);
		List<Authority> authorities = new ArrayList<Authority>() {{
			add(userAuthority);
			add(adminAuthority);
		}};
		admin.setAuthorities(authorities);

		userService.save(admin);
	}
}
