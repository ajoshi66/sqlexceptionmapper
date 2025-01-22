package org.joshi.sqlexceptionmapper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.joshi.sqlexceptionmapper.config.ErrorParserConfig;
import org.springframework.dao.DataAccessException;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface SqlExceptionHandler {
	
	static final String DBTYPE_H2DB = "H2DB";
	static final String DBTYPE_POSTGRESQL = "POSTGRESQL";
	static final String DBTYPE_MYSQL = "MYSQL";
	
	void handleSqlException(SqlExceptionDetail sqled);
	
	static SqlExceptionDetail getSqlExceptionDetails(DataAccessException dae) {
		return getSqlExceptionDetails(dae, null);
	}

	static SqlExceptionDetail getSqlExceptionDetails(DataAccessException dae, String dbType) {
		SqlExceptionDetail sed = null;
		if (dae.getMostSpecificCause() instanceof SQLException sqle) {
			sed = new SqlExceptionDetail();
			sed.setExceptionClassName(dae.getMostSpecificCause().getClass().getName());
			sed.setOriginalMessage(sqle.getMessage());
			sed.setSqlErrorCode(String.valueOf(sqle.getErrorCode()));
			sed.setSqlErrorState(String.valueOf(sqle.getSQLState()));
			sed.setDbType(dbType);
		}
		return sed;
	}

	static ErrorParserConfig loadConfig(String fileName) {
		ErrorParserConfig config = null;
		if (fileName != null) {
			InputStream is = ClassLoader.getSystemResourceAsStream(fileName);
			if (is != null) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					config = mapper.readValue(is, ErrorParserConfig.class);
				} catch (IOException e) {
					// Intentional suppress
					e.printStackTrace();
				}
			}
		}
		return config;
	}
}
