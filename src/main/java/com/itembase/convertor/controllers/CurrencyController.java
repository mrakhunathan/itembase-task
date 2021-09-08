package com.itembase.convertor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itembase.convertor.models.ConversionResponse;
import com.itembase.convertor.models.ConvertorRequest;
import com.itembase.convertor.services.CurrencyConvertorService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController(value = "currency/")
@Slf4j
public class CurrencyController {

	@Autowired
	CurrencyConvertorService currencyConvertorService;

	@PostMapping(value = "convert")
	public ResponseEntity<Flux<ConversionResponse>> convert(@RequestBody ConvertorRequest convertorRequest) {
		try {
			log.info("Converting {} {} to {}", convertorRequest.getAmount(), convertorRequest.getFrom(), convertorRequest.getTo());
			Flux<ConversionResponse> response = currencyConvertorService.convertCurrency(convertorRequest);
			return response != null ? new ResponseEntity<Flux<ConversionResponse>>(currencyConvertorService.convertCurrency(convertorRequest), HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
