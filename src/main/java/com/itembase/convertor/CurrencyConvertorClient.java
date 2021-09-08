package com.itembase.convertor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.itembase.convertor.models.ConversionResponse;

import reactor.core.publisher.Mono;

@Component
public class CurrencyConvertorClient {
	private final WebClient client;

	public CurrencyConvertorClient(WebClient.Builder builder) {
		this.client = builder.baseUrl("http://localhost:8080").build();
	}

	public Mono<ConversionResponse> convert() {
		return this.client.get().uri("/currency/convert").accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(ConversionResponse.class);
	}

}
