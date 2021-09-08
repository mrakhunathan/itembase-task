package com.itembase.convertor.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyLayerResponse {

	@JsonProperty("result")
	double result;

	@JsonProperty("success")
	boolean success;
}
