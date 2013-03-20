CREATE EXTERNAL TABLE curtis.trace (time string, ip string, src string, bracket string, dest string, protocol string, dependent string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' LOCATION '/users/curtisu/Lab6/Exp2';


SELECT 

regexp_extract(src, dest, int index)


SELECT regexp_replace(src, '', '[^.]*$') AS a, regexp_replace(dest,'', '[^.]*$') AS b, COUNT(1) AS c FROM curtis.trace GROUP BY a, b SORT BY c DESC LIMIT 5;
SELECT src, dest, COUNT(1) AS c FROM curtis.trace GROUP BY regexp_replace(src, '', '[^.]*$'), regexp_replace(dest, '', '[^.]*$') SORT BY c DESC LIMIT 5;
SELECT regexp_replace(src, '', '[^.]*$') AS a, regexp_replace(dest, '', '[^.]*$') AS b, COUNT(1) AS c FROM curtis.trace GROUP BY regexp_replace(src, '', '[^.]*$'), regexp_replace(dest, '', '[^.]*$') SORT BY c DESC LIMIT 5;

B:
227.64.161.155.80 237.83.213.217.2440:  6335124
162.142.157.250.1027  3.150.47.225.80:  75293
3.3.120.122.80  4.20.250.115.2881:  53496
3.3.120.126.80  4.22.194.30.3998: 38247
3.3.120.126.80  4.22.194.30.3999: 36301



SELECT src, dest, COUNT(1) AS c FROM curtis.trace WHERE protocol='tcp' GROUP BY src,dest SORT BY c DESC LIMIT 10;


SELECT src, COUNT(1) AS c 
FROM (SELECT src, dest, COUNT(1) AS d FROM curtis.trace WHERE protocol='tcp' GROUP BY src, dest SORT BY d DESC) sub
GROUP BY src SORT BY c DESC LIMIT 10;

SELECT src, COUNT(DISTINCT dest) AS d
FROM curtis.trace WHERE protocol='tcp'
GROUP BY src SORT BY d DESC LIMIT 10;

hive> SELECT src, COUNT(DISTINCT dest) AS d
    > FROM curtis.trace WHERE protocol='tcp'
    > GROUP BY src SORT BY d DESC LIMIT 10;
Total MapReduce jobs = 3
Launching Job 1 out of 3
Number of reduce tasks not specified. Estimated from input data size: 3
In order to change the average load for a reducer (in bytes):
  set hive.exec.reducers.bytes.per.reducer=<number>
In order to limit the maximum number of reducers:
  set hive.exec.reducers.max=<number>
In order to set a constant number of reducers:
  set mapred.reduce.tasks=<number>
Starting Job = job_201303051520_0740, Tracking URL = http://vhost1740.site1.compute.ihost.com:50030/jobdetails.jsp?jobid=job_201303051520_0740
Kill Command = /mnt/biginsights/opt/ibm/biginsights/IHC/libexec/../bin/hadoop job  -Dmapred.job.tracker=170.224.166.210:9001 -kill job_201303051520_0740
Hadoop job information for Stage-1: number of mappers: 11; number of reducers: 3
2013-03-15 19:28:08,862 Stage-1 map = 0%,  reduce = 0%
2013-03-15 19:28:22,015 Stage-1 map = 23%,  reduce = 0%
2013-03-15 19:28:23,031 Stage-1 map = 27%,  reduce = 0%
2013-03-15 19:28:24,049 Stage-1 map = 36%,  reduce = 0%, Cumulative CPU 10.33 sec
2013-03-15 19:28:25,061 Stage-1 map = 36%,  reduce = 0%, Cumulative CPU 10.33 sec
2013-03-15 19:28:26,073 Stage-1 map = 36%,  reduce = 0%, Cumulative CPU 10.33 sec
2013-03-15 19:28:27,239 Stage-1 map = 36%,  reduce = 0%, Cumulative CPU 10.33 sec
2013-03-15 19:28:28,252 Stage-1 map = 55%,  reduce = 0%, Cumulative CPU 10.33 sec
2013-03-15 19:28:29,262 Stage-1 map = 55%,  reduce = 0%, Cumulative CPU 10.33 sec
2013-03-15 19:28:30,277 Stage-1 map = 55%,  reduce = 0%, Cumulative CPU 10.33 sec
2013-03-15 19:28:31,296 Stage-1 map = 55%,  reduce = 0%, Cumulative CPU 10.33 sec
2013-03-15 19:28:32,310 Stage-1 map = 59%,  reduce = 0%, Cumulative CPU 34.46 sec
2013-03-15 19:28:33,343 Stage-1 map = 64%,  reduce = 0%, Cumulative CPU 59.46 sec
2013-03-15 19:28:34,361 Stage-1 map = 73%,  reduce = 0%, Cumulative CPU 107.19 sec
2013-03-15 19:28:35,381 Stage-1 map = 82%,  reduce = 0%, Cumulative CPU 158.1 sec
2013-03-15 19:28:36,401 Stage-1 map = 82%,  reduce = 0%, Cumulative CPU 158.1 sec
2013-03-15 19:28:37,806 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 273.25 sec
2013-03-15 19:28:38,822 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 273.25 sec
2013-03-15 19:28:39,831 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 273.25 sec
2013-03-15 19:28:40,859 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 273.25 sec
2013-03-15 19:28:41,866 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 273.25 sec
2013-03-15 19:28:42,877 Stage-1 map = 100%,  reduce = 6%, Cumulative CPU 273.25 sec
2013-03-15 19:28:43,887 Stage-1 map = 100%,  reduce = 6%, Cumulative CPU 273.25 sec
2013-03-15 19:28:44,901 Stage-1 map = 100%,  reduce = 6%, Cumulative CPU 273.25 sec
2013-03-15 19:28:46,062 Stage-1 map = 100%,  reduce = 6%, Cumulative CPU 273.25 sec
2013-03-15 19:28:47,074 Stage-1 map = 100%,  reduce = 6%, Cumulative CPU 273.25 sec
2013-03-15 19:28:48,095 Stage-1 map = 100%,  reduce = 6%, Cumulative CPU 273.25 sec
2013-03-15 19:28:49,104 Stage-1 map = 100%,  reduce = 11%, Cumulative CPU 273.25 sec
2013-03-15 19:28:50,115 Stage-1 map = 100%,  reduce = 33%, Cumulative CPU 283.15 sec
2013-03-15 19:28:51,126 Stage-1 map = 100%,  reduce = 44%, Cumulative CPU 283.15 sec
2013-03-15 19:28:52,138 Stage-1 map = 100%,  reduce = 57%, Cumulative CPU 283.15 sec
2013-03-15 19:28:53,153 Stage-1 map = 100%,  reduce = 59%, Cumulative CPU 283.15 sec
2013-03-15 19:28:54,225 Stage-1 map = 100%,  reduce = 72%, Cumulative CPU 283.15 sec
2013-03-15 19:28:55,238 Stage-1 map = 100%,  reduce = 77%, Cumulative CPU 283.15 sec
2013-03-15 19:28:56,272 Stage-1 map = 100%,  reduce = 77%, Cumulative CPU 283.15 sec
2013-03-15 19:28:57,289 Stage-1 map = 100%,  reduce = 79%, Cumulative CPU 283.15 sec
2013-03-15 19:28:58,301 Stage-1 map = 100%,  reduce = 83%, Cumulative CPU 283.15 sec
2013-03-15 19:28:59,320 Stage-1 map = 100%,  reduce = 83%, Cumulative CPU 283.15 sec
2013-03-15 19:29:00,334 Stage-1 map = 100%,  reduce = 85%, Cumulative CPU 327.18 sec
2013-03-15 19:29:01,345 Stage-1 map = 100%,  reduce = 88%, Cumulative CPU 327.18 sec
2013-03-15 19:29:02,371 Stage-1 map = 100%,  reduce = 91%, Cumulative CPU 347.72 sec
2013-03-15 19:29:03,385 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 361.79 sec
2013-03-15 19:29:04,396 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 361.79 sec
2013-03-15 19:29:05,407 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 361.79 sec
2013-03-15 19:29:06,415 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 361.79 sec
MapReduce Total cumulative CPU time: 6 minutes 1 seconds 790 msec
Ended Job = job_201303051520_0740
Launching Job 2 out of 3
Number of reduce tasks not specified. Estimated from input data size: 1
In order to change the average load for a reducer (in bytes):
  set hive.exec.reducers.bytes.per.reducer=<number>
In order to limit the maximum number of reducers:
  set hive.exec.reducers.max=<number>
In order to set a constant number of reducers:
  set mapred.reduce.tasks=<number>
Starting Job = job_201303051520_0741, Tracking URL = http://vhost1740.site1.compute.ihost.com:50030/jobdetails.jsp?jobid=job_201303051520_0741
Kill Command = /mnt/biginsights/opt/ibm/biginsights/IHC/libexec/../bin/hadoop job  -Dmapred.job.tracker=170.224.166.210:9001 -kill job_201303051520_0741
Hadoop job information for Stage-2: number of mappers: 1; number of reducers: 1
2013-03-15 19:29:37,666 Stage-2 map = 0%,  reduce = 0%
2013-03-15 19:29:50,894 Stage-2 map = 33%,  reduce = 0%
2013-03-15 19:29:56,958 Stage-2 map = 67%,  reduce = 0%
2013-03-15 19:30:08,063 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:09,072 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:10,082 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:11,094 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:12,102 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:13,114 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:14,129 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:15,139 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:16,275 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:17,288 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:18,302 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:19,311 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:20,345 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:21,353 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:22,361 Stage-2 map = 100%,  reduce = 0%, Cumulative CPU 34.29 sec
2013-03-15 19:30:23,371 Stage-2 map = 100%,  reduce = 67%, Cumulative CPU 34.29 sec
2013-03-15 19:30:24,574 Stage-2 map = 100%,  reduce = 100%, Cumulative CPU 44.14 sec
2013-03-15 19:30:25,586 Stage-2 map = 100%,  reduce = 100%, Cumulative CPU 44.14 sec
2013-03-15 19:30:26,603 Stage-2 map = 100%,  reduce = 100%, Cumulative CPU 44.14 sec
MapReduce Total cumulative CPU time: 44 seconds 140 msec
Ended Job = job_201303051520_0741
Launching Job 3 out of 3
Number of reduce tasks determined at compile time: 1
In order to change the average load for a reducer (in bytes):
  set hive.exec.reducers.bytes.per.reducer=<number>
In order to limit the maximum number of reducers:
  set hive.exec.reducers.max=<number>
In order to set a constant number of reducers:
  set mapred.reduce.tasks=<number>
Starting Job = job_201303051520_0742, Tracking URL = http://vhost1740.site1.compute.ihost.com:50030/jobdetails.jsp?jobid=job_201303051520_0742
Kill Command = /mnt/biginsights/opt/ibm/biginsights/IHC/libexec/../bin/hadoop job  -Dmapred.job.tracker=170.224.166.210:9001 -kill job_201303051520_0742
Hadoop job information for Stage-3: number of mappers: 1; number of reducers: 1
2013-03-15 19:30:55,012 Stage-3 map = 0%,  reduce = 0%
2013-03-15 19:30:58,043 Stage-3 map = 100%,  reduce = 0%, Cumulative CPU 0.54 sec
2013-03-15 19:30:59,057 Stage-3 map = 100%,  reduce = 0%, Cumulative CPU 0.54 sec
2013-03-15 19:31:00,069 Stage-3 map = 100%,  reduce = 0%, Cumulative CPU 0.54 sec
2013-03-15 19:31:01,111 Stage-3 map = 100%,  reduce = 0%, Cumulative CPU 0.54 sec
2013-03-15 19:31:02,119 Stage-3 map = 100%,  reduce = 0%, Cumulative CPU 0.54 sec
2013-03-15 19:31:03,127 Stage-3 map = 100%,  reduce = 0%, Cumulative CPU 0.54 sec
2013-03-15 19:31:04,152 Stage-3 map = 100%,  reduce = 0%, Cumulative CPU 0.54 sec
2013-03-15 19:31:05,163 Stage-3 map = 100%,  reduce = 0%, Cumulative CPU 0.54 sec
2013-03-15 19:31:06,178 Stage-3 map = 100%,  reduce = 0%, Cumulative CPU 0.54 sec
2013-03-15 19:31:07,197 Stage-3 map = 100%,  reduce = 0%, Cumulative CPU 0.54 sec
2013-03-15 19:31:08,208 Stage-3 map = 100%,  reduce = 0%, Cumulative CPU 0.54 sec
2013-03-15 19:31:09,218 Stage-3 map = 100%,  reduce = 100%, Cumulative CPU 4.99 sec
2013-03-15 19:31:10,264 Stage-3 map = 100%,  reduce = 100%, Cumulative CPU 4.99 sec
2013-03-15 19:31:11,276 Stage-3 map = 100%,  reduce = 100%, Cumulative CPU 4.99 sec
2013-03-15 19:31:12,287 Stage-3 map = 100%,  reduce = 100%, Cumulative CPU 4.99 sec
MapReduce Total cumulative CPU time: 4 seconds 990 msec
Ended Job = job_201303051520_0742
MapReduce Jobs Launched: 
Job 0: Map: 11  Reduce: 3   Accumulative CPU: 361.79 sec   HDFS Read: 2819793944 HDFS Write: 117572356 SUCESS
Job 1: Map: 1  Reduce: 1   Accumulative CPU: 44.14 sec   HDFS Read: 117573452 HDFS Write: 472 SUCESS
Job 2: Map: 1  Reduce: 1   Accumulative CPU: 4.99 sec   HDFS Read: 934 HDFS Write: 234 SUCESS
Total MapReduce CPU Time Spent: 6 minutes 50 seconds 920 msec
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
