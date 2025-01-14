package org.joshi.sqlexceptiontranslator;

import lombok.Data;

@Data
public class SqlExceptionDetails {
	private String sqlErrorCode;
	private String originalMessage;
	private String sqlErrorMessage;
	private String exceptionClassName;
	private String dbType;
	private String sqlStatement;
	private String mappedErrorCode;
	private String defaultMessage;
	
	public SqlExceptionDetails(String sqlErrorCode, String originalMessage, String exceptionClassName) {
		this.sqlErrorCode = sqlErrorCode;
		this.originalMessage = originalMessage;
		this.exceptionClassName = exceptionClassName;
	}
}
