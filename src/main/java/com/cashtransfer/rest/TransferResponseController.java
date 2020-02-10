package com.cashtransfer.rest;

import com.cashtransfer.model.TransferRequest;
import com.cashtransfer.model.TransferRequestStatus;
import com.cashtransfer.model.TransferResponse;
import com.cashtransfer.model.TransferResponseVerdict;
import com.cashtransfer.service.TransferRequestService;
import com.cashtransfer.service.TransferResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@Api(value = "Transfer response")
@RequestMapping(value = "/api/transfer-response")
public class TransferResponseController {

	@Autowired
	private TransferResponseService transferResponseService;
	@Autowired
	private TransferRequestService transferRequestService;

	@ApiOperation(value = "Gets all  transfer responses", response = List.class)
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<?> list(){
		List<TransferResponse> transferResponses =  transferResponseService.findAll();
		return ResponseEntity.ok().body(transferResponses);
	}

	@ApiOperation(value = "Gets the transfer response with the specified id", response = TransferResponse.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable String id){
		TransferResponse  transferResponse = transferResponseService.findById(id);
		if(transferResponse == null){
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(transferResponse);
	}

	@ApiOperation(value = "Gets the transfer response with the specified transfer request id", response = TransferResponse.class)
	@RequestMapping(value = "/transfer-request/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findByRequestId(@PathVariable String id){
		TransferRequest  transferRequest = transferRequestService.findById(id);
		if(transferRequest == null){
			return ResponseEntity.notFound().build();
		}
		TransferResponse transferResponse = transferResponseService.findByRequest(transferRequest);
		return ResponseEntity.ok().body(transferResponse);
	}

	@ApiOperation(value = "Checks whether a response exists", response = TransferResponse.class)
	@RequestMapping(value = "/transfer-request/exists/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> checkIfResponseExists(@PathVariable String id){
		TransferRequest  transferRequest = transferRequestService.findById(id);
		if(transferRequest == null){
			return ResponseEntity.notFound().build();
		}
		TransferResponse transferResponse = transferResponseService.findByRequest(transferRequest);
		return ResponseEntity.ok().body(transferResponse != null);
	}


	@ApiOperation(value = "Completes the transfer response after the exchange", response = TransferResponse.class)
	@RequestMapping(value = "/{id}/complete", method = RequestMethod.POST)
	public ResponseEntity<?> complete(Principal principal, @PathVariable String id){
		TransferResponse  transferResponse = transferResponseService.findById(id);
		if(transferResponse == null){
			return ResponseEntity.notFound().build();
		}

		//verify principal
		if(!transferResponse.getHandshakeUser().getUsername().equals(principal.getName())){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to complete the handshake");
		}

		TransferRequest transferRequest = transferResponse.getTransferRequest();
		transferRequest.setStatus(TransferRequestStatus.COMPLETED);
		transferRequestService.save(transferRequest);

		transferResponse.setCompleted(true);
		transferResponse.setDateCompleted(new Date());
		TransferResponse response = transferResponseService.save(transferResponse);
		return ResponseEntity.ok().body(response);
	}

}
