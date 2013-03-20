--# hadoop fs -cp /datasets/Lab6/Exp1/Gaz_tracts_national.txt /users/curtisu/Lab6/Exp1/

--CREATE DATABASE curtis;
use curtis;
--CREATE TABLE curtis.census (state string, geoid string, pop string, hu string, aland bigint, awater bigint, alandsqmi float, awatersqmi float, latitude float, longitude float) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
--LOAD DATA INPATH '/users/curtisu/Lab6/Exp1' INTO TABLE curtis.census;

CREATE EXTERNAL TABLE curtis.census_external (state string, geoid string, pop string, hu string, aland bigint, awater float, alandsqmi float, awatersqmi bigint, latitude float, longitude float) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LOCATION '/users/curtisu/Lab6/Exp1';


SELECT * FROM census WHERE state = 'IA';
-------------------------------------------------

