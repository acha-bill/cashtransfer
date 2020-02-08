package com.cashtransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
	@Id
	private
	String id;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
