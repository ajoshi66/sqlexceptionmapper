-- Ensure connected as DB Owner

INSERT INTO CITY (CITY_CODE, CITY_NAME) VALUES ('BLR', 'Bangalore');
INSERT INTO CITY (CITY_CODE, CITY_NAME) VALUES ('PUN', 'Pune');
INSERT INTO CITY (CITY_CODE, CITY_NAME) VALUES ('BOM', 'Mumbai');
INSERT INTO CITY (CITY_CODE, CITY_NAME) VALUES ('DEL', 'Delhi');

INSERT INTO MANUFACTURER (MNFR_NAME, MNFR_CITY_CODE, MNFR_ADDRESS_1) VALUES ('Maruthi Suzuki', 'DEL', null);
INSERT INTO MANUFACTURER (MNFR_NAME, MNFR_CITY_CODE, MNFR_ADDRESS_1) VALUES ('Honda Motors', 'PUN', null);
