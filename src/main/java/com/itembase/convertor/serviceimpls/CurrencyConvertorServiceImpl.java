package com.itembase.convertor.serviceimpls;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itembase.convertor.models.ConversionResponse;
import com.itembase.convertor.models.ConvertorRequest;
import com.itembase.convertor.models.ResponseParser;
import com.itembase.convertor.services.CurrencyConvertorService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class CurrencyConvertorServiceImpl implements CurrencyConvertorService {

	@Value("${application.external-provider-url}")
	String externalProviderUrl;

	@Override
	public Flux<ConversionResponse> convertCurrency(ConvertorRequest convertorRequest) {
		try {
			HttpResponse<String> response = Unirest.get(new StringBuilder().append(externalProviderUrl).append(convertorRequest.getFrom()).toString()).asString();
			ResponseParser wrapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(response.getBody(), ResponseParser.class);
			return Flux.just(new ConversionResponse(convertorRequest.getFrom(), convertorRequest.getTo(), convertorRequest.getAmount(), convertorRequest.getAmount() * wrapper.getRates().get(convertorRequest.getTo())));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
