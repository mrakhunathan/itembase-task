package com.itembase.convertor.services;

import org.springframework.http.ResponseEntity;

import com.itembase.convertor.models.ConversionResponse;
import com.itembase.convertor.models.ConversionRequest;

import reactor.core.publisher.Flux;

public interface CurrencyConvertorService {

	ResponseEntity<Flux<ConversionResponse>> convertCurrency(ConversionRequest convertorRequest);

	ResponseEntity<Flux<ConversionResponse>> convertCurrencyWithCurrencyLayer(ConversionRequest convertorRequest);

}
