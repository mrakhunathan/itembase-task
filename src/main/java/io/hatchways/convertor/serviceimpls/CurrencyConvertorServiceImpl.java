package io.hatchways.convertor.serviceimpls;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import io.hatchways.convertor.models.ConversionRequest;
import io.hatchways.convertor.models.ConversionResponse;
import io.hatchways.convertor.models.ResponseParser;
import io.hatchways.convertor.services.CurrencyConvertorService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CurrencyConvertorServiceImpl implements CurrencyConvertorService {

	@Value("${application.external-provider-url}")
	String externalProviderUrl;

	@Override
	public ResponseEntity<ConversionResponse> convertCurrency(ConversionRequest convertorRequest) {
		try {
			HttpResponse<String> response = Unirest.get(new StringBuilder().append(externalProviderUrl).append(convertorRequest.getFrom()).toString()).asString();
			if (response.getStatus() != 200) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			ResponseParser wrapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(response.getBody(), ResponseParser.class);
			return new ResponseEntity<ConversionResponse>(new ConversionResponse(convertorRequest.getFrom(), convertorRequest.getTo(), convertorRequest.getAmount(), convertorRequest.getAmount() * wrapper.getRates().get(convertorRequest.getTo())),
					HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

}
