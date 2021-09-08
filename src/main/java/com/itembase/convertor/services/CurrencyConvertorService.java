package com.itembase.convertor.services;

import com.itembase.convertor.models.ConversionResponse;
import com.itembase.convertor.models.ConvertorRequest;

import reactor.core.publisher.Flux;

public interface CurrencyConvertorService {

	Flux<ConversionResponse> convertCurrency(ConvertorRequest convertorRequest);

}
