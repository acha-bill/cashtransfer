package com.cashtransfer.repository;

import com.cashtransfer.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	User findByUsername(String username);
}

