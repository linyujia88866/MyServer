package cn.websocket;

import cn.entity.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseHelper {

    // 假设这是你的数据库连接方法
    private static Connection getDatabaseConnection() throws SQLException {
        // 实现数据库连接的逻辑
        // 返回Connection对象
        return connect01();
    }

    public static Connection connect01() throws SQLException {
        // 创建oracle的驱动对象
        // Driver driver = new oracle.jdbc.driver.OracleDriver();

        // 创建mysql的驱动对象,由于数据库厂商都实现了java.sql.Driver接口,所以可以使用多态机制
        Driver driver = new com.mysql.jdbc.Driver();

        // 编写连接数据库的url,用户名和密码(保存在Properties对象中,user和password是规定好的键名)
        String osName = System.getProperty("os.name");
        String url;
        if(osName.contains("Windows")){
            url = "jdbc:mysql://47.109.79.50:3306/liyan";
        }else {
            url="jdbc:mysql://172.19.15.159:3306/liyan";
        }

        Properties properties = new Properties();
        properties.setProperty("user", "newuser");
        properties.setProperty("password", "newpassword");

        // 调用驱动对象的connect方法获取连接对象,参数就是url,用户名和密码
        Connection connect = driver.connect(url, properties);
        return connect;
//        System.out.println("静态获取的数据库连接对象 = " + connect); // com.mysql.jdbc.JDBC4Connection@41cf53f9
    }

    public static void insertMessage(String sender, String recipient, String content) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getDatabaseConnection(); // 获取数据库连接
            String sql = "INSERT INTO messages (sender, receiver, content) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, sender);
            pstmt.setString(2, recipient);
            pstmt.setString(3, content);

            pstmt.executeUpdate(); // 执行更新操作
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static List<String> getAllUsers() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        List<String> res = new ArrayList<>();
        try {
            conn = getDatabaseConnection(); // 获取数据库连接
            String sql = "Select * from users";
            pstmt = conn.prepareStatement(sql);
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql); // 执行更新操作
            while (rs.next()) {
                String title = rs.getString("username");
                res.add(title);
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return res;
    }

    public static void insertMessage(Message message) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getDatabaseConnection(); // 获取数据库连接
            String sql = "INSERT INTO messages (sender, receiver, content, type) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, message.getSender());
            pstmt.setString(2, message.getReceiver());
            pstmt.setString(3, message.getContent());
            pstmt.setInt(4, message.getType());

            pstmt.executeUpdate(); // 执行更新操作
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}