package org.joshi.sqlexceptionmapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joshi.sqlexceptionmapper.config.ErrorParserConfig;
import org.joshi.sqlexceptionmapper.config.ErrorParserDetail;

public class MysqlExceptionHandler implements SqlExceptionHandler {

	private static final String defaultPattern = "'|\\.";
	private static final String dbType = DBTYPE_MYSQL;
	private static final String configFileName = "org/joshi/sqlexceptionmapper/error-parser-config-mysql.json";
	private static final ErrorParserConfig config = SqlExceptionHandler.loadConfig(configFileName);

	@Override
	public void handleSqlException(SqlExceptionDetail sqled) {
		String exceptionClassName = sqled.getExceptionClassName();
		String originalMessage = sqled.getOriginalMessage();
		String errorCode = sqled.getSqlErrorCode();
		String errorState = sqled.getSqlErrorState();
		String mappedErrorCode = "";
		String mappedErrorMessage = "";
		
		if (dbType.equals(sqled.getDbType())) {
			mappedErrorMessage = originalMessage;
			sqled.setSqlErrorMessage(mappedErrorMessage);
			
			ErrorParserDetail det = config.getDetail(errorCode, errorState);
			if (det == null) {
				mappedErrorCode = errorCode;
				String[] tokens = mappedErrorMessage.split(defaultPattern);
				if (tokens.length > 1) {
					mappedErrorCode += "." + tokens[1];
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
					String[] patterns = det.getPatternRegex();
					if (patterns != null && patterns.length > 0) {
						for (int i = 0; i < patterns.length; i++) {
							Pattern p = Pattern.compile(patterns[i]);
							Matcher m = p.matcher(mappedErrorMessage);
							if (m.find()) {
								mappedErrorCode += m.group() + ".";
							}
						}
					}
				}
			}
			while (mappedErrorCode.endsWith(".")) {
				mappedErrorCode = mappedErrorCode.substring(0, mappedErrorCode.length() - 1);
			}
			sqled.setMappedErrorCode(mappedErrorCode);
		}
	}
}
