package com.cashtransfer.service;

import com.cashtransfer.model.TransferRequest;
import com.cashtransfer.model.TransferResponse;

import java.util.List;

public interface TransferResponseService {
	TransferResponse getNewResponse(TransferRequest request);

	TransferResponse findByRequest(TransferRequest request);

	TransferResponse findById(String id);

	List<TransferResponse> findAll();

	TransferResponse save(TransferResponse transferResponse);
}
