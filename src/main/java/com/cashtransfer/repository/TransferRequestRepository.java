package com.cashtransfer.repository;

import com.cashtransfer.model.TransferRequest;
import com.cashtransfer.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransferRequestRepository extends MongoRepository<TransferRequest, String> {
	List<TransferRequest> findByFromUser(User user);
	List<TransferRequest> findByToUser(User user);
}
