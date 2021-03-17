package cn.learning.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author liaorui
 * @date 2021/3/1
 */
public class H2Conn {
    private static volatile H2Conn h2Conn;
    private Properties prop;
    private DruidDataSource dataSource;

    private H2Conn() {
        try {
            InputStream is = H2Conn.class.getResourceAsStream("/db.properties");
            prop = new Properties();
            prop.load(is);
            dataSource = getDataSource();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                dataSource.close();
            }));
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

    public Connection getConn() throws SQLException {
        return dataSource.getConnection();
    }

    public DruidDataSource getDataSource() throws Exception {
        Map properties = new HashMap<>(10);
        properties.put("driverClassName", prop.getProperty("driverClassName"));
        properties.put("url", prop.getProperty("url"));
        properties.put("username", prop.getProperty("user"));
        properties.put("password", prop.getProperty("password"));
        properties.put("initialSize", "2");
        properties.put("minIdle", "2");
        properties.put("maxActive", "10");
        properties.put("validationQuery", "SELECT 1");
        return (DruidDataSource)DruidDataSourceFactory.createDataSource(properties);
    }
}
