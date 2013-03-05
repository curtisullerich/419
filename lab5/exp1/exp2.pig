--load the file
lines = LOAD '/datasets/Lab5/network_trace' USING PigStorage(' ') AS (time:chararray, IP:chararray, srcip:chararray, bracket:chararray, destip:chararray, protocol:chararray, depdata:chararray);

--keep only tcp
filteredlines = FILTER lines BY protocol == 'tcp';

--split off the last part of the IPs
pairs = FOREACH filteredlines GENERATE SUBSTRING(srcip, 0, LAST_INDEX_OF(srcip, '.')) AS srcip, SUBSTRING(destip, 0, LAST_INDEX_OF(destip, '.')) AS destip;

--remove duplicates
sources = DISTINCT pairs;

--group by src ip and count dest ips
sources2 = GROUP sources BY srcip;
totals = FOREACH sources2 GENERATE group, COUNT(sources) AS count;

--sort, limit, and print
ordered = ORDER totals BY count DESC;
top = LIMIT ordered 10;
STORE top INTO '/users/curtisu/Lab5/exp2/output6' USING PigStorage('\t');