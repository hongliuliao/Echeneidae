Echeneidae
==========

a connection pool , supply a long connection for per thread 

##特点:##
- 1.实现模式简单:一个线程对应一个数据库长连接,无需加锁
- 2.使用简单:几乎无外部依赖,几乎无需配置即可使用
- 3.为了链接复用,请配合线程池一起使用

##使用要求:##
jdk 1.6以上,jdbc4

##TODO:##
未实现存储过程调用

##实现方式:##
>	第一次建立连接之后,首先将连接保存到ThreadLocal中,之后代理连接的close方法,让close的时候不执行关闭连接的操作,
下次需要连接的时候,直接返回之前建立的连接,如果在执行sql语句的时候出现了异常,会检查一下连接是否正常,如果不正常了,会把连接
标记为无效,并在下次获取连接的时候关闭旧连接,并重新创建新的连接

##使用方式:##
	使用方式和普通的数据源一样,以下是一个配合commons-dbutils的例子
	
	String driverClassName = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost/test?connectTimeout=1000&socketTimeout=1000";
	EcheneidaeDataSource ds = new EcheneidaeDataSource(driverClassName, url, "root", "");
	
	QueryRunner queryer = new QueryRunner(ds);
	List<Map<String, Object>> result = queryer.query("select * from tb_users", new MapListHandler());
	System.out.println(result);
	
