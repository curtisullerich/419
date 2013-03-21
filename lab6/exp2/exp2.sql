CREATE EXTERNAL TABLE curtis.trace (time string, ip string, src string, bracket string, dest string, protocol string, dependent string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' LOCATION '/users/curtisu/Lab6/Exp2';

--part b
SELECT regexp_replace(src, '(\.)[^\.]*$', '') AS a, regexp_replace(dest,  '(\.)[^\.]*$', '') AS b, COUNT(1) AS c FROM curtis.trace GROUP BY regexp_replace(src,  '(\.)[^\.]*$', ''), regexp_replace(dest,  '(\.)[^\.]*$', '') SORT BY c DESC LIMIT 5;
227.64.161.155  237.83.213.217  6335124
162.142.157.250 3.150.47.225  75293
3.3.120.126 4.22.194.30 74548
162.131.175.232 3.196.40.128  71051
1.37.196.123  237.81.250.217  66253




SELECT src, dest, COUNT(1) AS c FROM curtis.trace WHERE protocol='tcp' GROUP BY src,dest SORT BY c DESC LIMIT 10;


SELECT src, COUNT(1) AS c 
FROM (SELECT src, dest, COUNT(1) AS d FROM curtis.trace WHERE protocol='tcp' GROUP BY src, dest SORT BY d DESC) sub
GROUP BY src SORT BY c DESC LIMIT 10;

SELECT src, COUNT(DISTINCT dest) AS d
FROM curtis.trace WHERE protocol='tcp'
GROUP BY src SORT BY d DESC LIMIT 10;


--part c?
SELECT regexp_replace(src, '(\.)[^\.]*$', ''), COUNT(DISTINCT regexp_replace(dest, '(\.)[^\.]*$', '')) AS d
FROM curtis.trace WHERE protocol='tcp'
GROUP BY regexp_replace(src, '(\.)[^\.]*$', '') SORT BY d DESC LIMIT 10;

OK
0.3.117.37.80 72636
241.46.188.127.80 68388
241.46.218.115.80 47397
238.109.212.180.80  44278
241.46.218.114.80 43684
238.109.212.178.80  32196
0.3.117.73.80 28580
241.46.185.161.80 25074
241.46.185.227.80 17878
157.193.226.36.80 13395
Time taken: 193.833 seconds
