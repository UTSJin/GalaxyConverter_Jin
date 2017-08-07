package com.jin.galaxyconverter.business;

public class Query {
	private String queryLine;
	private CurrencyUnit unit;
	
	public Query(String queryLine, CurrencyUnit unit) {
		this.queryLine = queryLine;
		this.unit = unit;
	}
	public void setQueryLine(String queryLine) {
		this.queryLine = queryLine;
	}
	public String getQueryLine() {
		return queryLine;
	}
	public CurrencyUnit getUnit() {
		return unit;
	}
}

