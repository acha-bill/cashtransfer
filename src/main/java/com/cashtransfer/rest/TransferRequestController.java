package com.cashtransfer.rest;

import com.cashtransfer.model.*;
import com.cashtransfer.service.TransferPairService;
import com.cashtransfer.service.TransferRequestService;
import com.cashtransfer.service.TransferResponseService;
import com.cashtransfer.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/transfer-request")
@Api(value = "Request for a transfer")
public class TransferRequestController {

	@Autowired
	private TransferRequestService transferRequestService;
	@Autowired
	private UserService userService;
	@Autowired
	private TransferResponseService transferResponseService;
	@Autowired
	private TransferPairService transferPairService;

	@ApiOperation(value = "Create a new transfer request", response = TransferRequest.class)
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<?> create(@Valid TransferRequest request) {
		request.setTransferFee(null);
		TransferRequest transferRequest = transferRequestService.save(request);
		return ResponseEntity.created(URI.create("/")).body(transferRequest);
	}

	@ApiOperation(value = "List all transfer requests by all users", notes = "Only ADMIN", response = List.class)
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> list() {
		List<TransferRequest> transferRequests = transferRequestService.findAll();
		return ResponseEntity.ok().body(transferRequests);
	}

	@ApiOperation(value = "Gets the request with the specified Id", response = TransferRequest.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		TransferRequest transferRequest = transferRequestService.findById(id);
		if (transferRequest == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(transferRequest);
	}

	@ApiOperation(value = "Get all transfer requests that have been put in by the current user")
	@RequestMapping(value = "/my-requests", method = RequestMethod.GET)
	public ResponseEntity<?> listByUser(Principal principal) {
		User user = userService.findByUsername(principal.getName());
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		List<TransferRequest> requests = transferRequestService.findByFromUser(user);
		return ResponseEntity.ok().body(requests);
	}

	@ApiOperation(value = "Gets the transfer response for the specified request", response = TransferResponse.class)
	@RequestMapping(value = "/{id}/transfer-response", method = RequestMethod.GET)
	public ResponseEntity<?> getResponse(@PathVariable String id) {
		TransferRequest transferRequest = transferRequestService.findById(id);
		if (transferRequest == null) {
			return ResponseEntity.notFound().build();
		}

		TransferResponse existingResponse = transferResponseService.findByRequest(transferRequest);
		if(existingResponse != null){
			return ResponseEntity.ok().body(existingResponse);
		}

		TransferRequest transferRequestToPair = null;
		TransferResponse transferResponse = null;
		List<TransferRequest> potentialTransferRequestsToPair = getPotentialRequestsToPair(transferRequest);
		if(potentialTransferRequestsToPair.size() > 0) {
			transferRequestToPair = potentialTransferRequestsToPair.get(0);
		}

		TransferPair transferPair = transferPairService.findByTransferRequest(transferRequest);
		if(transferPair == null){
			TransferPair transferPair1 =  transferPairService.findByTransferRequest(transferRequestToPair);
			if(transferPair1 != null && transferPair1.getRequest2() == null){
				transferPair1.setRequest2(transferRequest);
				transferPairService.save(transferPair1);
				transferResponse = new TransferResponse();
				transferResponse.setDateCreated(new Date());
				transferResponse.setTransferRequest(transferRequest);
				transferResponse.setHandshakeUser(transferPair1.getRequest1().getToUser());
				transferResponseService.save(transferResponse);

				TransferResponse  response1 = transferResponseService.findByRequest(transferPair1.getRequest1());
				if(response1 == null){
					response1 = new TransferResponse();
					response1.setDateCreated(new Date());
					response1.setTransferRequest(transferPair1.getRequest1());
					response1.setHandshakeUser(transferRequest.getToUser());
					transferResponseService.save(response1);

					transferRequestService.updateStatus(transferPair1.getRequest1(), TransferRequestStatus.PAIRED);
					transferRequestService.updateStatus(transferPair1.getRequest2(), TransferRequestStatus.PAIRED);
				}
			}else{
				transferPair = new TransferPair();
				transferPair.setRequest1(transferRequest);
				transferPair.setRequest2(transferRequestToPair);
				transferPairService.save(transferPair);
			}
		}

		return ResponseEntity.ok().body(transferResponse);
	}

	private List<TransferRequest> getPotentialRequestsToPair(TransferRequest  transferRequest){
		Country fromCountry = transferRequest.getFromUser().getCountry();
		Country toCountry = transferRequest.getToUser().getCountry();
		return transferRequestService.findAll()
				.stream().filter(request -> request.getStatus().equals(TransferRequestStatus.PENDING) &&
						request.getToUser().getCountry().equals(fromCountry) &&
						request.getFromUser().getCountry().equals(toCountry) &&
						!request.getFromUser().equals(transferRequest.getFromUser())).sorted(Comparator.comparing(TransferRequest::getDateCreated))
				.collect(Collectors.toList());
	}
}
