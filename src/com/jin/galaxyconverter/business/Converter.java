package com.jin.galaxyconverter.business;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.text.DecimalFormat;
import com.jin.galaxyconverter.business.Translator;

public class Converter {
	private List<RomanNumeralUnit> romanNumeralList = new ArrayList<RomanNumeralUnit>();
	private List<IntergalaticRomanUnit> intergalaticRomanList = new ArrayList<IntergalaticRomanUnit>();
	private List<MetalNumeralUnit> metalNumeralList = new ArrayList<MetalNumeralUnit> ();
	private List<Query> askedQueries = new ArrayList<Query>();
	private Translator trans = new Translator();
	
	public Converter() {
		setRomanNumeralList();
	}
	/**
	 * Adds Romman Numeral symbols and its values in list as object type list.
	 *
	 * @param  		
	 * @return 
	 */
	private void setRomanNumeralList() {
		RomanNumeralUnit [] romanNumerals = new RomanNumeralUnit[]{
			new RomanNumeralUnit("I", 1),
			new RomanNumeralUnit("V", 5),
			new RomanNumeralUnit("X", 10),
			new RomanNumeralUnit("L", 50),
			new RomanNumeralUnit("C", 100),
			new RomanNumeralUnit("D", 500),
			new RomanNumeralUnit("M", 1000)
			};
		for(int i=0; i<romanNumerals.length; i++) {
			romanNumeralList.add(romanNumerals[i]);
		}
	}
	/**
	 * Analyse and categorise queries depend on charicteristics of the queries.  
	 * The queries, which means lines in text file, may include Intergalatic Roman value("pish" , "X"), 
	 * Metal Roman Value ("Silver", "1000" ) or Quesetion query ("How much or many Credits ..... ?").
	 * With the queries, this method will add in each list to store the values.
	 *
	 * @param  	Text file lines which contain information about intergalatic/metal symbols and its values	
	 * @return 
	 */
	public void analyseTextFileQueries(BufferedReader bf) {
		List<Query> textFileQueries = trans.extractTextQuery(bf);
		for(Query query : textFileQueries){
			if(query.getUnit() instanceof IntergalaticRomanUnit) {
				
				intergalaticRomanList.add(((IntergalaticRomanUnit) query.getUnit())); //("pish", "X")
			
			} else if(query.getUnit() instanceof MetalNumeralUnit) {
				
				/**
				 *  MetalNumeralUnit -> refinedMetalUnitType
				 * (pish pish Silver, "11111") ->(Silver, "11111/pish pish")
				 */
				MetalNumeralUnit refinedMetalUnit = getRefinedMetalNumeralUnit((MetalNumeralUnit) query.getUnit());
				metalNumeralList.add(refinedMetalUnit);
			
			} else {
				askedQueries.add(query);
			}
		} 
	} 
	/**
	 * Refines unrefined Metal Numeral Unit, which looks like ("pish pish Silver", "5600), 
	 * to refined Metal Numeral Unit ("Silver", "5600/(pish pish)")
	 * @param  	Unrefined Metal Numeral Unit
	 * @return  Refined Metal Numeral Unit
	 */
	private MetalNumeralUnit getRefinedMetalNumeralUnit(MetalNumeralUnit queryUnit) {
		String [] querySymbols = queryUnit.getSymbol().split(" ");
		String metalName = querySymbols[querySymbols.length-1];
		String romanSymbols="";
		
		for(int i=0; i<querySymbols.length-1; i++) {
			romanSymbols += intergalaticSymbolToRomanSymbol(querySymbols[i]);
		}
		
		return new MetalNumeralUnit(metalName, queryUnit.getValue()/romanSymbolsToNumber(romanSymbols));
	}
	/**
	 * Calculates Questions queries, which may asks the value of intergalatic units, metal values or intergalatic metal values.
	 * While calculating the queries, in the case of queries which translator cannot analyse will be recognised as invalid query.
	 *
	 * @param  	
	 * @return
	 */
	public void calculateQuries() {
		for(Query query: askedQueries) {
			if(trans.isValidQuery(query.getQueryLine())) {
				
				String intergalaticSymbol = trans.extractIntergalaticSymbolValue(query.getQueryLine(), intergalaticRomanList);
				
				if(trans.isCreditValue(query.getQueryLine())) {
				
						String metalSymbol = trans.extractMetalSymbol(query.getQueryLine(), metalNumeralList);
						Double metalValue = romanSymbolsToNumber(intergalaticSymbolToRomanSymbol(intergalaticSymbol))*getMetalSymbolValue(metalSymbol);
						DecimalFormat df = new DecimalFormat("#.##");
						query.setQueryLine(intergalaticSymbol +" "+ metalSymbol + " is " + df.format(metalValue)+" Credits");
			   } else if(trans.isSilverValue (query.getQueryLine())) {
				    String metalSymbol = trans.extractMetalSymbol(query.getQueryLine(), metalNumeralList);
					Double metalValue = romanSymbolsToNumber(intergalaticSymbolToRomanSymbol(intergalaticSymbol))*getMetalSymbolValue(metalSymbol);
					metalValue = metalValue/getMetalSymbolValue("Silver");
					DecimalFormat df = new DecimalFormat("#.##");
					query.setQueryLine(intergalaticSymbol +" "+ metalSymbol + " is " + df.format(metalValue)+" Silver");
			   }
				else {
					
					query.setQueryLine(intergalaticSymbol + " is " + romanSymbolsToNumber(intergalaticSymbolToRomanSymbol(intergalaticSymbol)));
				}
				
			} else {
				query.setQueryLine("I have no idea what you are talking about");
			}
		}
		
	}
	/**
	 * Returns Roman Symbols from intergalatic symbols
	 *
	 * @param  	Intergalatic Symbols (pish pish)
	 * @return Roman Symbol (II)
	 */
	public String intergalaticSymbolToRomanSymbol(String intergalaticSymbols) {
		String [] intergalaticSymbolsArray = intergalaticSymbols.split(" ");
		String romanSymbols= "";
		for(int i = 0; i<intergalaticSymbolsArray.length; i++) {
			for(IntergalaticRomanUnit unit : intergalaticRomanList) {
				if(unit.getSymbol().equals(intergalaticSymbolsArray[i])) {
					romanSymbols += unit.getValue();
					break;
				}
			}
		}
		return romanSymbols;
	}
	/**
	 *  Retuns numeral values from Roman characters
	 *  
	 * @param  	Roman Symbols(II)
	 * @return Numeral Value (2)
	 */
	public int romanSymbolsToNumber(String romanSymbol) {
		int value=0;
		
		for(int i=0; i<romanSymbol.length(); i++) {
			
			int followedByCharValue;
			int charValue = getRomanSymbolValue(romanSymbol.charAt(i));
			
			if(i+1>=romanSymbol.length()) {
				value+=charValue;
			} else {
				followedByCharValue = getRomanSymbolValue(romanSymbol.charAt(i+1));
				
				if(charValue < followedByCharValue){
					value = value + (followedByCharValue - charValue);
					i++;
				} else {
					value += charValue;
					}
		
			}
		}
		return value;
		
	}
	/**
	 *  Returns Numeral value from Metal Symbol
	 *  
	 * @param  	Metal Symbol (Gold)
	 * @return Numeral Value (10000)
	 */
	public double getMetalSymbolValue(String metalSymbol) {
		double value=0;
		for(MetalNumeralUnit unit : metalNumeralList) {
			if(unit.getSymbol().equals(metalSymbol)) {
				value = unit.getValue();
			}
		}
		return value;
	}
	/**
	 *  Returns numeral value from Roman Character
	 *  
	 * @param  	Roman Character (I,X,L,V...)
	 * @return Numeral Value (1, 5, 10 ...)
	 */
	public int getRomanSymbolValue(char ch) {
		int value=0;
		for(RomanNumeralUnit unit:romanNumeralList) {
			if((unit.getSymbol().charAt(0) == ch)) {
				value = unit.getValue();
				break;
			}
		}
		return value;
	}
	public void showAskedQuery() {
		for(Query query : askedQueries) {
			System.out.println(query.getQueryLine());
		}
	} 
}
