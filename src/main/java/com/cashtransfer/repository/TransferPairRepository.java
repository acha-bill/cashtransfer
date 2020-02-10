package com.cashtransfer.repository;

import com.cashtransfer.model.TransferPair;
import com.cashtransfer.model.TransferRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransferPairRepository extends MongoRepository<TransferPair, String> {
	TransferPair findByRequest1OrRequest2(TransferRequest request);
}
