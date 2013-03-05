--load files
ipTraceLines = LOAD '/datasets/Lab5/ip_trace' USING PigStorage(' ') AS (time:chararray, id:long, srcip:chararray, bracket:chararray, destip:chararray, protocol:chararray, depdata:chararray);
rawBlockLines = LOAD '/datasets/Lab5/raw_block' USING PigStorage(' ') AS (id:long, status:chararray);

--keep only blocked ones
blocked = FILTER rawBlockLines BY status == 'Blocked';

--intersect the two
joined = JOIN ipTraceLines BY id, blocked BY id;

--generate the columns we care to keep
firewall = FOREACH joined GENERATE time, ipTraceLines::id AS id, srcip, destip, status;

--output
STORE firewall INTO '/users/curtisu/Lab5/exp3/firewall' USING PigStorage(' ');

--group and count
groups = GROUP firewall BY srcip;
totals = FOREACH groups GENERATE group, COUNT(firewall) AS count;

--sort and store
ordered = ORDER totals BY count DESC;
STORE ordered INTO '/users/curtisu/Lab5/exp3/output' USING PigStorage('\t');