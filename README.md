# oracle-aq-demo

Demonstrates connecting to an Oracle AQ using the JMS API.

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


