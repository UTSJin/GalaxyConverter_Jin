package com.jin.galaxyconverter.business;

public class MetalNumeralUnit extends CurrencyUnit {
	private double value;
	
	public MetalNumeralUnit(String symbol, double value) {
		this.symbol = symbol;
		this.value = value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	public double getValue() {
		return value;
	}
}