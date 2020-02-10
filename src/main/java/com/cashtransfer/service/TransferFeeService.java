package com.cashtransfer.service;

import com.cashtransfer.model.TransferFee;

import java.util.List;

public interface TransferFeeService {
	List<TransferFee> findAll();

	TransferFee save(TransferFee transferFee);

	TransferFee findById(String id);
}
