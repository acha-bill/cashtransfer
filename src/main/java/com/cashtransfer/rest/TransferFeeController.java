package com.cashtransfer.rest;

import com.cashtransfer.model.TransferFee;
import com.cashtransfer.model.TransferRequest;
import com.cashtransfer.service.TransferFeeService;
import com.cashtransfer.service.TransferRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Api(value = "Transfer fees")
@RequestMapping(value = "/api/transfer-fee")
public class TransferFeeController {

	private static final double TRANSFER_REQUEST_FEE_PERCENTAGE = 0.05F;

	@Autowired
	private TransferFeeService transferFeeService;
	@Autowired
	private TransferRequestService transferRequestService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ApiOperation(value = "Get all transfer fees", response = List.class)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> list(){
		List<TransferFee> transferFees = transferFeeService.findAll();
		return ResponseEntity.ok().body(transferFees);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get the transfer fee with the specified id")
	public ResponseEntity<?> findById(@PathVariable  String id){
		TransferFee transferFee = transferFeeService.findById(id);
		if(transferFee != null){
			return ResponseEntity.ok().body(transferFee);
		}
		return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/transfer-request/{id}", method = RequestMethod.POST)
	@ApiOperation(value = "Adds a fee to the specified transfer request", response = TransferFee.class)
	public ResponseEntity<?> add(@RequestBody @Valid TransferFee fee, @PathVariable String id){
		TransferRequest transferRequest = transferRequestService.findById(id);
		if(transferRequest == null){
			return ResponseEntity.notFound().build();
		}
		double requiredFee = calculateTransferFee(transferRequest.getAmount());
		fee.setAmount(roundTwo(fee.getAmount()));
		if(requiredFee != fee.getAmount()){
			return ResponseEntity.badRequest().body(String.format("The exact fee of %s is required to transfer %s", requiredFee, transferRequest.getAmount()));
		}

		fee.setTransferRequest(transferRequest);
		fee = transferFeeService.save(fee);
		transferRequestService.setFee(transferRequest, fee);

		return ResponseEntity.created(URI.create("/")).body(fee);
	}

	@RequestMapping(value = "/transfer-request/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Gets the fee required for the transfer request with the specified id", response = Double.class)
	public ResponseEntity<?> getFee(@PathVariable String id){
		TransferRequest transferRequest = transferRequestService.findById(id);
		if(transferRequest == null){
			return ResponseEntity.notFound().build();
		}
		double fee = calculateTransferFee(transferRequest.getAmount());
		return ResponseEntity.ok().body(fee);
	}

	private double calculateTransferFee(double amount){
		double val =  TRANSFER_REQUEST_FEE_PERCENTAGE * amount;
		return roundTwo(val);
	}

	private double roundTwo(double value){
		return Math.round(value * 100.0) / 100.0;
	}
}
