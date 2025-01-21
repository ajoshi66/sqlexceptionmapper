package org.joshi.sqlexceptionmapper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.joshi.sqlexceptionmapper.config.ErrorParserConfig;
import org.springframework.dao.DataAccessException;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

public interface SqlExceptionHandler {
	
	static final String DBTYPE_H2DB = "H2DB";
	static final String DBTYPE_POSTGRESQL = "POSTGRESQL";
	static final String DBTYPE_MYSQL = "MYSQL";
	
	static final String H2DB_EXCEPTION = "org.h2.jdbc.";
	static final String POSTGRESQL_EXCEPTION = "org.postgresql.";
	
	static final List<Handler> registeredHandlers = init();
	
	void handleSqlException(SqlExceptionDetail sqled);
	
	static SqlExceptionHandler getHandler(String dbType) {
		SqlExceptionHandler result = null;
		Optional<Handler> optHandler = registeredHandlers.stream()
				.filter( h -> (dbType != null && dbType.equals(h.getDbType())))
				.findFirst();
		if (optHandler.isPresent()) {
			try {
				result = optHandler.get().getHandlerClass().getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				// Intentional
				e.printStackTrace();
			}
		}
		return result;
	}
	
	static SqlExceptionHandler getHandler(SqlExceptionDetail sqle) {
		SqlExceptionHandler result = null;
		String className = sqle.getExceptionClassName();
		Optional<Handler> optHandler = registeredHandlers.stream()
				.filter( h -> className.startsWith(h.getDbPackageName()))
				.findFirst();
		if (optHandler.isPresent()) {
			try {
				result = optHandler.get().getHandlerClass().getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				// Intentional
				e.printStackTrace();
			}
		}
		return result;
	}
	
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

	private static List<Handler> init() {
		List<Handler> result = new ArrayList<Handler>();
		result.add(new Handler(H2DB_EXCEPTION, DBTYPE_H2DB, H2DBExceptionHandler.class));
		result.add(new Handler(POSTGRESQL_EXCEPTION, DBTYPE_POSTGRESQL, PostgresExceptionHandler.class));
		result.add(new Handler(POSTGRESQL_EXCEPTION, DBTYPE_MYSQL, MysqlExceptionHandler.class));
		return result;
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

@Getter
class Handler {
	private String dbPackageName;
	private String dbType;
	private Class<? extends SqlExceptionHandler> handlerClass;
	
	Handler(String dbPackageName, String dbType, Class<? extends SqlExceptionHandler> handlerClass) {
		this.dbPackageName = dbPackageName;
		this.dbType = dbType;
		this.handlerClass = handlerClass;
	}
}
