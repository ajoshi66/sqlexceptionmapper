package org.joshi.sqlexceptiontranslator;

import static org.junit.jupiter.api.Assertions.*;

import org.joshi.sqlexceptionmapper.MysqlExceptionHandler;
import org.joshi.sqlexceptionmapper.SqlExceptionDetail;
import org.joshi.sqlexceptionmapper.SqlExceptionHandler;
import org.junit.jupiter.api.Test;

class MysqlExceptionHandlerTest {

	@Test
	void testHandledOutput1() {
		SqlExceptionDetail det = new SqlExceptionDetail();
		det.setDbType(SqlExceptionHandler.DBTYPE_MYSQL);
		det.setOriginalMessage("Cannot add or update a child row: a foreign key constraint fails (`vehicles`.`manufacturer`, CONSTRAINT `FK_MNFR_CITY` FOREIGN KEY (`MNFR_CITY_CODE`) REFERENCES `city` (`CITY_CODE`))");
		det.setSqlErrorCode("1452");
		det.setSqlErrorState("23000");
		det.setExceptionClassName("some.unknown.Class");
		System.out.println("testHandledOutput1::handling::" + det);
		
		SqlExceptionHandler handler = SqlExceptionHandler.getHandler(SqlExceptionHandler.DBTYPE_MYSQL);
		assertTrue(handler instanceof MysqlExceptionHandler);
		handler.handleSqlException(det);
		System.out.println("testHandledOutput1::handledOutput::" + det);
	}

	@Test
	void testHandledOutput2() {
		SqlExceptionDetail det = new SqlExceptionDetail();
		det.setDbType(SqlExceptionHandler.DBTYPE_MYSQL);
		det.setOriginalMessage("Duplicate entry 'Hyundai Automobiles' for key 'manufacturer.UK_MNFR_NAME'");
		det.setSqlErrorCode("1062");
		det.setSqlErrorState("23000");
		det.setExceptionClassName("some.unknown.Class");
		System.out.println("testHandledOutput2::handling::" + det);
		
		SqlExceptionHandler handler = SqlExceptionHandler.getHandler(SqlExceptionHandler.DBTYPE_MYSQL);
		assertTrue(handler instanceof MysqlExceptionHandler);
		handler.handleSqlException(det);
		System.out.println("testHandledOutput2::handledOutput::" + det);
	}

}
