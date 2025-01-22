package org.joshi.sqlexceptionmapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joshi.sqlexceptionmapper.config.ErrorParserConfig;
import org.joshi.sqlexceptionmapper.config.ErrorParserDetail;

public class GenericSqlExceptionHandler implements SqlExceptionHandler {

	private String dbType;
	private String configFileName;
	private ErrorParserConfig config;
	
	public GenericSqlExceptionHandler(String dbType, String configFileName) {
		this.dbType = dbType;
		this.configFileName = configFileName;
		this.config = SqlExceptionHandler.loadConfig(this.configFileName);
	}

	@Override
	public void handleSqlException(SqlExceptionDetail sqled) {
		String originalMessage = sqled.getOriginalMessage();
		String errorCode = sqled.getSqlErrorCode();
		String errorState = sqled.getSqlErrorState();
		String mappedErrorCode = "";
		String mappedErrorMessage = originalMessage;
		
		if (this.dbType.equals(sqled.getDbType())) {
			extractAdditionalInfo(mappedErrorMessage, sqled);
			if (sqled.getSqlErrorMessage() != null) {
				mappedErrorMessage = sqled.getSqlErrorMessage();
			}
			if (config.isUseErrorCode()) {
				mappedErrorCode += errorCode + ".";
			}
			if (config.isUseErrorState()) {
				mappedErrorCode += errorState + ".";
			}
			String defaultPattern = config.getDefaultSplitRegex();
			int defaultTokenInd = config.getDefaultTokenIndex();

			ErrorParserDetail det = config.getDetail(errorCode, errorState);
			if (det == null) {
				if (defaultPattern != null) {
					String[] tokens = mappedErrorMessage.split(defaultPattern);
					if (tokens.length > defaultTokenInd) {
						mappedErrorCode += tokens[defaultTokenInd] + ".";
					}
				}
			} else {
				if (det.getSplitterRegex() != null) {
					String[] tokens = originalMessage.split(det.getSplitterRegex());
					int[] inds = det.getTokenInds();
					if (inds == null) {
						inds = new int[] {defaultTokenInd};
					}
					for (int i : inds) {
						if (tokens.length > i) {
							mappedErrorCode += tokens[i] + ".";
						}
					}
				} else {
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
		} else {
			mappedErrorMessage = String.format("Handler for DB %s may not handle SQL Exception for %s", this.dbType,sqled.getDbType() );
			System.err.println(mappedErrorMessage);
			sqled.setSqlErrorMessage(mappedErrorMessage);
			sqled.setMappedErrorCode("DBTYPE.MISMATCH");
		}
	}
	
	private void extractAdditionalInfo(String message, SqlExceptionDetail sed) {
		int posSqlStmt = -1;
		int posDetail = -1;
		if (this.config.getSqlStmtToken() != null) {
			posSqlStmt = message.indexOf(this.config.getSqlStmtToken());
		}
		if (this.config.getDetailsToken() != null) {
			posDetail = message.indexOf(this.config.getDetailsToken());
		}
		if (posSqlStmt > 0) {
			if (posSqlStmt > posDetail) {
				sed.setSqlStatement(message.substring(posSqlStmt + this.config.getSqlStmtToken().length()));
				sed.setSqlErrorMessage(message.substring(0, (posDetail > 0? posDetail : posSqlStmt)));
			} else {
				sed.setSqlStatement(message.substring(posSqlStmt + this.config.getSqlStmtToken().length(), posDetail));
				sed.setSqlErrorMessage(message.substring(0, (posSqlStmt > 0? posSqlStmt : posDetail)));
			}
		}
	}
}
