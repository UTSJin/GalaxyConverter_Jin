package com.jin.galaxyconverter.business;

public class RomanNumeralUnit extends CurrencyUnit {
	private int value;
	
	public RomanNumeralUnit (String symbol, int value) {
		this.symbol = symbol;
		this.value = value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}