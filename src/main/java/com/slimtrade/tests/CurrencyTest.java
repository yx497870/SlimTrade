package main.java.com.slimtrade.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.com.slimtrade.core.utility.TradeUtility;
import main.java.com.slimtrade.enums.CurrencyType;

public class CurrencyTest {

	@Test
	public void test() {
		System.out.println(TradeUtility.getCurrencyType("test"));
		System.out.println(TradeUtility.getCurrencyType("alchesdf435435"));
		System.out.println(TradeUtility.getCurrencyType("093485alch34509dlk"));
		System.out.println(TradeUtility.getCurrencyType("chaos"));
		System.out.println(TradeUtility.getCurrencyType("ex"));
	}

}