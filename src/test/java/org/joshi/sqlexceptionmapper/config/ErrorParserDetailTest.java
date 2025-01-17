package org.joshi.sqlexceptiontranslator.config;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.joshi.sqlexceptionmapper.config.ErrorParserDetail;
import org.junit.jupiter.api.Test;

class ErrorParserDetailTest {

	@Test
	void testHashCodeAndEquals() {
		ErrorParserDetail a = new ErrorParserDetail("12345", "12345");
		ErrorParserDetail b = new ErrorParserDetail("78910", "78910");
		ErrorParserDetail c = new ErrorParserDetail("12345", "12345");
		System.out.println("A:" + a.hashCode() + "::B:" + b.hashCode() + "::C:" + c.hashCode());
		System.out.println("A = B:" + (a == b) + "::A.equals(B):" + a.equals(b));
		System.out.println("A = C:" + (a == c) + "::A.equals(C):" + a.equals(c));
		System.out.println("B = C:" + (b == c) + "::B.equals(C):" + b.equals(c));
		assertFalse(a.equals(b));
		assertTrue(a.equals(c));
		assertFalse(b.equals(c));
	}

}
