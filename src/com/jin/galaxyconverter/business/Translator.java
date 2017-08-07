package com.jin.galaxyconverter.business;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Translator {
public Translator() {
		
	}
/**
 * Returns extracted list of text lines in forms of Query object which contains 
 * two variables QueryLine and Unit which can be a type of two objects.
 *  
 * @param  	Text file
 * @return Query object list
 */
	public List<Query> extractTextQuery(BufferedReader bf) {
	
		try {
			List <Query> textFileLines = new ArrayList<Query>();
			String line;
			while((line = bf.readLine()) != null) {
				String [] words = line.split(" ");
				if(line.contains("is") && (words.length==3)) { // (pish) is (X) ->IntergalaticRomanUnit (pish, x)
					IntergalaticRomanUnit unit = new IntergalaticRomanUnit(words[0], words[words.length-1]);
					textFileLines.add(new Query(line, unit));
				} else if(line.contains("is") && words[words.length-1].equals("Credits")) { 
					//(pish pish Silver) is (1000) Credits ->unrefined MetalNumeralUnit(pish pish silver, 1000)
					int isSeparatorPosition = searchIsPosition(words);
					MetalNumeralUnit newUnit = new MetalNumeralUnit(joiner(words, 0, isSeparatorPosition - 1), Integer.parseInt(words[isSeparatorPosition + 1]));
					textFileLines.add(new Query(line, newUnit));
				} else  { // How many Credits is pish pish Silver?
					textFileLines.add(new Query(line, null));
				}
			}
			return textFileLines;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	/**
	 *  Returns position of word is in text line.
	 *  
	 * @param  	Text Line
	 * @return position of word, which is "is"
	 */
	private int searchIsPosition(String [] words) {
		int position=0;
		for(int i=0; i<words.length; i++){
			if(words[i].equals("is")) 
				position = i;
		}
		return position;
	}
	/**
	 *  Combines arrays of words.
	 *  
	 * @param  	Array of text line
	 * @return Combined words
	 */
	private String joiner(String [] words, int startPoint, int endPoint) {
		String result="";
		for(int i=startPoint; i<=endPoint; i++){
			result += words[i] +" ";
		}
		return result.trim();
	}
	/**
	 *  Idenfity whether query is valid question query or not by finding keywords in text line.
	 *  
	 * @param  	text line
	 * @return True or False
	 */
	public boolean isValidQuery(String queryLine) {
		String [] words = queryLine.split(" ");
		if((words[0].equals("how") || words[0].equals("HOW"))&& Arrays.asList(words).contains("is") && words[words.length-1].equals("?")) {
			
			return true;
		} else {
			return false;			
		}
	}
	/**
	 *  Idenfity whether query is asking value of metal or not, using keyworkd "Credits".
	 *  
	 * @param  	text line
	 * @return True or False
	 */
	
	public boolean isCreditValue(String queryLine) {
		return isValidUnit(queryLine, "Credits");
	}
	public boolean isSilverValue(String queryLine) {
		return isValidUnit(queryLine, "Silver");
	}
	private boolean isValidUnit(String queryLine, String unit) {
		String [] words = queryLine.split(" ");
		if(Arrays.asList(words).contains(unit)  ) {
			return true;
		} else {
			return false;			
		}
	}
	/**
	 *  Returns only extracted Intergalatic Symbols from intergalatic and metal combines symbols, using IntergalaticRomanUnit lists.
	 *  
	 * @param  	Str text line and Intergaltic Roman Unit lists
	 * @return intergalatic Symbols
	 */
	public String extractIntergalaticSymbolValue(String queryLine, List<IntergalaticRomanUnit> intergalaticRomanList) {
		String [] words = queryLine.split(" ");
		int isSeparator = searchIsPosition(words); 
		String [] separatedQueryWords = joiner(words, isSeparator+1, words.length-2).split(" ");
		String result="";
		for(int i=0; i<separatedQueryWords.length; i++) {
			for(IntergalaticRomanUnit unit : intergalaticRomanList) {
				
				if(separatedQueryWords[i].equals(unit.getSymbol())) {
					result+=separatedQueryWords[i] +" ";
					break;
				}
			}
		}
		
		return result.trim();
	}
	/**
	 *  Returns only extracted metal symbols from text line, using Metal Numeral Unit List
	 *  
	 * @param  	Str text line and Metal Numeral List
	 * @return Metal Symbol
	 */
	public String extractMetalSymbol(String queryLine, List<MetalNumeralUnit> metalNumeralList) {
		String [] words = queryLine.split(" ");
		String metalSymbol="";
		for(MetalNumeralUnit unit:metalNumeralList) {
			if(Arrays.asList(words).contains(unit.getSymbol())) {
				metalSymbol = unit.getSymbol();
				break;
			}
		}
		return metalSymbol;
	}
	
	public String getExtractLastSymbol(String queryLine,  List<MetalNumeralUnit> metalNumeralList) {
		String symbol="as";
		return symbol;
	}
}

