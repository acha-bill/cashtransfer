package com.cashtransfer.service.impl;

import com.cashtransfer.model.TransferRequest;
import com.cashtransfer.model.TransferResponse;
import com.cashtransfer.repository.TransferResponseRepository;
import com.cashtransfer.service.TransferResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class TransferResponseServiceImpl implements TransferResponseService {
	@Autowired
	private TransferResponseRepository repository;

	@Override
	public TransferResponse getNewResponse(TransferRequest request) {
		//find appropriate response for request
		throw new NotImplementedException();
	}

	@Override
	public TransferResponse findByRequest(TransferRequest request) {
		return repository.findByTransferRequest(request);
	}

	@Override
	public TransferResponse findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public List<TransferResponse> findAll() {
		return repository.findAll();
	}

	@Override
	public TransferResponse save(TransferResponse transferResponse) {
		return repository.save(transferResponse);
	}
}
