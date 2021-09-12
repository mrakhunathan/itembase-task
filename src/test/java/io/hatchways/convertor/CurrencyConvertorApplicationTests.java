package io.hatchways.convertor;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.hatchways.convertor.models.ConversionRequest;
import io.hatchways.convertor.models.ConversionResponse;
import io.hatchways.convertor.services.CurrencyConvertorService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CurrencyConvertorApplicationTests {

	@Autowired
	CurrencyConvertorService currencyConvertorService;

	@Test
	void testConvertCurrency() {
		ConversionRequest request = new ConversionRequest("USD", "INR", 23);
		ResponseEntity<ConversionResponse> response = currencyConvertorService.convertCurrency(request);
		assertEquals(response.getStatusCodeValue(), 200);
		assertEquals(response.getBody(), Boolean.TRUE);
		assertEquals(response.getBody().getAmount() > 0, Boolean.TRUE);
		assertEquals(response.getBody().getConverted() > 0, Boolean.TRUE);
		assertEquals(response.getBody().getFrom(), request.getFrom());
		assertEquals(response.getBody().getTo(), request.getTo());
	}

	@Test
	void testConvertCurrencyFail() {
		ResponseEntity<ConversionResponse> response = currencyConvertorService.convertCurrency(new ConversionRequest("USD", "INXR", 23d));
		assertEquals(response.getStatusCodeValue(), 400);
	}

	@Test
	void testConvertCurrencyFailGateway() {
		ResponseEntity<ConversionResponse> response = currencyConvertorService.convertCurrency(new ConversionRequest());
		assertEquals(response.getStatusCodeValue(), 400);
	}
}
