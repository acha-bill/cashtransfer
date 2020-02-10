package com.cashtransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
	private UserRoleName name;

	@Override
	public String getAuthority() {
		return name.name();
	}

	@JsonIgnore
	public UserRoleName getName() {
		return name;
	}

	public void setName(UserRoleName name) {
		this.name = name;
	}
}
