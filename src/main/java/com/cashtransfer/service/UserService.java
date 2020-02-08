package com.cashtransfer.service;

import com.cashtransfer.model.User;

import java.util.List;

public interface UserService {
	User findById(String id);

	User findByUsername(String username);

	List<User> findAll();

	User save(User user);
}
