package org.joshi.sqlexceptionmapper.config;

import java.util.List;
import java.util.Optional;

import lombok.Data;

@Data
public class ErrorParserConfig {
	private String dbType;
	private boolean useErrorCode;
	private boolean useErrorState;
	private String sqlStmtToken;
	private String detailsToken;
	private String defaultSplitRegex;
	private int defaultTokenIndex;
	private List<ErrorParserDetail> errorDetails;
	
	public ErrorParserDetail getDetail(String errorCode, String errorState) {
		ErrorParserDetail result = null;
		if (errorDetails != null && errorDetails.size() > 0) {
			ErrorParserDetail temp = new ErrorParserDetail(errorCode, errorState);
			Optional<ErrorParserDetail> optDetail = errorDetails.stream().filter(d -> temp.equals(d)).findFirst();
			if (optDetail.isPresent()) {
				result = optDetail.get();
			}
		}
		return result;
	}
}
