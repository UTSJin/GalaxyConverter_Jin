package com.jin.galaxyconverter.console;

import java.io.*;
import com.jin.galaxyconverter.business.Converter;

public class ConsoleBoard {
	/**
	 * Run the program followed by generating Converter object to analyse text file lines, calculating
	 * asked queries in "test.txt" file and displaying the result of the queries.
	 *
	 * @param  		
	 * @return      
	 */
	public void run() {
		BufferedReader testFileReader = fileReader();
		
		if(testFileReader!=null) {
			Converter conv = new Converter();
			conv.analyseTextFileQueries(testFileReader);
			conv.calculateQuries();
			display(conv);
		} else {
			System.out.println("File is not found!!");
		}
	}
	/**
	 * Returns a type of BufferedReader that can be used for being analysed as string lines which
	 * are written in test.txt file.
	 *
	 * @param  		
	 * @return      Object of BufferedReader with "src\test.txt"
	 */
	public BufferedReader fileReader() {
		try {
			File textFile = new File("src\\test.txt");
			FileReader testFileReader = new FileReader(textFile);
			return new BufferedReader(testFileReader);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public void display(Converter conv) {
		conv.showAskedQuery();
	}
}
