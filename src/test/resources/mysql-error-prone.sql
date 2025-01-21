-- Ensure connected as DB User

INSERT INTO CITY (CODE, NAME) VALUES ('1','2');
/**
 ERROR 1054 (42S22): Unknown column 'CODE' in 'field list'
*/


INSERT INTO CITY (CITY_NAME) VALUES ('LAS');
/*
MySQL Client: ERROR 1364 (HY000): Field 'CITY_CODE' doesn't have a default value

*/


INSERT INTO CITY (CITY_CODE) VALUES ('LAS');
/*
 ERROR 1364 (HY000): Field 'CITY_NAME' doesn't have a default value
*/


INSERT INTO CITY (CITY_CODE, CITY_NAME) VALUES ('LAS-VEGAS', 'Las Vegas');
/*
 ERROR 1406 (22001): Data too long for column 'CITY_CODE' at row 1
*/


INSERT INTO CITY (CITY_CODE, CITY_NAME) VALUES ('BOM', 'Las Vegas');
/*
 ERROR 1062 (23000): Duplicate entry 'BOM' for key 'city.PRIMARY'
*/


INSERT INTO MANUFACTURER (MNFR_ID) VALUES (101);
/**
 ERROR 1364 (HY000): Field 'MNFR_NAME' doesn't have a default value
*/


INSERT INTO MANUFACTURER (MNFR_NAME, MNFR_CITY_CODE, MNFR_ADDRESS_1) VALUES ('Maruthi Suzuki', 'DEL', null);
/**
 ERROR 1048 (23000): Column 'MNFR_ADDRESS_1' cannot be null
 
 ERROR 1452 (23000): Cannot add or update a child row: a foreign key constraint fails (`vehicles`.`manufacturer`, CONSTRAINT `FK_MNFR_CITY` FOREIGN KEY (`MNFR_CITY_CODE`) REFERENCES `city` (`CITY_CODE`))
 
 ERROR 1062 (23000): Duplicate entry 'Maruthi Suzuki' for key 'manufacturer.UK_MNFR_NAME'
*/


INSERT INTO MANUFACTURER (MNFR_NAME, MNFR_CITY_CODE, MNFR_ADDRESS_1) VALUES ('Tata Motors', 'LAS', null);
/**
*/


DELETE FROM CITY WHERE CITY_CODE = 'DEL';
/**
 ERROR 1451 (23000): Cannot delete or update a parent row: a foreign key constraint fails (`vehicles`.`manufacturer`, CONSTRAINT `FK_MNFR_CITY` FOREIGN KEY (`MNFR_CITY_CODE`) REFERENCES `city` (`CITY_CODE`))
*/


-- Errors captured from Java
Java: Column 'XXXXMNFR_ADDRESS_1' cannot be null

Cannot add or update a child row: a foreign key constraint fails (`vehicles`.`manufacturer`, CONSTRAINT `FK_MNFR_CITY` FOREIGN KEY (`MNFR_CITY_CODE`) REFERENCES `city` (`CITY_CODE`))

Duplicate entry 'Hyundai Automobiles' for key 'manufacturer.UK_MNFR_NAME'