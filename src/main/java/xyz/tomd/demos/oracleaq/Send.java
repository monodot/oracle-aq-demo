package xyz.tomd.demos.oracleaq;

import oracle.jms.AQjmsQueueConnectionFactory;

import javax.jms.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Based on the awesome blog post:
 * https://laurentschneider.com/wordpress/2013/09/advanced-queuing-hello-world.html
 */
public class Send {

    public static void main(String args[]) throws SQLException, JMSException {
        String hostname = System.getProperty("hostname");
        String port = System.getProperty("port");
        String sid = System.getProperty("sid");
        String username = System.getProperty("username");
        String password = System.getProperty("password");
        String queue = System.getProperty("queue");

        String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)" +
                "(Host=" + hostname + ")" +
                "(Port=" + port + ")" +
                ")" +
                "(CONNECT_DATA=(SID=" + sid + "))" +
                ")";

        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

        java.sql.Connection conn = DriverManager.getConnection(url, props);
        QueueConnection qconn = AQjmsQueueConnectionFactory.createQueueConnection(conn);
        QueueSession qsess = qconn.createQueueSession(true, 0);
        Queue q = qsess.createQueue(queue);

        TextMessage msg = qsess.createTextMessage("TEST JAVA");
        QueueSender qsend = qsess.createSender(q);
        qsend.send(msg);

        qsess.commit();

    }
}
