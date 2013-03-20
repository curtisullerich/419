CREATE EXTERNAL TABLE curtis.iptrace  (src string, bracket string, dest string, protocol string, port string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' LOCATION '/users/curtisu/Lab6/Exp3/ip_trace';
CREATE EXTERNAL TABLE curtis.rawblock (time string, id string, src string, bracket string, dest string, protocol string, dependent string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' LOCATION '/users/curtisu/Lab6/Exp3/raw_block';

select distinct src, count(src) as c from curtis.rawblock group by src sort by c desc;



