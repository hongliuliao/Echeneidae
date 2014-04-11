package org.github.echeneidae;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hongliuliao
 * 2014-4-11 下午1:58:42
 */
public class EcheneidaeDataSourceTest {
	
	private EcheneidaeDataSource getDataSource() {
		String driverClassName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost/test?connectTimeout=1000&socketTimeout=1000";
		EcheneidaeDataSource ds = new EcheneidaeDataSource(driverClassName, url, "root", "");
		return ds;
	}

	@Test
	public void testGetConnection() throws SQLException {
		EcheneidaeDataSource ds = this.getDataSource();
		
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
	
	@Test
	public void testMutilThread() throws InterruptedException, ExecutionException {
		ExecutorService threadPool = Executors.newFixedThreadPool(3);
		final EcheneidaeDataSource ds = this.getDataSource();
		final QueryRunner queryer = new QueryRunner(ds);
		
		List<Future<?>> futures = new ArrayList<Future<?>>();
		for (int i = 0; i < 100; i++) {
			Future<?> f = threadPool.submit(new Runnable() {
				
				@Override
				public void run() {
					try {
						queryer.query("select * from tb_users", new MapListHandler());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});
			futures.add(f);
		}
		
		for (Future<?> future : futures) {
			future.get();
		}
	}
	
}
