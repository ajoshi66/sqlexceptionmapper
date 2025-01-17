package org.joshi.sqlexceptiontranslator;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.joshi.sqlexceptionmapper.H2DBExceptionHandler;
import org.joshi.sqlexceptionmapper.PostgresExceptionHandler;
import org.joshi.sqlexceptionmapper.SqlExceptionDetail;
import org.joshi.sqlexceptionmapper.SqlExceptionHandler;
import org.joshi.sqlexceptionmapper.config.ErrorParserConfig;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

class SqlExceptionHandlerTest {

	@Test
	void testGetConfig_good() {
		ErrorParserConfig config = SqlExceptionHandler.loadConfig("org/joshi/sqlexceptionmapper/error-parser-config-postgres.json");
		assertTrue(config != null);
		assertTrue(SqlExceptionHandler.DBTYPE_POSTGRESQL.equals(config.getDbType()));
	}

	@Test
	void testGetConfig_bad1() {
		ErrorParserConfig config = SqlExceptionHandler.loadConfig("unknown.json");
		assertTrue(config == null);
	}

	@Test
	void testGetSqlDtl_good() {
		
		DataAccessException dae = new DataIntegrityViolationException("Wrapping SQL Exception", new SQLException("Some SQL Exception", "5555", 23505));
		SqlExceptionDetail det = SqlExceptionHandler.getSqlExceptionDetails(dae);
		assertTrue(det != null);
		assertTrue(det.getExceptionClassName() != null);
		assertTrue(det.getExceptionClassName().startsWith("java.sql."));
	}

	@Test
	void testGetSqlDtl_bad1() {
		DataAccessException dae = new DataIntegrityViolationException("Wrapping SQL Exception");
		SqlExceptionDetail det = SqlExceptionHandler.getSqlExceptionDetails(dae);
		assertTrue(det == null);
	}

	@Test
	void testGetHandler_good1() {
		
		SqlExceptionDetail det = new SqlExceptionDetail();
		det.setExceptionClassName("org.h2.jdbc.SomeH2JdbcException");
		SqlExceptionHandler handler = SqlExceptionHandler.getHandler(det);
		assertTrue(handler != null);
		assertTrue(handler instanceof H2DBExceptionHandler);
	}

	@Test
	void testGetHandler_good2() {
		
		SqlExceptionDetail det = new SqlExceptionDetail();
		det.setExceptionClassName("org.postgresql.jdbc.SomePostgreSQLJdbcException");
		SqlExceptionHandler handler = SqlExceptionHandler.getHandler(det);
		assertTrue(handler != null);
		assertTrue(handler instanceof PostgresExceptionHandler);
	}

	@Test
	void testGetHandler_bad1() {
		
		SqlExceptionDetail det = new SqlExceptionDetail();
		det.setExceptionClassName("some.H2.Jdbc.Exception");
		SqlExceptionHandler handler = SqlExceptionHandler.getHandler(det);
		assertTrue(handler == null);
	}

}
