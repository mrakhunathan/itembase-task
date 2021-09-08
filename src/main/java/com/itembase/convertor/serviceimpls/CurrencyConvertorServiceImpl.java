package com.itembase.convertor.serviceimpls;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itembase.convertor.models.ConversionResponse;
import com.itembase.convertor.models.ConversionRequest;
import com.itembase.convertor.models.CurrencyLayerResponse;
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

	@Value("${application.currencylayer-url}")
	String currencyLayerUrl;

	@Value("${application.currencylayer.accesskey}")
	String currencyLayerAccessKey;

	@Override
	public ResponseEntity<Flux<ConversionResponse>> convertCurrency(ConversionRequest convertorRequest) {
		try {
			ResponseEntity<Flux<ConversionResponse>> responseEntity = convertCurrencyWithCurrencyLayer(convertorRequest);
			if (responseEntity.getStatusCodeValue() == 200) {
				return responseEntity;
			}
			HttpResponse<String> response = Unirest.get(new StringBuilder().append(externalProviderUrl).append(convertorRequest.getFrom()).toString()).asString();
			if (response.getStatus() != 200) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			ResponseParser wrapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(response.getBody(), ResponseParser.class);
			return new ResponseEntity<Flux<ConversionResponse>>(
					Flux.just(new ConversionResponse(convertorRequest.getFrom(), convertorRequest.getTo(), convertorRequest.getAmount(), convertorRequest.getAmount() * wrapper.getRates().get(convertorRequest.getTo()))), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<Flux<ConversionResponse>> convertCurrencyWithCurrencyLayer(ConversionRequest convertorRequest) {
		try {
			HttpResponse<String> response = Unirest.get(new StringBuilder().append(currencyLayerUrl).append("?access_key=").append(currencyLayerAccessKey).append("&from=").append(convertorRequest.getFrom()).append("&to=")
					.append(convertorRequest.getTo()).append("&amount=").append(convertorRequest.getAmount()).toString()).asString();
			if (response.getBody().startsWith("{\"success\":false,")) {
				if (response.getBody().contains("Access Restricted")) {
					return new ResponseEntity<>(null, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
				} else {
					return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
				}
			}
			CurrencyLayerResponse wrapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(response.getBody(), CurrencyLayerResponse.class);
			return new ResponseEntity<Flux<ConversionResponse>>(Flux.just(new ConversionResponse(convertorRequest.getFrom(), convertorRequest.getTo(), convertorRequest.getAmount(), convertorRequest.getAmount() * wrapper.getResult())), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

}
