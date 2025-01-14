package org.joshi.sqlexceptiontranslator;

import org.springframework.dao.DataAccessException;

public interface SqlExceptionHandler {
	
	final String H2DB_EXCEPTION = "org.h2.jdbc.";
	
	SqlExceptionDetails handleSqlException(DataAccessException sed);
	
	static SqlExceptionHandler getHandler(DataAccessException sqle) {
		SqlExceptionHandler result = null;
		String className = sqle.getMostSpecificCause().getClass().getName();
		if (className.startsWith(H2DB_EXCEPTION)) {
			result = new H2DBExceptionHandler();
		}
		return result;
	}

}
