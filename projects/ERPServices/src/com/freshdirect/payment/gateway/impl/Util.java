package com.freshdirect.payment.gateway.impl;

public class Util {
	static void replace(String input) {
		 System.out.println(input.replace("\t\t", "\n"));
	}
	static void printResponse(String input) {
		 System.out.println(input.replace("><", ">\n<"));
	}
}
