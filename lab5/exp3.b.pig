--load file
ipTraceLines = LOAD '/datasets/Lab5/ip_trace' USING PigStorage(' ') AS (time:chararray, id:long, srcip:chararray, bracket:chararray, destip:chararray, protocol:chararray, depdata:chararray);

--group and count
groups = GROUP firewall BY srcip;
totals = FOREACH groups GENERATE group, COUNT(firewall) AS count;

--sort and store
ordered = ORDER totals BY count DESC;
STORE ordered INTO '/users/curtisu/Lab5/exp3/output' USING PigStorage('\t');