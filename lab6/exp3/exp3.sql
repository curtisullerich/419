use curtis;

--part a
--<Time> <Connection ID> <Source IP> <Destination IP> “Blocked”
CREATE EXTERNAL TABLE curtis.iptrace  (time string, id bigint, src string, bracket string, dest string, protocol string, port string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' LOCATION '/users/curtisu/datasets/iptrace/';
--'/users/curtisu/Lab6/Exp3/ip_trace';

--<id> <blocked>
CREATE EXTERNAL TABLE curtis.rawblock (id bigint, status string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' LOCATION '/users/curtisu/datasets/rawblock';
--'/users/curtisu/Lab6/Exp3/raw_block';
  
--part b
select src, count(1) as c 
from (select * from rawblock join iptrace on (rawblock.id = iptrace.id and rawblock.status = 'Blocked')) blocks
group by src sort by c desc;

--part c
select port, count(1) as c 
from (select * from rawblock join iptrace on (rawblock.id = iptrace.id and rawblock.status = 'Blocked')) blocks
group by port sort by c desc limit 5;


