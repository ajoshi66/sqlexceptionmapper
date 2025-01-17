package org.joshi.sqlexceptionmapper;

import org.joshi.sqlexceptionmapper.config.ErrorParserConfig;
import org.joshi.sqlexceptionmapper.config.ErrorParserDetail;

public class H2DBExceptionHandler implements SqlExceptionHandler {

	private static final String H2DB_SQL_STMT = "SQL statement:";
	private static final String defaultPattern = "\"";
	private static final String dbType = DBTYPE_H2DB;
	private static final String configFileName = "org/joshi/sqlexceptionmapper/error-parser-config-postgres.json";
	private static final ErrorParserConfig config = SqlExceptionHandler.loadConfig(configFileName);

	@Override
	public void handleSqlException(SqlExceptionDetail sed) {
		String exceptionClassName = sed.getExceptionClassName();
		String originalMessage = sed.getOriginalMessage();
		String errorCode = sed.getSqlErrorCode();
		String errorState = sed.getSqlErrorState();
		String mappedErrorCode = "";
		
		if (exceptionClassName.startsWith(SqlExceptionHandler.H2DB_EXCEPTION)) {
			sed.setDbType(dbType);
			String errorMessage = originalMessage;
			int pos1 = originalMessage.indexOf(H2DB_SQL_STMT);
			if (pos1 > 0) {
				sed.setSqlStatement(originalMessage.substring(pos1 + H2DB_SQL_STMT.length()));
				errorMessage = originalMessage.substring(0, pos1);
			}
			sed.setSqlErrorMessage(errorMessage);
			ErrorParserDetail det = config.getDetail(errorCode, errorState);
			if (det == null) {
				mappedErrorCode = errorCode;
				String[] tokens = errorMessage.split(defaultPattern);
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
					System.err.println("*****H2DB::Special handling with token extraction required");
				}
			}
			if (mappedErrorCode.endsWith("\\.")) {
				mappedErrorCode.substring(0, mappedErrorCode.length() - 1);
			}
			sed.setMappedErrorCode(errorCode);
		}
	}

}
