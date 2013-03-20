SELECT regexp_replace(src, '(\.)[^\.]*$', '') a, regexp_replace(dest, '(\.)[^\.]*$', '') b, COUNT(1) AS c FROM curtis.trace GROUP BY regexp_replace(src, '(\.)[^\.]*$', ''), regexp_replace(dest, '(\.)[^\.]*$', '') SORT BY c DESC LIMIT 5;
SELECT regexp_replace(src, '(\.)[^\.]*$', '') a, regexp_replace(dest, '(\.)[^\.]*$', '') b, COUNT(1) AS c FROM curtis.trace WHERE protocol='tcp' GROUP BY regexp_replace(src, '(\.)[^\.]*$', ''), regexp_replace(dest, '(\.)[^\.]*$', '') SORT BY c DESC LIMIT 10;

