package com.itembase.convertor;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.itembase.convertor.models.ConversionRequest;
import com.itembase.convertor.models.ConversionResponse;
import com.itembase.convertor.services.CurrencyConvertorService;

import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CurrencyConvertorApplicationTests {

	@Autowired
	CurrencyConvertorService currencyConvertorService;

	@Test
	void testConvertCurrency() {

		ConversionRequest request = new ConversionRequest("USD", "INR", 23);
		ResponseEntity<Flux<ConversionResponse>> response = currencyConvertorService.convertCurrency(request);
		assertEquals(response.getStatusCodeValue(), 200);
		assertEquals(response.getBody().collectList().hasElement().block(), Boolean.TRUE);
		assertEquals(response.getBody().blockFirst().getAmount() > 0, Boolean.TRUE);
		assertEquals(response.getBody().blockFirst().getConverted() > 0, Boolean.TRUE);
		assertEquals(response.getBody().blockFirst().getFrom(), request.getFrom());
		assertEquals(response.getBody().blockFirst().getTo(), request.getTo());
	}

	@Test
	void testConvertCurrencyFail() {
		ResponseEntity<Flux<ConversionResponse>> response = currencyConvertorService.convertCurrency(new ConversionRequest("USD", "INXR", 23d));
		assertEquals(response.getStatusCodeValue(), 400);
	}

	@Test
	void testConvertCurrencyFailGateway() {
		ResponseEntity<Flux<ConversionResponse>> response = currencyConvertorService.convertCurrency(new ConversionRequest());
		assertEquals(response.getStatusCodeValue(), 400);
	}

}
