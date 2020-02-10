package com.cashtransfer.service.impl;

import com.cashtransfer.model.TransferFee;
import com.cashtransfer.model.TransferRequest;
import com.cashtransfer.model.TransferRequestStatus;
import com.cashtransfer.model.User;
import com.cashtransfer.repository.TransferRequestRepository;
import com.cashtransfer.service.TransferRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferRequestServiceImpl implements TransferRequestService {
	@Autowired
	private TransferRequestRepository repository;

	@Override
	public List<TransferRequest> findByFromUser(User user) {
		return repository.findByFromUser(user);
	}

	@Override
	public List<TransferRequest> findByToUser(User user) {
		return repository.findByToUser(user);
	}

	@Override
	public List<TransferRequest> findAll() {
		return repository.findAll();
	}

	@Override
	public TransferRequest findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public TransferRequest save(TransferRequest transferRequest) {
		return repository.save(transferRequest);
	}

	@Override
	public void setFee(TransferRequest transferRequest, TransferFee transferFee) {
		transferRequest.setTransferFee(transferFee);
		repository.save(transferRequest);
	}

	@Override
	public void updateStatus(TransferRequest transferRequest, TransferRequestStatus status) {
		transferRequest.setStatus(status);
		repository.save(transferRequest);
	}
}
