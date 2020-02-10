package com.cashtransfer.service.impl;

import com.cashtransfer.model.TransferFee;
import com.cashtransfer.repository.TransferFeeRepository;
import com.cashtransfer.service.TransferFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferFeeServiceImpl implements TransferFeeService {
	@Autowired
	private TransferFeeRepository transferFeeRepository;

	@Override
	public List<TransferFee> findAll() {
		return transferFeeRepository.findAll();
	}

	@Override
	public TransferFee findById(String id) {
		return transferFeeRepository.findOne(id);
	}

	@Override
	public TransferFee save(TransferFee transferFee) {
		return transferFeeRepository.save(transferFee);
	}
}
