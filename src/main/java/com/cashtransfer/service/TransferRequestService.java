package com.cashtransfer.service;

import com.cashtransfer.model.TransferFee;
import com.cashtransfer.model.TransferRequest;
import com.cashtransfer.model.TransferRequestStatus;
import com.cashtransfer.model.User;

import java.util.List;

public interface TransferRequestService {
	List<TransferRequest> findByFromUser(User user);

	List<TransferRequest> findByToUser(User user);

	List<TransferRequest> findAll();

	TransferRequest findById(String id);

	TransferRequest save(TransferRequest transferRequest);

	void setFee(TransferRequest transferRequest, TransferFee transferFee);

	void updateStatus(TransferRequest transferRequest, TransferRequestStatus status);
}
