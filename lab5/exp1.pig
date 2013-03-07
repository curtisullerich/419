--load file
records = LOAD 'input.txt' USING PigStorage('\t') AS (usps:chararray, geoid:chararray, pop10:int, hu10:int, aland:long, awater:int, alandsqmi:int, awatersqmi:int, intptlat:int, intptlong:int);

--combine based on state and sum
states = GROUP records BY usps;
totals = FOREACH states GENERATE group, SUM(records.aland) AS sum;

--order, split, and output
ordered = ORDER totals BY sum DESC;
top = LIMIT ordered 2;
STORE top INTO 'outputexp72' USING PigStorage('\t');