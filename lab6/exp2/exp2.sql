CREATE EXTERNAL TABLE curtis.trace (time string, ip string, src string, bracket string, dest string, protocol string, dependent string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' LOCATION '/datasets/Lab6/Exp2';
--/users/curtisu/datasets

SELECT 
              
regexp_extract(src, dest, int index)


SELECT regexp_replace(src, '', '[^.]*$') AS a, regexp_replace(dest,'', '[^.]*$') AS b, COUNT(1) AS c FROM curtis.trace GROUP BY a, b SORT BY c DESC LIMIT 5;
SELECT src, dest, COUNT(1) AS c FROM curtis.trace GROUP BY regexp_replace(src, '', '[^.]*$'), regexp_replace(dest, '', '[^.]*$') SORT BY c DESC LIMIT 5;


--my original results for part b
B:
SELECT src, dest, COUNT(1) AS c FROM curtis.trace GROUP BY src, dest SORT BY c DESC LIMIT 5;
227.64.161.155.80 237.83.213.217.2440:  6335124
162.142.157.250.1027  3.150.47.225.80:  75293
3.3.120.122.80  4.20.250.115.2881:  53496
3.3.120.126.80  4.22.194.30.3998: 38247
3.3.120.126.80  4.22.194.30.3999: 36301


--USE THIS ONE FOR PART B
SELECT regexp_replace(src, '(\.)[^\.]*$', '') a, regexp_replace(dest, '(\.)[^\.]*$', '') b, COUNT(1) AS c FROM curtis.trace GROUP BY regexp_replace(src, '(\.)[^\.]*$', ''), regexp_replace(dest, '(\.)[^\.]*$', '') SORT BY c DESC LIMIT 5;

--USE THIS ONE FOR PART C
SELECT regexp_replace(src, '(\.)[^\.]*$', '') a, regexp_replace(dest, '(\.)[^\.]*$', '') b, COUNT(1) AS c FROM curtis.trace WHERE protocol='tcp' GROUP BY regexp_replace(src, '(\.)[^\.]*$', ''), regexp_replace(dest, '(\.)[^\.]*$', '') SORT BY c DESC LIMIT 10;

--something
SELECT src, COUNT(1) AS c 
FROM (SELECT src, dest, COUNT(1) AS d FROM curtis.trace WHERE protocol='tcp' GROUP BY src, dest SORT BY d DESC) sub
GROUP BY src SORT BY c DESC LIMIT 10;

--my old attempt at part c
SELECT src, COUNT(DISTINCT dest) AS d
FROM curtis.trace WHERE protocol='tcp'
GROUP BY src SORT BY d DESC LIMIT 10;

--idk what this is
SELECT regexp_replace(src, '[^.]*$', '') a, COUNT(DISTINCT regexp_replace(dest, '[^.]*$', '') b) AS d
FROM curtis.trace WHERE protocol='tcp'
GROUP BY regexp_replace(src, '[^.]*$', '') SORT BY d DESC LIMIT 10;



