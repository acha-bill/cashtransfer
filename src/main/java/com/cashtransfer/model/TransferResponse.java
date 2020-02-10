package com.cashtransfer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class TransferResponse {
	@Id
	private String id;
	@DBRef
	@NotNull
	private TransferRequest transferRequest;
	@NotNull
	private Date dateCreated;
	@NotNull
	private User handshakeUser;
	private boolean isCompleted;
	private Date dateCompleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TransferRequest getTransferRequest() {
		return transferRequest;
	}

	public void setTransferRequest(TransferRequest transferRequest) {
		this.transferRequest = transferRequest;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public User getHandshakeUser() {
		return handshakeUser;
	}

	public void setHandshakeUser(User handshakeUser) {
		this.handshakeUser = handshakeUser;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean completed) {
		isCompleted = completed;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}
}
