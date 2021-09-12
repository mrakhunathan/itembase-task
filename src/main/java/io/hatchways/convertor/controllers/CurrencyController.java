package io.hatchways.convertor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.hatchways.convertor.models.ConversionRequest;
import io.hatchways.convertor.models.ConversionResponse;
import io.hatchways.convertor.services.CurrencyConvertorService;
import lombok.extern.slf4j.Slf4j;

@RestController(value = "currency/")
@Slf4j
public class CurrencyController {

	@Autowired
	CurrencyConvertorService currencyConvertorService;

	@PostMapping(value = "convert")
	public ResponseEntity<ConversionResponse> convert(@RequestBody ConversionRequest convertorRequest) {
		try {
			log.info("Converting {} {} to {}", convertorRequest.getAmount(), convertorRequest.getFrom(), convertorRequest.getTo());
			return currencyConvertorService.convertCurrency(convertorRequest);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
