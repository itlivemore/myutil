package pers.itlivemore.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @Title GenerateInsertSql.java
 * @Package: com.test.generatecode
 * @Description: 生成Merge into语句，适用于sqlserver
 *
 * @Author: laigc
 * @Date: 2016年7月21日 下午6:27:56
 *
 *        Copyright @ 2016 Corpration Name
 * 
 */
public class GenerateMergeSql {
	Connection connection = null;
	PreparedStatement ps = null;

	private static Map<Integer, String> floatMap = new HashMap<>();// 存放Jdbc中小数的类型,暂时只用到了Key
	static {
		floatMap.put(3, "BigDecimal");
		floatMap.put(8, "Double");
		floatMap.put(6, "Float");
	}

	private Connection getConnection(String driverName, String dbURL, String userName, String userPwd) {
		try {
			// 加载驱动
			Class.forName(driverName);
			// 获取连接
			connection = DriverManager.getConnection(dbURL, userName, userPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	void generateInsertSqlFromDB(String tableName) throws Exception {
		Pattern pattern = Pattern.compile("[A-Z]+");

		List<String> tableColumnNameList = new ArrayList<>(); // 存放数据库表中字段名
		List<String> classColumnNameList = new ArrayList<>(); // 存放java类中字段名
		Map<String, Integer> classColumnTypeMap = new HashMap<>(); // java类中字段名作key，类型作value

		ps = connection.prepareStatement("select top 10 * from " + tableName);
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			// 处理字段名
			String tableColumnName = rsmd.getColumnName(i); // 数据库表中字段名
			String classColumnName = ""; // java类中的字段名
			if (pattern.matcher(tableColumnName).matches()) {
				// 如果数据库中的字段全为大写，则把该字段所有字母修改为小写，如ID修改为id
				classColumnName = tableColumnName.toLowerCase();
			} else {
				// 替换第一个字母为小写
				classColumnName = tableColumnName.substring(0, 1).toLowerCase()
						+ tableColumnName.replaceFirst("\\w", "");
			}
			tableColumnNameList.add(tableColumnName);
			classColumnNameList.add(classColumnName);

			// 处理字段类型
			int columnType = rsmd.getColumnType(i);
			classColumnTypeMap.put(classColumnName, columnType);
		}

		// 增加过滤字段
		List<String> filterList = new ArrayList<>();
		// filterList = Arrays.asList("planID", "StockDevReason",
		// "BondDevReason", "ConvBDDevReason",
		// "CashDevReason", "FundDevReason", "UpdateTime");

		Set<String> set = new HashSet<>();
		if (filterList.isEmpty()) {
			for (String string : tableColumnNameList) {
				set.add(string.toLowerCase());
			}
		} else {
			for (String string : filterList) {
				set.add(string.toLowerCase());
			}
		}

		for (Iterator<String> iterator = tableColumnNameList.iterator(); iterator.hasNext();) {
			String columnName = iterator.next().toLowerCase();
			if (!set.contains(columnName)) {
				iterator.remove();
			}
		}
		
		for (Iterator<String> iterator = classColumnNameList.iterator(); iterator.hasNext();) {
			String columnName = iterator.next().toLowerCase();
			if (!set.contains(columnName)) {
				iterator.remove();
			}
		}

		System.out.print("MERGE INTO " + tableName + " p USING ( ");
		System.out.println("<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\" UNION ALL \">");
		System.out.print("SELECT ");
		int size = tableColumnNameList.size();
		for (int i = 0; i < size; i++) {
			String tableColumnName = tableColumnNameList.get(i);
			String classColumnName = classColumnNameList.get(i);
			Integer classColumnType = classColumnTypeMap.get(classColumnName);
			if (i != size - 1) {
				if (floatMap.containsKey(classColumnType)) {
					printFloatNotLast(classColumnName, tableColumnName);
				} else {
					System.out.print("#{item." + classColumnName + "} " + tableColumnName + ",");
				}
			} else {
				if (floatMap.containsKey(classColumnType)) {
					printFloatLast(classColumnName, tableColumnName);
				} else {
					System.out.print("#{item." + classColumnName + "} " + tableColumnName);
				}
			}
		}
		System.out.print("</foreach>");
		System.out.println(") np ");
		System.out.println("ON (p.ID = np.ID )");
		System.out.println("WHEN NOT MATCHED THEN ");
		System.out.print("INSERT(");

		for (int i = 0; i < size; i++) {
			if (i != size - 1) {
				System.out.print(tableColumnNameList.get(i) + ",");
			} else {
				System.out.print(tableColumnNameList.get(i));
			}
		}
		System.out.println(")");
		System.out.print("VALUES(");
		for (int i = 0; i < size; i++) {
			if (i != size - 1) {
				System.out.print("np." + tableColumnNameList.get(i) + ",");
			} else {
				System.out.print("np." + tableColumnNameList.get(i));
			}
		}
		System.out.println(")");
		System.out.println("WHEN MATCHED THEN ");
		System.out.print("UPDATE SET ");
		for (int i = 0; i < size; i++) {
			if (i != size - 1) {
				System.out.print("p." + tableColumnNameList.get(i) + "=np." + tableColumnNameList.get(i) + ",");
			} else {
				System.out.print("p." + tableColumnNameList.get(i) + "=np." + tableColumnNameList.get(i));
			}
		}
		System.out.println(";");
	}

	void printFloatNotLast(String classColumnName, String tableColumnName) {
		// 如果是小数类型添加空判断
		System.out.println("\r\n<choose>");
		System.out.println("	<when test=\"item." + classColumnName + " == null\">");
		System.out.println("		NULL " + tableColumnName + ",");
		System.out.println("	</when>");
		System.out.println("	<otherwise>");
		System.out.println("		${item." + classColumnName + "} " + tableColumnName + ",");
		System.out.println("	</otherwise>");
		System.out.println("</choose>");
	}

	void printFloatLast(String classColumnName, String tableColumnName) {
		// 如果是小数类型添加空判断
		System.out.println("\r\n<choose>");
		System.out.println("	<when test=\"item." + classColumnName + " == null\">");
		System.out.println("		NULL " + tableColumnName);
		System.out.println("	</when>");
		System.out.println("	<otherwise>");
		System.out.println("		${item." + classColumnName + "} " + tableColumnName);
		System.out.println("	</otherwise>");
		System.out.println("</choose>");
	}

	public static void main(String[] args) {
		String dataBaseName = "test"; // 数据库名
		String tableName = "UserInfo"; // 要生成sql的表名

		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // 加载JDBC驱动
		String dbURL = "jdbc:sqlserver://127.0.0.1:1433; DatabaseName=" + dataBaseName; // 连接服务器和数据库
		String userName = "sa"; // 默认用户名
		String userPwd = "sa"; // 密码
		try {
			GenerateMergeSql generateMergeSql = new GenerateMergeSql();
			generateMergeSql.getConnection(driverName, dbURL, userName, userPwd);
			generateMergeSql.generateInsertSqlFromDB(tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
