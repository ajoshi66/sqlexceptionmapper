package org.joshi.sqlexceptiontranslator;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

public class H2DBExceptionHandler implements SqlExceptionHandler {

	private final String H2DB_SQL_STMT = "SQL statement:";
	private final String defaultPattern = "\"";
	private final String dbType = "H2DB";

	@Override
	public SqlExceptionDetails handleSqlException(DataAccessException exc) {
		SqlExceptionDetails sed = null;
		SQLException sqle = (SQLException) exc.getMostSpecificCause();
		String exceptionClassName = exc.getMostSpecificCause().getClass().getName();
		String originalMessage = sqle.getMessage();
		String errorCode = String.valueOf(sqle.getErrorCode());
		
		if (exceptionClassName.startsWith(SqlExceptionHandler.H2DB_EXCEPTION)) {
			sed = new SqlExceptionDetails(errorCode, originalMessage, exceptionClassName);
			sed.setDbType(dbType);
			String errorMessage = originalMessage;
			int pos = originalMessage.indexOf(H2DB_SQL_STMT);
			if (pos > 0) {
				sed.setSqlStatement(originalMessage.substring(pos + H2DB_SQL_STMT.length()));
				errorMessage = originalMessage.substring(0, pos);
			}
			sed.setSqlErrorMessage(errorMessage);
			String[] tokens = errorMessage.split(defaultPattern);
			if (tokens.length > 1) {
				if (tokens[1].contains(" ")) {
					errorCode += "." + tokens[1].split(" |:")[0];
				} else {
					errorCode += "." + tokens[1];
				}
			}
			sed.setMappedErrorCode(errorCode);
		}
		
		return sed;

	}

}
