package com.cashtransfer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

public class TransferFee {
	@Id
	private String id;
	private double amount;
	private Date date;
	@DBRef
	private TransferRequest transferRequest;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public TransferRequest getTransferRequest() {
		return transferRequest;
	}

	public void setTransferRequest(TransferRequest transferRequest) {
		this.transferRequest = transferRequest;
	}
}
