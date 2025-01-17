package org.joshi.sqlexceptiontranslator.config;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.joshi.sqlexceptionmapper.config.ErrorParserConfig;
import org.joshi.sqlexceptionmapper.config.ErrorParserDetail;
import org.junit.jupiter.api.Test;

class ErrorParserConfigTest {

	@Test
	void testGetDetail() {
		ErrorParserDetail a = new ErrorParserDetail("12345", "12345");
		ErrorParserDetail b = new ErrorParserDetail("78910", "78910");
		ErrorParserDetail c = new ErrorParserDetail("12345", "00000");
		List<ErrorParserDetail> dets = List.of(a, b, c);
		ErrorParserConfig conf = new ErrorParserConfig();
		conf.setErrorDetails(dets);
		
		assertTrue(conf.getDetail("12345", "12345") != null);
		assertTrue(conf.getDetail("12345", "12345").equals(a));
		
		assertTrue(conf.getDetail("78910", "78910") != null);
		assertTrue(conf.getDetail("78910", "78910").equals(b));

		assertTrue(conf.getDetail("12345", "00000") != null);
		assertTrue(conf.getDetail("12345", "00000").equals(c));
		
		assertTrue(conf.getDetail("78910", "00000") == null);
		assertTrue(conf.getDetail("12345", "78910") == null);
		
	}

}
