package org.joshi.sqlexceptionmapper.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"errorCode", "errorState"})
public class ErrorParserDetail {
	private String errorCode;
	private String errorState;
	private String description;
	private String sampleError;
	private String splitterRegex;
	private int[] tokenInds;
	private String[] patternRegex;
	
	@Deprecated
	private boolean removeSplChars;

	public ErrorParserDetail() {
		
	}
	
	public ErrorParserDetail(String errorCode, String errorState) {
		this.errorCode = errorCode;
		this.errorState = errorState;
	}
}
