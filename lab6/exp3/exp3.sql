--<Time> <Connection ID> <Source IP> <Destination IP> “Blocked”
--<id> <blocked>
use curtis;
CREATE EXTERNAL TABLE curtis.iptrace  (time string, id bigint, src string, dest string, status string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' LOCATION '/users/curtisu/Lab6/Exp3/ip_trace';
CREATE EXTERNAL TABLE curtis.rawblock (id bigint, status string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' LOCATION '/users/curtisu/Lab6/Exp3/raw_block';


select src from curtis.rawblock;
  

SELECT distinct src
FROM (
  select src, count(1) as c from curtis.rawblock group by src sort by c desc
)

;



