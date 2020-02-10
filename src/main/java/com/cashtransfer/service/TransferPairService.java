package com.cashtransfer.service;

import com.cashtransfer.model.TransferPair;
import com.cashtransfer.model.TransferRequest;

import java.util.List;

public interface TransferPairService {
	List<TransferPair> findAll();

	TransferPair findById(String id);

	TransferPair findByTransferRequest(TransferRequest request);

	TransferPair save(TransferPair transferPair);
}
