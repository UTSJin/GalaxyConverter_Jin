package com.jin.galaxyconverter.business;

public class IntergalaticRomanUnit extends CurrencyUnit {
	private String value;
	
	public IntergalaticRomanUnit(String symbol, String value) {
		this.symbol = symbol;
		this.value = value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}

