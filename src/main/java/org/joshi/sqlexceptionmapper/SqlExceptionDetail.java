package org.joshi.sqlexceptionmapper;

import lombok.Data;

@Data
public class SqlExceptionDetail {
	private String sqlErrorCode;
	private String sqlErrorState;
	private String originalMessage;
	private String sqlErrorMessage;
	private String exceptionClassName;
	private String dbType;
	private String sqlStatement;
	private String mappedErrorCode;
	private String defaultMessage;
	
	public SqlExceptionDetail() {
		
	}
	
	public SqlExceptionDetail(String sqlErrorCode, String sqlErrorState, String originalMessage, String exceptionClassName) {
		this.sqlErrorCode = sqlErrorCode;
		this.originalMessage = originalMessage;
		this.exceptionClassName = exceptionClassName;
	}
}
