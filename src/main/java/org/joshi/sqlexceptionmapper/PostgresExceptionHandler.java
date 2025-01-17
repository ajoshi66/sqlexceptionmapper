package org.joshi.sqlexceptionmapper;

import org.joshi.sqlexceptionmapper.config.ErrorParserConfig;
import org.joshi.sqlexceptionmapper.config.ErrorParserDetail;

public class PostgresExceptionHandler implements SqlExceptionHandler {

	private static final String POSTGRES_SQL_STMT = "SQL statement:";
	private static final String POSTGRES_DETAIL = "Detail:";
	private static final String defaultPattern = "\"";
	private static final String dbType = DBTYPE_POSTGRESQL;
	private static final String configFileName = "org/joshi/sqlexceptionmapper/error-parser-config-postgres.json";
	private static final ErrorParserConfig config = SqlExceptionHandler.loadConfig(configFileName);

	@Override
	public void handleSqlException(SqlExceptionDetail sqled) {
		String exceptionClassName = sqled.getExceptionClassName();
		String originalMessage = sqled.getOriginalMessage();
		String errorCode = sqled.getSqlErrorCode();
		String errorState = sqled.getSqlErrorState();
		String mappedErrorCode = "";
		String mappedErrorMessage = "";
		
		if (exceptionClassName.startsWith(SqlExceptionHandler.POSTGRESQL_EXCEPTION)) {
			mappedErrorMessage = originalMessage;
			int pos1 = originalMessage.indexOf(POSTGRES_SQL_STMT);
			if (pos1 > 0) {
				sqled.setSqlStatement(originalMessage.substring(pos1 + POSTGRES_SQL_STMT.length()));
				mappedErrorMessage = originalMessage.substring(0, pos1);
			}
			
			int pos2 = mappedErrorMessage.indexOf(POSTGRES_DETAIL);
			if (pos2 > 0) {
				//sqled.setSqlStatement(originalMessage.substring(pos1 + POSTGRES_SQL_STMT.length()));
				mappedErrorMessage = mappedErrorMessage.substring(0, pos2);
			}
			sqled.setSqlErrorMessage(mappedErrorMessage);
			sqled.setDbType(dbType);
			
			ErrorParserDetail det = config.getDetail(errorCode, errorState);
			if (det == null) {
				mappedErrorCode = errorState;
				String[] tokens = mappedErrorMessage.split(defaultPattern);
				if (tokens.length > 1) {
					if (tokens[1].contains(" ")) {
						mappedErrorCode += "." + tokens[1].split(" |:")[0];
					} else {
						mappedErrorCode += "." + tokens[1];
					}
				}
			} else {
				if (config.isUseErrorCode()) {
					mappedErrorCode += errorCode + ".";
				}
				if (config.isUseErrorState()) {
					mappedErrorCode += errorState + ".";
				}
				if (det.getSplitterRegex() != null) {
					String[] tokens = originalMessage.split(det.getSplitterRegex());
					int[] inds = det.getTokenInds();
					if (inds == null) {
						inds = new int[] {1};
					}
					for (int i : inds) {
						if (tokens.length > i) {
							if (det.isRemoveSplChars()) {
								mappedErrorCode += tokens[i].replaceAll("[^\\w]", "") + ".";
							} else {
								mappedErrorCode += tokens[i] + ".";
							}
						}
					}
				} else {
					//Special handling with token extraction required
					System.err.println("*****POSTGRES::Special handling with token extraction required");
				}
			}
			while (mappedErrorCode.endsWith(".")) {
				mappedErrorCode = mappedErrorCode.substring(0, mappedErrorCode.length() - 1);
			}
			sqled.setMappedErrorCode(mappedErrorCode);
		}
	}
}
