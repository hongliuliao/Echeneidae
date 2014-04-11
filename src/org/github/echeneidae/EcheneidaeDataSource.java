package org.github.echeneidae;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author hongliuliao
 * 2014-4-11 上午10:43:54
 */
public class EcheneidaeDataSource implements DataSource {
	
	private static final Log log = LogFactory.getLog(EcheneidaeDataSource.class);
	
	private static final int DEFAULT_SECOND_OF_MAX_IDLE = 60;
	
	private ThreadLocal<EcheneidaeConnection> connections = new ThreadLocal<EcheneidaeConnection>();
	
	private String url;
	
	private String userName;
	
	private String password;
	
	/**
	 * 最大链接空闲时间
	 */
	private int secondOfMaxIdle;
	
	/**
	 * @param url
	 * @param userName
	 * @param password
	 */
	public EcheneidaeDataSource(String driverClassName, String url, String userName, String password) {
		super();
		this.url = url;
		this.userName = userName;
		this.password = password;
		secondOfMaxIdle = DEFAULT_SECOND_OF_MAX_IDLE;
		
		registerDriver(driverClassName);
	}
	
	private void registerDriver(String driverClassName) {
		try {
			Class.forName(driverClassName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int getSecondOfMaxIdle() {
		return secondOfMaxIdle;
	}

	public void setSecondOfMaxIdle(int secondOfMaxIdle) {
		this.secondOfMaxIdle = secondOfMaxIdle;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		throw new UnsupportedOperationException("getLogWriter");
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		throw new UnsupportedOperationException("setLogWriter");
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		throw new UnsupportedOperationException("setLoginTimeout");
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		throw new UnsupportedOperationException("getLoginTimeout");
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException("unwrap");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public Connection getConnection() throws SQLException {
		EcheneidaeConnection conn = connections.get();
		int nowSeconds = (int) (new Date().getTime() / 1000);
		if(conn != null && conn.isValid() && nowSeconds - conn.getLastInteractTime() < secondOfMaxIdle) {
			return conn;
		}
		
		if(conn != null) { // 如果之前的链接还在,但是由于某种原因失效了,需要先关闭它
			conn.close();
		}
		Connection rawConn = DriverManager.getConnection(url, userName, password);
		conn = new EcheneidaeConnection(this, rawConn);
		connections.set(conn);
		log.info("Start create new connection ...");
		return conn;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		throw new UnsupportedOperationException("getConnection with username password");
	}

}
