# sqlexceptionmapper
Mapping SQL Exceptions to Business error messages.

# What is SQL Excption Mapper?
It helps to map typical SQL exceptions to proper business messages that users can understand. This variant is developed for applications using Spring 6.x framework.

# Why is it required?
Showing SQL exception messages as they are may be difficult for the end users to understand. Applications may usually treat all SQL exceptions as "Internal Server Error" that makes them appear unstable. Real-life business applications may have complex database constraints and handling all known SQL exceptions is difficult.

For example, typical H2 DB SQL exception when a column with NOT-NULL constraint may appear as follows and may not be understood by all users.

<code>
NULL not allowed for column "MNFR_NAME"; SQL statement:
insert into manufacturer (mnfr_address_1,mnfr_address_2,mnfr_city_code,mnfr_country_code,mnfr_name,mnfr_state_code,mnfr_id) values (?,?,?,?,?,?,default) [23502-232]
</code>

Of course, the messages may be complex when they are related to referential integrity constraints.

# My app has UI handling validations. Why do I need to handle SQL exceptions?
The user interface handling application validations is the ideal approach. However, UI's ability to handle such validations are limited, especially when the UI and the Services are separate components of your application. In addition, the UI may be consuming services from multiple applications and respective services own the data and validation, whether UI does it or not.

If your service layer is an independent deployable component, it cannot depend on UI and has an obligation to validate all conditions including referential integrity constraints.

# My app's  has all validations. Why do I need the SQL Exception Mapper?
Validation of referential integrity constraints by the service are hard and requires additional calls to database, which hits the application performance. Also, making code changes is inefficient when a new SQL exception is discovered every time. With the mapper, the new exception may require just configuration changes.
