package com.cashtransfer.repository;

import com.cashtransfer.model.TransferFee;
import com.cashtransfer.model.TransferRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransferFeeRepository extends MongoRepository<TransferFee, String> {
	List<TransferFee> findByTransferRequest(TransferRequest transferRequest);
	TransferFee findById(String id);
}
