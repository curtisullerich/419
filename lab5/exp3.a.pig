--load file
ipTraceLines = LOAD '/datasets/Lab5/ip_trace' USING PigStorage(' ') AS (time:chararray, id:long, srcip:chararray, bracket:chararray, destip:chararray, protocol:chararray, depdata:chararray);
rawBlockLines = LOAD '/datasets/Lab5/raw_block' USING PigStorage(' ') AS (id:long, status:chararray);

--keep only blocked
blocked = FILTER rawBlockLines BY status == 'Blocked';
joined = JOIN ipTraceLines BY id, blocked BY id;

--output to desired format
firewall = FOREACH joined GENERATE time, ipTraceLines::id, srcip, destip, status;
STORE firewall INTO '/users/curtisu/Lab5/exp3/firewall' USING PigStorage(' ');