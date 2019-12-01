# oracle-aq-demo

Demonstrates connecting to an Oracle AQ using the JMS API.

## Pre-requisites

### Setting up a demo Oracle Advanced Queuing (AQ) queue

Setting up some demo queues in Oracle. First go to https://hub.docker.com/_/oracle-database-enterprise-edition and accept the Terms. Then start an instance of Oracle:

```
podman pull store/oracle/database-enterprise:12.2.0.1
podman run -d -it --name oracle -p 1521:1521 store/oracle/database-enterprise:12.2.0.1
```

Open a shell on the container, and execute:

```
sqlplus / as sysdba
# OR: sqlplus sys/Oradoc_db1@ORCLCDB as sysdba

alter session set "_ORACLE_SCRIPT"=true;
create user scott identified by tiger;
grant dba to scott;
```

Then set up some queues - [thanks to Laurent Schneider's awesome blog post!][schneider]:

```
grant execute on dbms_aq to scott;
grant execute on dbms_aqadm to scott;
grant execute on dbms_aqin to scott;

EXEC dbms_aqadm.create_queue_table('FOOQUEUETABLE', 'SYS.AQ$_JMS_TEXT_MESSAGE')
EXEC dbms_aqadm.create_queue('FOOQUEUE','FOOQUEUETABLE')
EXEC dbms_aqadm.start_queue('FOOQUEUE')
set serverout on


DECLARE
enqueue_options DBMS_AQ.ENQUEUE_OPTIONS_T;
message_properties DBMS_AQ.MESSAGE_PROPERTIES_T;
message_handle RAW (16);
msg SYS.AQ$_JMS_TEXT_MESSAGE;
BEGIN
msg := SYS.AQ$_JMS_TEXT_MESSAGE.construct;
msg.set_text('HELLO PLSQL WORLD !');
DBMS_AQ.ENQUEUE (
queue_name => 'FOOQUEUE',
enqueue_options => enqueue_options,
message_properties => message_properties,
payload => msg,
msgid => message_handle);
COMMIT;
END;
/

select owner, table_name from dba_all_tables where table_name = 'QT';
select owner, table_name from dba_all_tables where table_name = 'FOOQUEUETABLE';
select count(*) from sys.qt;
```

Now you'll have an AQ queue called `FOOQUEUE`.

### Adding Oracle client libraries

You'll need _aqapi.jar_ and _ojdbc8.jar_:

- Download Oracle Database distribution or client (`linuxx64_12201_database.zip` or `linuxx64_12201_client.zip`).
- Unzip.
- Extract `aqapi.jar` from inside `./client/stage/Components/oracle.rdbms.util/12.2.0.1.0/1/DataFiles/filegroup1.jar`
- Copy `ojdbc8.jar` from `./client/stage/ext/jlib/`

Then add these to Maven, e.g.:

```
mvn install:install-file -Dfile=aqapi.jar -DgroupId=com.oracle -DartifactId=aqapi -Dversion=12.2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc -Dversion=12.2.0.1 -Dpackaging=jar
```

## To run

To run the sender:

```
mvn clean compile exec:java \
    -Dexec.mainClass=xyz.tomd.demos.oracleaq.Send \
    -Dhostname=localhost -Dport=1521 -Dsid=ORCLCDB \
    -Dusername=scott -Dpassword=tiger -Dqueue=FOOQUEUE
```

To run the receiver:

```
mvn clean compile exec:java \
    -Dexec.mainClass=xyz.tomd.demos.oracleaq.Receive \
    -Dhostname=localhost -Dport=1521 -Dsid=ORCLCDB \
    -Dusername=scott -Dpassword=tiger -Dqueue=FOOQUEUE
```

[schneider]: https://laurentschneider.com/wordpress/2013/09/advanced-queuing-hello-world.html
