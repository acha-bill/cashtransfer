package com.cashtransfer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class TransferPair {
	@Id
	private String id;
	@DBRef
	private TransferRequest request1;
	@DBRef
	private TransferRequest request2;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TransferRequest getRequest1() {
		return request1;
	}

	public void setRequest1(TransferRequest request1) {
		this.request1 = request1;
	}

	public TransferRequest getRequest2() {
		return request2;
	}

	public void setRequest2(TransferRequest request2) {
		this.request2 = request2;
	}
}
