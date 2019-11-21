# oracle-aq-demo

Demonstrates connecting to an Oracle AQ using the JMS API.

## Pre-requisites

You'll need _aqapi.jar_ and _ojdbc8.jar_:

- Download Oracle Database distribution or client (`linuxx64_12201_database.zip` or `linuxx64_12201_client.zip`).
- Unzip.
- Extract `aqapi.jar` from `./client/stage/Components/oracle.rdbms.util/12.2.0.1.0/1/DataFiles/filegroup1.jar`
- Copy `ojdbc8.jar` from `./client/stage/ext/jlib/`

Then add these to Maven, e.g.:

    mvn install:install-file -Dfile=aqapi.jar -DgroupId=com.oracle -DartifactId=aqapi -Dversion=12.2.0.1 -Dpackaging=jar
    mvn install:install-file -Dfile=ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc -Dversion=12.2.0.1 -Dpackaging=jar

## To run

To run the sender:

    mvn clean compile exec:java \
        -Dexec.mainClass=xyz.tomd.demos.oracleaq.Send \
        -Dhostname=localhost -Dport=1521 -Dsid=ORCLCDB \
        -Dusername=scott -Dpassword=tiger -Dqueue=FOOQUEUE

To run the receiver:

    mvn clean compile exec:java \
        -Dexec.mainClass=xyz.tomd.demos.oracleaq.Receive \
        -Dhostname=localhost -Dport=1521 -Dsid=ORCLCDB \
        -Dusername=scott -Dpassword=tiger -Dqueue=FOOQUEUE
