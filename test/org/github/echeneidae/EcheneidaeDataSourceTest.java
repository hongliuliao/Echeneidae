package org.github.echeneidae;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hongliuliao
 * 2014-4-11 下午1:58:42
 */
public class EcheneidaeDataSourceTest {

	@Test
	public void testGetConnection() throws SQLException {
		String driverClassName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost/test?connectTimeout=1000&socketTimeout=1000";
		EcheneidaeDataSource ds = new EcheneidaeDataSource(driverClassName, url, "root", "");
		
		QueryRunner queryer = new QueryRunner(ds);
		List<Map<String, Object>> result = queryer.query("select * from tb_users", new MapListHandler());
		System.out.println(result);
		
		try {
			// 执行到这里需要暂停mysql服务
			result = queryer.query("select * from tb_users", new MapListHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			result = queryer.query("select * from tb_users", new MapListHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
		result = queryer.query("select * from tb_users", new MapListHandler());
		Assert.assertNotNull(result);
	}
	
}
