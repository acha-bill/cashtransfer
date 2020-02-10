package com.cashtransfer.service.impl;

import com.cashtransfer.model.TransferPair;
import com.cashtransfer.model.TransferRequest;
import com.cashtransfer.repository.TransferPairRepository;
import com.cashtransfer.service.TransferPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferPairServiceImpl implements TransferPairService {
	@Autowired
	private TransferPairRepository repository;

	@Override
	public List<TransferPair> findAll() {
		return repository.findAll();
	}

	@Override
	public TransferPair findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public TransferPair findByTransferRequest(TransferRequest request) {
		return repository.findByRequest1OrRequest2(request);
	}

	@Override
	public TransferPair save(TransferPair transferPair) {
		return repository.save(transferPair);
	}
}
