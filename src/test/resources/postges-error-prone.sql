-- Ensure connected as DB User

INSERT INTO CITY (CODE, NAME) VALUES ('1','2');
/**
ERROR:  column "code" of relation "city" does not exist
SQL state: 42703
*/


INSERT INTO CITY (CITY_NAME) VALUES ('LAS');
/**
ERROR:  Failing row contains (null, LAS).null value in column "city_code" of relation "city" violates not-null constraint 

ERROR:  null value in column "city_code" of relation "city" violates not-null constraint
SQL state: 23502
Detail: Failing row contains (null, LAS).
*/


INSERT INTO CITY (CITY_CODE) VALUES ('LAS');
/**
ERROR:  Failing row contains (LAS, null).null value in column "city_name" of relation "city" violates not-null constraint 

ERROR:  null value in column "city_name" of relation "city" violates not-null constraint
SQL state: 23502
Detail: Failing row contains (LAS, null).
*/


INSERT INTO CITY (CITY_CODE, CITY_NAME) VALUES ('LAS-VEGAS', 'Las Vegas');
/**
ERROR:  value too long for type character varying(5) 

SQL state: 22001
*/


INSERT INTO CITY (CITY_CODE, CITY_NAME) VALUES ('BOM', 'Las Vegas');
/**
ERROR:  Key (city_code)=(BOM) already exists.duplicate key value violates unique constraint "city_pkey" 

ERROR:  duplicate key value violates unique constraint "city_pkey"
SQL state: 23505
Detail: Key (city_code)=(BOM) already exists.
*/


INSERT INTO MANUFACTURER (MNFR_ID) VALUES (101);
/**
ERROR:  Column "mnfr_id" is an identity column defined as GENERATED ALWAYS.cannot insert a non-DEFAULT value into column "mnfr_id" 

ERROR:  cannot insert a non-DEFAULT value into column "mnfr_id"
SQL state: 428C9
Detail: Column "mnfr_id" is an identity column defined as GENERATED ALWAYS.
Hint: Use OVERRIDING SYSTEM VALUE to override.
*/


INSERT INTO MANUFACTURER (MNFR_NAME, MNFR_CITY_CODE, MNFR_ADDRESS_1) VALUES ('Maruthi Suzuki', 'DEL', null);
/**
ERROR:  Key (mnfr_name)=(Maruthi Suzuki) already exists.duplicate key value violates unique constraint "uk_mnfr_name" 

ERROR:  duplicate key value violates unique constraint "uk_mnfr_name"
SQL state: 23505
Detail: Key (mnfr_name)=(Maruthi Suzuki) already exists.
*/


INSERT INTO MANUFACTURER (MNFR_NAME, MNFR_CITY_CODE, MNFR_ADDRESS_1) VALUES ('Tata Motors', 'LAS', null);
/**
ERROR:  Key (mnfr_city_code)=(LAS) is not present in table "city".insert or update on table "manufacturer" violates foreign key constraint "fk_mnfr_city" 

ERROR:  insert or update on table "manufacturer" violates foreign key constraint "fk_mnfr_city"
SQL state: 23503
Detail: Key (mnfr_city_code)=(LAS) is not present in table "city".
*/


DELETE FROM CITY WHERE CITY_CODE = 'DEL';
/**
ERROR:  Key (city_code)=(DEL) is still referenced from table "manufacturer".update or delete on table "city" violates foreign key constraint "fk_mnfr_city" on table "manufacturer" 

ERROR:  update or delete on table "city" violates foreign key constraint "fk_mnfr_city" on table "manufacturer"
SQL state: 23503
Detail: Key (city_code)=(DEL) is still referenced from table "manufacturer".
*/


-- Errors captured from Java
/*
ERROR: null value in column "mnfr_name" of relation "manufacturer" violates not-null constraint
  Detail: Failing row contains (3, null, null, null, null, null, null).
*/

/*
ERROR: null value in column "mnfr_address_1" of relation "manufacturer" violates not-null constraint
  Detail: Failing row contains (3, Hyundai Automobiles, null, null, null, null, null).
*/

/*
ERROR: null value in column "mnfr_city_code" of relation "manufacturer" violates not-null constraint
  Detail: Failing row contains (4, Hyundai Automobiles, Sriperumbudur, null, null, null, null).
*/

/*
ERROR: null value in column "mnfr_state_code" of relation "manufacturer" violates not-null constraint
  Detail: Failing row contains (5, Hyundai Automobiles, Sriperumbudur, null, MAS, null, null).
*/

/*
ERROR: null value in column "mnfr_country_code" of relation "manufacturer" violates not-null constraint
  Detail: Failing row contains (6, Hyundai Automobiles, Sriperumbudur, null, MAS, TN, null).
*/

/*
ERROR: insert or update on table "manufacturer" violates foreign key constraint "fk_mnfr_city"
  Detail: Key (mnfr_city_code)=(MAS) is not present in table "city".
*/