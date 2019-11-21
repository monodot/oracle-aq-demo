package xyz.tomd.demos.oracleaq;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jms.AQjmsFactory;
import oracle.jms.AQjmsQueueConnectionFactory;

import javax.jms.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Receive {

    public static void main(String args[]) throws SQLException, JMSException {
        String hostname = System.getProperty("hostname");
        String port = System.getProperty("port");
        String sid = System.getProperty("sid");
        String username = System.getProperty("username");
        String password = System.getProperty("password");
        String queue = System.getProperty("queue");

        Connection conn = null;

        String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)" +
                "(Host=" + hostname + ")" +
                "(Port=" + port + ")" +
                ")" +
                "(CONNECT_DATA=(SID=" + sid + "))" +
                ")";

        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        // Create a datasource
        OracleDataSource ds = new OracleDataSource();
        ds.setURL(url);
        ds.setUser(username);
        ds.setPassword(password);

        try {
            // Create AQ connection factory and wire up the datasource
            ConnectionFactory cf = AQjmsFactory.getQueueConnectionFactory(ds);

            conn = cf.createConnection();

            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Queue q = session.createQueue(queue);
            MessageConsumer consumer = session.createConsumer(q);

            conn.start();

            TextMessage received = (TextMessage) consumer.receive(5000L);
            System.out.println("Received a message! " + received.getText());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }
}
