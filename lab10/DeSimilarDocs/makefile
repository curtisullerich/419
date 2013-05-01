all: java spl 

java:
	#spl-make-operator --kind java
	#mv DeSimilarDocs.xml my.op/DeSimilarDocs/DeSimilarDocs.xml
	javac -cp /opt/ibm/InfoSphereStreams/lib/com.ibm.streams.operator.jar:/opt/ibm/InfoSphereStreams/ext/lib/commons-math-2.1.jar my.op/DeSimilarDocs/DeSimilarDocs.java

spl:
	sc -T -M Main



