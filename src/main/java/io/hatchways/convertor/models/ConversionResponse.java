package io.hatchways.convertor.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResponse {
	String from;

	String to;

	double amount;

	double converted;
}
