package com.cashtransfer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class TransferRequest {
	@Id
	private String id;
	@NotNull
	@Min(value = 1_000)
	@Max(value = 1_000_000)
	private double amount;
	@NotNull
	private Date dateCreated;
	private TransferRequestStatus status;
	@NotNull
	@DBRef
	private User fromUser;
	@DBRef
	private User toUser;
	private TransferFee  transferFee;

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

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public TransferRequestStatus getStatus() {
		return status;
	}

	public void setStatus(TransferRequestStatus status) {
		this.status = status;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	public TransferFee getTransferFee() {
		return transferFee;
	}

	public void setTransferFee(TransferFee transferFee) {
		this.transferFee = transferFee;
	}
}
