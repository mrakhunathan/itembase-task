package io.hatchways.convertor.services;

import org.springframework.http.ResponseEntity;

import io.hatchways.convertor.models.ConversionRequest;
import io.hatchways.convertor.models.ConversionResponse;

public interface CurrencyConvertorService {

	ResponseEntity<ConversionResponse> convertCurrency(ConversionRequest convertorRequest);

}
