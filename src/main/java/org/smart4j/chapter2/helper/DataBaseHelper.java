package org.smart4j.chapter2.helper;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.util.PropsUtil;

/**
 * 数据库操作
 * 
 * @author zhanglin
 *
 * @date 2018年8月26日
 */
public class DataBaseHelper {

	private static final Logger logger = LoggerFactory.getLogger(DataBaseHelper.class);
	private static final QueryRunner runner = new QueryRunner();
	private static final String DRIVER;
	private static final String URL;
	private static final String USERNAME;
	private static final String PASSWORD;
	private static final int initSize;
	private static final int maxSize;
	private static final int maxIdle;
	private static final boolean testWhileIdle;
	private static final int timeBetweenEvictionRunsMillis;
	private static final BasicDataSource DATA_SOURCE;
	private static ThreadLocal<Connection> local = new ThreadLocal<>();

	static {
		Properties p = PropsUtil.loadProperties("config.properties");
		if (p == null)
			try {
				throw new FileNotFoundException("config.properties not found");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		DRIVER = p.getProperty("jdbc.driver");
		URL = p.getProperty("jdbc.url");
		
		USERNAME = p.getProperty("jdbc.username");
		PASSWORD = p.getProperty("jdbc.password");
		initSize = PropsUtil.getInt(p, "jdbc.initSize");
		maxSize = PropsUtil.getInt(p, "jdbc.maxSize");
		maxIdle = PropsUtil.getInt(p, "jdbc.maxIdle");
		testWhileIdle = PropsUtil.getBoolean(p, "jdbc.testWhileIdle");
		timeBetweenEvictionRunsMillis = PropsUtil.getInt(p, "jdbc.timeBetweenEvictionRunsMillis");
		DATA_SOURCE = new BasicDataSource();
		DATA_SOURCE.setDriverClassName(DRIVER);
		DATA_SOURCE.setUrl(URL);
		DATA_SOURCE.setUsername(USERNAME);
		DATA_SOURCE.setPassword(PASSWORD);
		/*DATA_SOURCE.setInitialSize(initSize);
		DATA_SOURCE.setMaxActive(maxSize);
		DATA_SOURCE.setMaxIdle(maxIdle);
		DATA_SOURCE.setTestWhileIdle(testWhileIdle);
		DATA_SOURCE.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);*/
	}

	public static Connection getConnect() {
		Connection conn = local.get();
		if (conn == null) {
			try {

				conn = DATA_SOURCE.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("create connection error.");
			} finally {
				local.set(conn);
			}
		}
		return conn;
	}

	public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
		List<T> entityList = null;
		Connection conn = local.get();
		if (conn == null) {
			conn = getConnect();
		}
		try {
			entityList = runner.query(conn, sql, params, new BeanListHandler<>(entityClass));
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("query list fail error");
		}
		return entityList;
	}
	
	public static <T> T getEntity(Class<T> entityClass, String sql, Object... params) {
		T entity = null;
		Connection conn = local.get();
		if (conn == null) {
			conn = getConnect();
		}
		try {
			entity = runner.query(conn, sql, new BeanHandler<>(entityClass) , params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return entity;
	}
	
	public static List<Map<String, Object>> executeQuery(String sql, Object ...params) {
		List<Map<String, Object>> result = null;
		Connection conn = local.get();
		if (conn == null) {
			conn = getConnect();
		}
		try {
			result = runner.query(conn, sql, new MapListHandler() , params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static int executeUpdate(String sql, Object ...params) {
		int rows = 0;
		Connection conn = local.get();
		if (conn == null) {
			conn = getConnect();
		}
		try {
			rows = runner.update(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}
	
	public static <T> int updateEntity(Class<T> entityClass,long id, Map<String, Object> map) {
		String sql = "update " + entityClass.getSimpleName() + " set ";
		StringBuilder sb = new StringBuilder();
		List<Object> list = new ArrayList<>();
		for (Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey()).append(" =?, ");
			list.add(entry.getValue());
		}
		sql += sb.substring(0, sb.lastIndexOf(",")) + " where id = ?";
		list.add(id);
		Object[] params = list.toArray();
		int i = DataBaseHelper.executeUpdate(sql, params);
		return i;
	}
	
	public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> map) {
		if (map == null || map.size() == 0) {
			logger.error("map not empty");
			return false;
		}
		String sql = "insert into " + entityClass.getSimpleName(); 
		List<Object> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		StringBuilder values = new StringBuilder();
		sb.append(" (");
		values.append("(");
		for (Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey()).append(",");
			values.append("?,");
			list.add(entry.getValue());
		}
		sb.replace(sb.lastIndexOf(","), sb.length(), ")");
		values.replace(values.lastIndexOf(","), values.length(), ")");
		sql += sb + " values " + values;
		Object[] params = list.toArray();
		return executeUpdate(sql, params) == 1;
	}
	
	public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
		String sql = "delete from " + entityClass.getSimpleName() + " where id = ?";
		return DataBaseHelper.executeUpdate(sql, id) == 1;
	}
	
	public static <T> List<T> queryEntity(Class<T> entityClass) {
		String sql = "select * from " + entityClass.getSimpleName();
		return DataBaseHelper.queryEntityList(entityClass, sql, null);
	}
}
