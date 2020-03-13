package tools;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ExternalDB {

	public static final int EXECUTION_TIMEOUT = 60 * 10; // Seconds
	private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String CONN_STR = "jdbc:oracle:thin:@";

	public static List<HashMap<String, Object>> exeSQL(String sql, List<Object> params, String dbUrl, String dbUser,
			String dbPass) {
		return exeSQL(sql, params, dbUrl, dbUser, dbPass, 0);
	}

	public static List<HashMap<String, Object>> exeSQL(String sql, List<Object> params, String dbUrl, String dbUser,
			String dbPass, int queryTimeout) {
		return exeSQL(sql, params, dbUrl, dbUser, dbPass, 0, false);
	}

	public static List<HashMap<String, Object>> exeSQL(String sql, List<Object> params, String dbUrl, String dbUser,
			String dbPass, int queryTimeout, boolean isIndex) {

		List<HashMap<String, Object>> ret = new ArrayList<>();
		java.sql.Connection cn = null;
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Class.forName(ORACLE_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			List<String> cols = new ArrayList<>();

			cn = DriverManager.getConnection(CONN_STR + dbUrl, dbUser, dbPass);

			if (isIndex) {
				PreparedStatement ps2 = cn.prepareStatement(Const.SQL_INDEX);
				if (queryTimeout <= 0) {
					ps2.setQueryTimeout(EXECUTION_TIMEOUT);
				} else {
					ps2.setQueryTimeout(queryTimeout);
				}

				ps2.executeQuery();
				ps2.close();
				ps2 = null;
			}

			ps = cn.prepareStatement(sql);
			if (queryTimeout <= 0)
				ps.setQueryTimeout(EXECUTION_TIMEOUT);
			else
				ps.setQueryTimeout(queryTimeout);

			if (null != params)
				for (int i = 1; i <= params.size(); i++) {
					Object v = params.get(i - 1);
					if (v instanceof java.util.Date)
						ps.setDate(i, new java.sql.Date(((java.util.Date) v).getTime()));
					else
						ps.setObject(i, v);
				}
			rs = ps.executeQuery();
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				cols.add(rs.getMetaData().getColumnName(i));
			}
			while (rs.next()) {
				HashMap<String, Object> row = new HashMap<>();
				for (String col : cols) {
					row.put(col, rs.getObject(col));
				}
				ret.add(row);
			}

			rs.close();
			rs = null;
			ps.close();
			ps = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != cn) {
				try {
					cn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			if (ps != null) {
				try {
					ps.close();
					ps = null;
				} catch (Exception e1) {
					// Nothing
				}
			}

			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (Exception e1) {
					// Nothing
				}
			}
		}
		return ret;
	}

	private static HashMap<String, String> dynamicSql = new HashMap<>();

	public static List<HashMap<String, Object>> exeSQL(String sql, Map<String, Object> params, String dbUrl,
			String dbUser, String dbPass) {
		return exeSQL(sql, params, dbUrl, dbUser, dbPass, false);
	}

	public static List<HashMap<String, Object>> exeSQL(String sql, Map<String, Object> params, String dbUrl,
			String dbUser, String dbPass, boolean isIndex) {

		// ========== Check params ==========
		if (null == sql)
			return Collections.emptyList();
		if (null == params)
			params = new Hashtable<>();
		// -------------------------

		int i = 0;
		int paramCount = 0;
		String paramPrefix = ":";
		Map<Integer, String> paramsLocal = new HashMap<>();

		// Боломжит шинэ мөрүүдийг шалгадаг болгов.
		String newLine1 = "\r"; // TOAD
		String newLine2 = "\r\n";
		String newLine3 = "\n";

		while (i < sql.length()) {
			String currentChar = sql.substring(i, i + 1);
			if (paramPrefix.equals(currentChar)) {
				StringBuilder sbParamName = new StringBuilder();
				while (!" ".equals(currentChar) && !"	".equals(currentChar) && !newLine3.equals(currentChar)
						&& !newLine2.equals(currentChar) && !newLine1.equals(currentChar)) {
					sbParamName.append(currentChar);
					i++;
					if (i == sql.length())
						break;
					currentChar = sql.substring(i, i + 1);
				}
				paramsLocal.put(paramCount++, sbParamName.toString());
			}
			i++;
		}

		List<Object> newParams = new ArrayList<>();
		String newSql = sql;
		for (Entry<Integer, String> p : paramsLocal.entrySet()) {
			newSql = Func.replaceFirst(newSql, p.getValue(), "?");
			newParams.add(params.get(Func.replaceFirst(p.getValue(), paramPrefix, "")));
		}

		// ========== LOG ==========
		if (!dynamicSql.containsKey(sql) && dynamicSql.size() <= 1000) {
			dynamicSql.put(sql, newSql);
			StringBuilder pp = new StringBuilder();
			for (Entry<Integer, String> p : paramsLocal.entrySet()) {
				pp.append(p.getValue()).append(System.getProperty("line.separator"));
			}
		}
		// --------------------------

		return exeSQL(newSql, newParams, dbUrl, dbUser, dbPass, 0, isIndex);
	}
}
