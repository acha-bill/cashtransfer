package com.cashtransfer.repository;

import com.cashtransfer.model.TransferRequest;
import com.cashtransfer.model.TransferResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransferResponseRepository extends MongoRepository<TransferResponse, String> {
	TransferResponse findByTransferRequest(TransferRequest transferRequest);
}
