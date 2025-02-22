# Brief Context
## sqlexceptionmapper
Mapping SQL Exceptions to Business error messages.

## What is SQL Excption Mapper?
It helps to map typical SQL exceptions to proper business messages that users can understand. This variant is developed for applications using Spring 6.x framework.

## Why is it required?
Showing SQL exception messages as they are may be difficult for the end users to understand. Applications may usually treat all SQL exceptions as "Internal Server Error" that makes them appear unstable. Real-life business applications may have complex database constraints and handling all known SQL exceptions is difficult.

For example, typical H2 DB SQL exception when a column with NOT-NULL constraint may appear as follows and may not be understood by all users.

```
NULL not allowed for column "MNFR_NAME"; SQL statement:
insert into manufacturer (mnfr_address_1,mnfr_address_2,mnfr_city_code,mnfr_country_code,mnfr_name,mnfr_state_code,mnfr_id) values (?,?,?,?,?,?,default) [23502-232]
```

Of course, the messages may be complex when they are related to referential integrity constraints.

## My app has UI handling validations. Why do I need to handle SQL exceptions?
The user interface handling application validations is the ideal approach. However, UI's ability to handle such validations are limited, especially when the UI and the Services are separate components of your application. In addition, the UI may be consuming services from multiple applications and respective services own the data and validation, whether UI does it or not.

If your service layer is an independent deployable component, it cannot depend on UI and has an obligation to validate all conditions including referential integrity constraints.

## My app's Service Layer has all validations. Why do I need the SQL Exception Mapper?
Validation of referential integrity constraints by the service are hard and requires additional calls to database, which hits the application performance. Also, making code changes is inefficient when a new SQL exception is discovered every time. With the mapper, the new exception may require just configuration changes.

# Download

## Download the Binaries
Click on "Releases" and choose the version to download. Click on the "Assets" and download the Jar file. 

## Download the Source Code
Click on "Releases" and choose the version to download. Click on the "Assets" and download the Source code ZIP file. You may also download the most recent version directly from the source code page by clicking on "<> Code" and then "Download Zip". Make sure you are downloading the "main" branch.

## Critical Dependencies
The current release is compiled using Java JDK vesion 21. It also refering to `org.springframework.dao.DataAccessException` that allows easy integration with Spring Framework. Planning to get rid of this soon.

The framework also depends on Jackson Object Mapper to read JSON files.

# How to adopt?
## Adopt using Spring Boot
This section describes usage of SQL Exception Mapper for Spring Boot. The usage is test with spring-boot version 3.4.x.
### Add a config entry
Add an attribute to locate the configuration file and a bean definition. Please note that the default configuration files are available in `org/joshi/sqlexceptionmapper/` folder under `src/main/resources`. If you are using a custom configuration file, then specify the location as required in the application properties. The custom file must be available in class path.

```java
	@Value("${sqlexceptionmapper.configfile:org/joshi/sqlexceptionmapper/error-parser-config-mysql.json}")
	private String mapperConfigFile;
	
	@Bean
	public SqlExceptionHandler sqlExceptionHandler() {
		return new GenericSqlExceptionHandler(SqlExceptionHandler.DBTYPE_MYSQL, mapperConfigFile);
	}
```

### Add REST Controller Advice
In case of Spring, Spring Data, Spring JDBC, the most common exception used is `org.springframework.dao.DataAccessException` is thrown for problems related to DB operations. Hence, create a REST Controller Advice to catch this exception. You can also use `java.sql.SQLException` instead of `DataAccessException`.

```java
@Slf4j
@RestControllerAdvice
public class SqlExceptionAdvice extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private SqlExceptionHandler sqlExceptionHandler;
	
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<?> handleDataAccessException(
            final DataAccessException exception, final HttpServletRequest request) throws ManufacturerException {
		String response = "Database error::";
		String errorCode = "";
		SqlExceptionDetail sqlED = SqlExceptionHandler.getSqlExceptionDetails(exception, SqlExceptionHandler.DBTYPE_MYSQL);
		if (sqlED != null) {
			try {
				sqlExceptionHandler.handleSqlException(sqlED);
			} catch (Exception e) {
				log.error("Critical Error: Unable to handle exception.", e);
			}
			if (sqlED.getMappedErrorCode() != null) {
				errorCode += sqlED.getMappedErrorCode();
			}
			try {
				response += messageSource.getMessage(errorCode.toUpperCase(), null, request.getLocale());
			} catch (NoSuchMessageException e) {
				log.error("Critical Error: No such error message.", e);
				response += sqlED.toString();
			}
		} else {
			response += " from unknown database.";
		}
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
```

### Configure messages
Up on the occurrance of an SQL error, the `handleSqlException()` method updates with `mappedErrorCode` attribute in `SqlExceptionDetail`. Add an entry in `Messages` resource bundle with a meaningful message for that error code.

