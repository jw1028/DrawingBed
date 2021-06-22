package org.example.util;

/**
 * 获取数据库连接
 */
public class MysqlDataSource {
    private static volatile com.mysql.jdbc.jdbc2.optional.MysqlDataSource DATA_SOURCE = null;
    private MysqlDataSource() {}
    public static com.mysql.jdbc.jdbc2.optional.MysqlDataSource getInstance() {
        if (DATA_SOURCE == null) {
            synchronized (com.mysql.jdbc.jdbc2.optional.MysqlDataSource.class) {
                if (DATA_SOURCE == null) {
                    DATA_SOURCE = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
                    DATA_SOURCE.setURL("jdbc:mysql://localhost:3306/drawing_bed");
                    DATA_SOURCE.setUser("root");
                    DATA_SOURCE.setPassword("12345678");
                    DATA_SOURCE.setUseSSL(false);
                    DATA_SOURCE.setCharacterEncoding("UTF-8");
                }
            }
        }
        return DATA_SOURCE;
    }
}
