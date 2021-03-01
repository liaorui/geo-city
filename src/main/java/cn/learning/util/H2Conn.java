package cn.learning.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author liaorui
 * @date 2021/3/1
 */
public class H2Conn {
    private static volatile H2Conn h2Conn;
    private Properties prop;

    private H2Conn() {
        try {
            InputStream is = H2Conn.class.getResourceAsStream("/db.properties");
            prop = new Properties();
            prop.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static H2Conn getInstance() {
        if (h2Conn == null) {
            synchronized (H2Conn.class) {
                if (h2Conn == null) {
                    h2Conn = new H2Conn();
                }
            }
        }
        return h2Conn;
    }

    public Connection getConn() throws ClassNotFoundException, SQLException {
        Class.forName(prop.getProperty("driverClassName"));
        return DriverManager
            .getConnection(prop.getProperty("url"), prop.getProperty("user"), prop.getProperty("password"));
    }
}
