
package db;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBUtil {

	private Connection conn = null;

	private Statement sta = null;

	private ResultSet rs = null;

	public DBUtil(){
		this.conn = this.getConnection();
	}

	private Connection getConnection(){
		Connection conn = null;

		try {

			Properties properties = new Properties();
			properties.load(DBUtil.class.getResourceAsStream("db.properties"));

			String driverClass =  properties.getProperty("driver_class");
			String connectionUrl = properties.getProperty("connection_url");
			String dbUser = properties.getProperty("db_user");
			String dbPassword = properties.getProperty("db_password");

			Class.forName("com.mysql.cj.jdbc.Driver");
			Class.forName(driverClass);
			conn = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;

	}

	public int update(String sql) {
		int row = -1;
		try {
			sta = conn.createStatement();
			row = sta.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("update:SQLException寮傚父");
			e.printStackTrace();
		}
		return row;
	}

	public ResultSet query(String sql) {
		try {
			sta = conn.createStatement();
			rs = sta.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public void close() {
		try {
			if(rs != null){
				rs.close();
				rs = null;
				System.out.println(rs);
			}
			if(sta != null){
				sta.close();
				sta = null;
				System.out.println(sta);
			}
			if(conn != null){
				conn.close();
				conn = null;
				System.out.println(conn);
			}
		} catch (SQLException e) {
			System.out.println("close:SQLException寮傚父");
			e.printStackTrace();
		}
	}

}
