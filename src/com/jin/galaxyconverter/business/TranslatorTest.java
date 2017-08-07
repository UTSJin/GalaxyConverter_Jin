package com.jin.galaxyconverter.business;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TranslatorTest {

	@Test
	public void testIsValidQuery() {
		Translator trans = new Translator ();
		assertEquals(true, trans.isValidQuery("how many Credits is glob ?"));
	
	}

	@Test
	public void testIsValidQuery2() {
		Translator trans = new Translator ();
		assertEquals(true, trans.isValidQuery("HOW much Credits is glob ?"));
	
	}
	@Test
	public void testIsValidQuery3() {
		Translator trans = new Translator ();
		assertEquals(true, trans.isSilverValue("how many Silver is glob Gold ?"));
	
	}
	
	@Test
	public void testIsValidQuery4() {
		Translator trans = new Translator ();
		assertEquals(false, trans.isSilverValue("how many Credits is glob ?"));
	
	}
	@Test
	public void testIsValidQuery5() {
		Translator trans = new Translator ();
		List<MetalNumeralUnit> metalNumeralList = new ArrayList<MetalNumeralUnit> ();
		metalNumeralList.add(new MetalNumeralUnit("Gold", 10));
		metalNumeralList.add(new MetalNumeralUnit("Silver", 15));
		assertEquals("Gold", trans.getExtractLastSymbol("how many Silver is glob Gold ?", metalNumeralList));
	
	}

}
