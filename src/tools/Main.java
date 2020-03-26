package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class Main {

	static HashMap<String, String> hm = new HashMap<>();
	static HashMap<String, String> ms = new HashMap<>();

	/**
	 * subLayer: 20 - Table, 30 - Type, 40 - Функц, 50 - View, 60 - Procedure, 70 -
	 * Package, 80 - Data (constants, configs etc), 90 - Бусад (шаардлагатай үед
	 * ашиглана)
	 */

	static String startUp = "";
	static String module = "";

	static String dbName = "NES_2013";
	static String dbPassword = "gcm";
	static String dbPort = "172.16.116.49:1521/nes";

	static String CREATE_SCRIPT_PATH = System.getProperty("user.dir") + "\\CREATE_METADATA_SCRIPTS\\";
	static String FILE_OUTPUT_PATH = "C:\\nes";

	static String[] systemList = { "CIF" };

	public static void main(String[] args) throws Exception {
		createFullDbChange();
//		oneline();
//		chgExistingDbChange();
	}

	private static void createFullDbChange() {
		String path = FILE_OUTPUT_PATH;

		putModules();
		putModuleSystem();
		Func.checkAndCreateDir(path);

		startUp = "";

		print("*******************************Баазын өөрчилөлтийг үүсгэж эхлэв*******************************");

		for (String s : systemList) {
			if (startUp != null && !startUp.equals("")) {
				startUp = startUp + "\r\n" + "--------------------------- " + hm.get(s)
						+ " ---------------------------";
			} else {
				startUp = " ---------------------------" + hm.get(s) + "--------------------------- ";
			}

			module = s;

			createFuncs(path, false);
		}
		writeToFile(path + File.separator + "startup_text.txt", startUp);

		print("*******************************Баазын өөрчилөлтийг үүсгэж дууслаа*******************************");
	}

	public static void createFuncs(String path) {
		createFuncs(path, false);
	}

	// Table
	private static void createFuncs(String path, boolean onlyTable) {
		createTableAll(path);

		if (!onlyTable) {
			// View
			createViewDbChange(path);
			
			// System
			createDataDbChange(path, DBchangeType.GEN_SYSTEM);

			// Const
			createDataDbChange(path, DBchangeType.CONST);

			// Dictionary
			createDataDbChange(path, DBchangeType.DICT);

			// Msg
			createDataDbChange(path, DBchangeType.MSG);

			// Screen
			createDataDbChange(path, DBchangeType.SCREEN);

			// Operation & Privilege
			createDataDbChange(path, DBchangeType.OPER);

			// Cache
			createDataDbChange(path, DBchangeType.CACHE);

			// Bulg
			createDataDbChange(path, DBchangeType.BULG_TYPE);

			// Config
			createDBChanges(path, DataType.CONFIG);

			// PROD_TYPE
			createDBChanges(path, DataType.PROD_TYPE);

			// BAL_TYPE
			createDBChanges(path, DataType.BAL_TYPE);

			// BAL_TYPES
			createDBChanges(path, DataType.BAL_TYPES);

			// TXN_CODE
			createDBChanges(path, DataType.TXN_CODE);

			// INT
			createDBChanges(path, DataType.INT);

			// STMT_EXCLUSION
			createDBChanges(path, DataType.STMT_EXCLUSION);

			// CLS
			createDBChanges(path, DataType.CLS);

			// PL_OPER
			createDBChanges(path, DataType.PL_OPER);

			// NMW_OPER_COL
			createDBChanges(path, DataType.NMW_OPER_COL);

			// NMW_OPERATIONS
			createDBChanges(path, DataType.NMW_OPERATIONS);

			// GLIP_GL_CFG
			createDBChanges(path, DataType.GL_CFG);

			// GLIP_CONT_ENTRY_CFG
			createDBChanges(path, DataType.GL_CONT_CFG);

			// GLIP_TXN_CONFIG
			createDBChanges(path, DataType.GL_TXN_CFG);

			// TLLR_OPER
			createDataDbChange(path, DBchangeType.TLLR_OPER);

			// LM_OPER
			createDBChanges(path, DataType.LM_OPER);

			// PROC_OPER
			createDataDbChange(path, DBchangeType.PROC_OPER);
		}
	}

	// Table үүсгэх
	private static void createTableAll(String path) {
		List<String> tables = new ArrayList<>();
		List<String> columns = new ArrayList<>();
		List<String> foreignKeys = new ArrayList<>();
		List<String> indexes = new ArrayList<>();

		String text = "";
		print(module + " модулын байзын файл үүсэгж эхлэв.");
		String filePath = path + File.separator + hm.get(module);
		Func.checkAndCreateDir(filePath);
		filePath = filePath + File.separator + "src_" + hm.get(module).toLowerCase();

		tables = prepTableFiles(CREATE_SCRIPT_PATH + "1_CreateTable_NEW.sql", module, false);
		columns = prepTableFiles(CREATE_SCRIPT_PATH + "2_AddColumn.sql", module, false);
		foreignKeys = prepTableFiles(CREATE_SCRIPT_PATH + "3_ForeignKey_NEW.sql", module, false);
		indexes = prepTableFiles(CREATE_SCRIPT_PATH + "4_Index_NEW.sql", module, true);

		List<String> tableNames = getTableNames(module);
		HashMap<String, Long> tableTier = getTableTier(tableNames, foreignKeys);

		for (String name : tableNames) {
			text = "";
			String chkTable = "CHK_TBL('" + name + "')";
			String chkColumn = "CHK_COL('" + name + "'";
			String chkForeignKey = "ALTER TABLE " + name + " ADD CONSTRAINT";
			String chkIndex = "ON " + name + " (";
			for (String t : tables) {
				if (t.contains(chkTable)) {
					text = text + t + "{newLine}";
					text = text + "{newLine}";
					tables.remove(t);
					break;
				}
			}
			for (String c : columns) {
				if (c.contains(chkColumn)) {
					text = text + c + "{newLine}";
				}
			}
			for (String i : indexes) {
				if (i.contains(chkIndex)) {
					text = text + i + "{newLine}";
				}
			}
			for (String f : foreignKeys) {
				if (f.contains(chkForeignKey)) {
					text = text + f + "{newLine}";
				}
			}

			String fileName = "src_" + hm.get(module).toLowerCase() + "_tbl_" + name.toLowerCase();
			Long tier = tableTier.get(name);
			String timeStamp = fileName + "$2" + tier + Func.toDateTimeStr(new Date(), "yyMMddHHmmss");

			text = "--" + timeStamp + "{newLine}" + text;
			text = text.replace("{newLine}", "\r\n");

			String writePath = path + File.separator + hm.get(module).toLowerCase();
			Func.checkAndCreateDir(writePath);
			Func.checkAndCreateDir(writePath + File.separator + "db");
			String outPut = writePath + File.separator + "db" + File.separator + fileName + ".sql";

			writeToFile(outPut, text);
			String su = '"' + timeStamp + '"' + ",";
			startUp = startUp + "\r\n" + su;

			print(name + " table file created!");
		}
	}

	private static HashMap<String, Long> getTableTier(List<String> tables, List<String> foreignKeys) {
		HashMap<String, Long> tableTier = new HashMap<>();
		HashMap<String, List<String>> tableKeys = new HashMap<>();
		List<String> sortTables = new ArrayList<>();
		List<String> tmpList = new ArrayList<>();
		String chkForeignKey;
		for (String t : tables) {
			chkForeignKey = "ALTER TABLE " + t + " ADD CONSTRAINT";
			boolean isExist = false;
			tmpList = new ArrayList<>();
			for (String f : foreignKeys) {
				if (f.contains(chkForeignKey)) {
					String parentTable = getStrWithRegex(f, "REFERENCES ", " (");
					if (parentTable != null && !parentTable.equals("")) {
						if (parentTable.startsWith(module) && !parentTable.equals(t)) {
							isExist = true;
							tmpList.add(parentTable);
						}
					}
				}
			}

			if (isExist) {
				sortTables.add(t);
				tableTier.put(t, 1L);
				tableKeys.put(t, tmpList);
			} else {
				tableTier.put(t, 0L);
			}
		}

		boolean sorting = false;
		if (sortTables != null && !sortTables.isEmpty()) {
			sorting = true;
		}
		while (sorting) {
			List<String> tmpSort = new ArrayList<String>();
			for (String name : sortTables) {
				Long tier = tableTier.get(name);
				tmpList = tableKeys.get(name);
				for (String f : tmpList) {
					Long parentTier = tableTier.get(f);
					if (tier <= parentTier) {
						tableTier.put(name, tier + 1);
						tmpSort.add(name);
						break;
					}
				}

			}

			if (tmpSort != null && !tmpSort.isEmpty()) {
				sortTables = tmpSort;
			} else {
				sorting = false;
			}
		}

		return tableTier;

	}

	private static List<String> prepTableFiles(String filePath, String moduleName, boolean isIndex) {
		String sql = "";
		List<String> data = new ArrayList<>();

		List<String> line = readFileInList(filePath);

		Iterator<String> itr = line.iterator();
		while (itr.hasNext()) {
			sql = sql + itr.next() + " ";
		}
		sql = sql.replace("&&tableprefix", moduleName);

		List<HashMap<String, Object>> lstRes = ExecuteQuery(sql, isIndex);

		for (HashMap<String, Object> l : lstRes) {
			data.add(l.get("CREATESCRIPT").toString());
		}

		return data;
	}

	private static List<String> getTableNames(String moduleName) {
		List<String> tableNames = new ArrayList<>();
		String textFile = "";
		String sql = "SELECT TABLE_NAME FROM ALL_TABLES WHERE OWNER = '" + dbName + "' AND TABLE_NAME LIKE '"
				+ moduleName + "%'";

		List<HashMap<String, Object>> lstRes = ExecuteQuery(sql, false);

		for (HashMap<String, Object> l : lstRes) {
			textFile = l.get("TABLE_NAME").toString();
			tableNames.add(textFile);
		}

		return tableNames;
	}

	// View үүсгэх
	private static void createViewDbChange(String path) {
		String sql = Const.SQL_VIEW.replace("{module}", module).replace("{dbName}", dbName);
		List<HashMap<String, Object>> lstRes = ExecuteQuery(sql);

		for (HashMap<String, Object> l : lstRes) {
			String viewName = l.get("VIEW_NAME").toString();
			String viewQuery = l.get("TEXT").toString();
			viewQuery = viewQuery.replace("\n", "\r\n");
			String fileName = "src_" + hm.get(module).toLowerCase() + "_" + viewName.toLowerCase();
			String timeStamp = fileName + "$50" + Func.toDateTimeStr(new Date(), "yyMMddHHmmss");

			viewQuery = "--" + timeStamp + "\r\n" + "CREATE OR REPLACE VIEW " + viewName + "\r\n" + "AS\r\n"
					+ viewQuery;

			String writePath = path + File.separator + hm.get(module).toLowerCase();
			Func.checkAndCreateDir(writePath);
			Func.checkAndCreateDir(writePath + File.separator + "db");
			String filePath = writePath + File.separator + "db" + File.separator + fileName + ".sql";

			writeToFile(filePath, viewQuery);
			String su = '"' + timeStamp + '"' + ",";
			startUp = startUp + "\r\n" + su;
		}

		print(module + " модулын view-ын файл үүсэгж дууслаа");
		print("");
	}

	// View үүсгэх
	private static void createDBChanges(String path, DataType type) {
		String outPutText = "";
		String sql = "";
		String fileType = type.toString();

		switch (type) {
		case CONFIG:
			sql = Const.SQL_MERGE_CONFIG;
			break;
		case PROD_TYPE:
			sql = Const.SQL_MERGE_PROD_TYPE;
			break;
		case BAL_TYPE:
			sql = Const.SQL_MERGE_BAL_TYPE;
			break;
		case BAL_TYPES:
			sql = Const.SQL_MERGE_BAL_TYPES;
			break;
		case TXN_CODE:
			sql = Const.SQL_MERGE_TXN_CODE;
			break;
		case INT:
			sql = Const.SQL_MERGE_INT;
			break;
		case STMT_EXCLUSION:
			sql = Const.SQL_MERGE_STMT_EXCLUSION;
			break;
		case CLS:
			sql = Const.SQL_MERGE_CLS;
			break;
		case PL_OPER:
			sql = Const.SQL_MERGE_PL_OPER;
			break;
		case NMW_OPER_COL:
			sql = Const.SQL_MERGE_NMW_OPER_COL;
			break;
		case NMW_OPERATIONS:
			sql = Const.SQL_MERGE_NMW_OPERATIONS;
			break;
		case GL_CFG:
			sql = Const.SQL_MERGE_GL_CFG;
			break;
		case GL_CONT_CFG:
			sql = Const.SQL_MERGE_GL_CONT_CFG;
			break;
		case GL_TXN_CFG:
			sql = Const.SQL_MERGE_GL_TXN_CFG;
			break;
		case LM_OPER:
			sql = Const.SQL_MERGE_LM_OPER;
			break;
		}
		sql = sql.replace("{system}", ms.get(module)).replace("{module}", module);
		List<HashMap<String, Object>> lstRes = ExecuteQuery(sql);

		if (lstRes != null && !lstRes.isEmpty()) {
			for (HashMap<String, Object> l : lstRes) {
				outPutText = outPutText + l.get("MRG_QUERY").toString() + "{newLine}";
			}
			outPutText = outPutText.replace("{newLine}", "\r\n");

			if (outPutText != null && !outPutText.equals("")) {
				String timeStamp = "80" + Func.toDateTimeStr(new Date(), "yyMMddHHmmss");
				String fileName = timeStamp + "_" + hm.get(module).toLowerCase() + "_" + fileType;

				String writePath = path + File.separator + hm.get(module).toLowerCase();
				Func.checkAndCreateDir(writePath);
				Func.checkAndCreateDir(writePath + File.separator + "db");
				String filePath = writePath + File.separator + "db" + File.separator + fileName + ".sql";

				writeToFile(filePath, outPutText);
				String su = '"' + fileName + '"' + ",";
				startUp = startUp + "\r\n" + su;
			}

		}
		print(module + " модулын " + fileType + "-ын файл үүсэгж дууслаа");
		print("");
	}

	private static void createDataDbChange(String path, DBchangeType type) {
		String typeName = "";
		String outPutText = "";
		String sql = "";

		switch (type) {
		case CONST:
			typeName = "const";
			sql = Const.SQL_CONST;
			break;
		case DICT:
			typeName = "dictionary";
			sql = Const.SQL_DICTIONARY;
			break;
		case MSG:
			typeName = "msg";
			sql = Const.SQL_MSG;
			break;
		case SCREEN:
			typeName = "screen";
			sql = Const.SQL_SCREEN;
			break;
		case GEN_SYSTEM:
		case ADM_SYSTEM:
		case EOD_SYSTEM:
		case RAM_SYSTEM:
			type = DBchangeType.GEN_SYSTEM;
			typeName = "system";
			sql = Const.SQL_GEN_SYSTEM;
			break;
		case OPER:
		case PRIV:
		case OPER_PRIV:
			type = DBchangeType.OPER;
			typeName = "oper_priv";
			sql = Const.SQL_OPER;
			break;
		case TLLR_OPER:
		case TLLR_PRIV:
		case TLLR_OPER_PRIV:
			type = DBchangeType.TLLR_OPER;
			typeName = "tllr_oper_priv";
			sql = Const.SQL_TLLR_OPER;
			break;
		case CACHE:
			typeName = "cache";
			sql = Const.SQL_CACHE;
			break;
		case BULG_TYPE:
		case BULG_FIELD:
			type = DBchangeType.BULG_TYPE;
			typeName = "bulg";
			sql = Const.SQL_BULG_TYPE;
			break;
		case PROC_OPER:
			typeName = "proc_oper";
			sql = Const.SQL_PROC_OPER;
			break;
		}

		print(module + " модулын " + typeName + "-ын файл үүсэгж эхлэв");

		sql = replaceSqlSysMod(sql, type);
		outPutText = executeQuery(sql, type);
		if (type.equals(DBchangeType.OPER)) {
			sql = replaceSqlSysMod(Const.SQL_PRIV, DBchangeType.PRIV);
			outPutText = outPutText + executeQuery(sql, DBchangeType.PRIV);

			sql = replaceSqlSysMod(Const.SQL_OPER_PRIV, DBchangeType.OPER_PRIV);
			outPutText = outPutText + executeQuery(sql, DBchangeType.OPER_PRIV);
		} else if (type.equals(DBchangeType.BULG_TYPE)) {
			sql = replaceSqlSysMod(Const.SQL_BULG_FIELD, DBchangeType.BULG_FIELD);
			outPutText = outPutText + executeQuery(sql, DBchangeType.BULG_FIELD);
		} else if (type.equals(DBchangeType.GEN_SYSTEM)) {
			sql = replaceSqlSysMod(Const.SQL_ADM_SYSTEM, DBchangeType.ADM_SYSTEM);
			outPutText = outPutText + executeQuery(sql, DBchangeType.ADM_SYSTEM);

			sql = replaceSqlSysMod(Const.SQL_EOD_SYSTEM, DBchangeType.EOD_SYSTEM);
			outPutText = outPutText + executeQuery(sql, DBchangeType.EOD_SYSTEM);

			sql = replaceSqlSysMod(Const.SQL_RAM_SYSTEM, DBchangeType.RAM_SYSTEM);
			outPutText = outPutText + executeQuery(sql, DBchangeType.RAM_SYSTEM);
		} else if (type.equals(DBchangeType.TLLR_OPER)) {
			sql = replaceSqlSysMod(Const.SQL_TLLR_PRIV, DBchangeType.TLLR_PRIV);
			outPutText = outPutText + executeQuery(sql, DBchangeType.TLLR_PRIV);

			sql = replaceSqlSysMod(Const.SQL_TLLR_OPER_PRIV, DBchangeType.TLLR_OPER_PRIV);
			outPutText = outPutText + executeQuery(sql, DBchangeType.TLLR_OPER_PRIV);
		} else {
			// do nothing.
		}

		if (outPutText != null && !outPutText.equals("")) {
			outPutText = outPutText.replace("{newLine}", "\r\n");
			String timeStamp = "80" + Func.toDateTimeStr(new Date(), "yyMMddHHmmss");
			String fileName = timeStamp + "_" + hm.get(module).toLowerCase() + "_" + typeName;

			String writePath = path + File.separator + hm.get(module).toLowerCase();
			Func.checkAndCreateDir(writePath);
			Func.checkAndCreateDir(writePath + File.separator + "db");
			String filePath = writePath + File.separator + "db" + File.separator + fileName + ".sql";

			writeToFile(filePath, outPutText);
			String su = '"' + fileName + '"' + ",";
			startUp = startUp + "\r\n" + su;
		}
		print(module + " модулын " + typeName + "-ын файл үүсэгж дууслаа");
		print("");
	}

	private static String replaceSqlSysMod(String sql, DBchangeType type) {
		switch (type) {
		case CONST:
		case DICT:
		case CACHE:
		case BULG_TYPE:
		case BULG_FIELD:
		case PROC_OPER:
		case TLLR_OPER:
		case TLLR_PRIV:
		case TLLR_OPER_PRIV:
			sql = sql.replace("{module}", module);
			break;
		case MSG:
		case SCREEN:
		case GEN_SYSTEM:
		case ADM_SYSTEM:
		case EOD_SYSTEM:
		case RAM_SYSTEM:
		case OPER:
		case PRIV:
		case OPER_PRIV:
			sql = sql.replace("{system}", ms.get(module));
			break;
		}

		return sql;
	}

	private static String executeQuery(String sql, DBchangeType type) {
		String outPutText = "";
		List<HashMap<String, Object>> lstRes = ExecuteQuery(sql);

		for (HashMap<String, Object> l : lstRes) {
			String text = "";

			switch (type) {
			case CONST:
				text = replaceQueryConst(l);
				break;
			case DICT:
				text = replaceQueryDictionary(l);
				break;
			case MSG:
				text = replaceQueryMsg(l);
				break;
			case SCREEN:
				text = replaceQueryScreen(l);
				break;
			case GEN_SYSTEM:
				text = replaceQuerySystem1(l);
				break;
			case OPER:
			case PROC_OPER:
			case TLLR_OPER:
				text = replaceQueryOper(l);
				break;
			case CACHE:
				text = replaceQueryCache(l);
				break;
			case BULG_TYPE:
				text = replaceQueryBulgType(l);
				break;
			case BULG_FIELD:
				text = replaceQueryBulgField(l);
				break;
			case PRIV:
			case TLLR_PRIV:
				text = replaceQueryPriv(l);
				break;
			case OPER_PRIV:
			case TLLR_OPER_PRIV:
				text = replaceQueryOperPriv(l);
				break;
			case ADM_SYSTEM:
			case EOD_SYSTEM:
			case RAM_SYSTEM:
				text = replaceQuerySystem2(l, type);
				break;
			}

			if (text != null && !text.equals("")) {
				outPutText = outPutText + text + "{newLine}";
			}
		}

		return outPutText;
	}

	private static String replaceQueryConst(HashMap<String, Object> l) {
		String mergeScript = Const.SQL_MERGE_CONST;

		String tableName = Func.toString(l.get("TABLE_NAME"));
		String colName = Func.toString(l.get("COL_NAME"));
		String colValue = Func.toString(l.get("COL_VALUE"));
		String name = Func.toString(l.get("NAME"));
		String name2 = Func.toString(l.get("NAME2"));
		String orderNo = Func.toString(l.get("ORDER_NO"));

		mergeScript = mergeScript.replace("{param1}", tableName).replace("{param2}", colName)
				.replace("{param3}", colValue).replace("{param4}", name).replace("{param5}", name2)
				.replace("{param6}", orderNo).replace("{module}", module);

		return mergeScript;
	}

	private static String replaceQueryDictionary(HashMap<String, Object> l) {
		String mergeScript = Const.SQL_MERGE_DICTIONARY;

		String dictCode = Func.toString(l.get("DICT_CODE"));
		String viewName = Func.toString(l.get("VIEW_NAME"));
		String tableName = Func.toString(l.get("TABLE_NAME"));
		String isMulti = Func.toString(l.get("IS_MULTILANG"));

		mergeScript = mergeScript.replace("{param1}", dictCode).replace("{param2}", viewName)
				.replace("{param3}", tableName).replace("{param4}", isMulti);

		return mergeScript;
	}

	private static String replaceQueryMsg(HashMap<String, Object> l) {
		String mergeScript = Const.SQL_MERGE_MSG;

		String msgCode = Func.toString(l.get("MSG_CODE"));
		String sysNo = Func.toString(l.get("SYS_NO"));
		String msgType = Func.toString(l.get("MSG_TYPE"));
		String msgDesc = Func.toString(l.get("MSG_DESC"));
		String msgDesc2 = Func.toString(l.get("MSG_DESC2"));

		mergeScript = mergeScript.replace("{param1}", msgCode).replace("{param2}", sysNo).replace("{param3}", msgType)
				.replace("{param4}", msgDesc).replace("{param5}", msgDesc2);

		return mergeScript;
	}

	private static String replaceQueryScreen(HashMap<String, Object> l) {
		String mergeScript = Const.SQL_MERGE_SCREEN;

		String scrCode = Func.toString(l.get("SCR_CODE"));
		String sysNo = Func.toString(l.get("SYS_NO"));
		String scrKey = Func.toString(l.get("SCR_KEY"));
		String scrName = Func.toString(l.get("SCR_NAME"));
		String scrName2 = Func.toString(l.get("SCR_NAME2"));
		String scrDesc = Func.toString(l.get("SCR_DESC"));
		String isMenu = Func.toString(l.get("IS_MENU"));
		String menuName = Func.toString(l.get("MENU_NAME"));
		String menuName2 = Func.toString(l.get("MENU_NAME2"));

		mergeScript = mergeScript.replace("{param1}", scrCode).replace("{param2}", sysNo).replace("{param3}", scrKey)
				.replace("{param4}", scrName).replace("{param5}", scrName2).replace("{param6}", scrDesc)
				.replace("{param7}", isMenu).replace("{param8}", menuName).replace("{param9}", menuName2);

		return mergeScript;
	}

	private static String replaceQuerySystem1(HashMap<String, Object> l) {
		String mergeScript = Const.SQL_MERGE_GEN_SYSTEM;

		String sysNo = Func.toString(l.get("SYS_NO"));
		String name = Func.toString(l.get("NAME"));
		String name2 = Func.toString(l.get("NAME2"));
		String status = Func.toString(l.get("STATUS"));
		String orderNo = Func.toString(l.get("ORDER_NO"));

		mergeScript = mergeScript.replace("{param1}", sysNo).replace("{param2}", name).replace("{param3}", name2)
				.replace("{param4}", status).replace("{param5}", orderNo);

		return mergeScript;
	}

	private static String replaceQuerySystem2(HashMap<String, Object> l, DBchangeType type) {
		String mergeScript = "";
		String sysNo = Func.toString(l.get("SYS_NO"));

		if (type.equals(DBchangeType.ADM_SYSTEM)) {
			mergeScript = Const.SQL_MERGE_ADM_SYSTEM;
		} else if (type.equals(DBchangeType.EOD_SYSTEM)) {
			if (checkSystem(sysNo, true)) {
				mergeScript = Const.SQL_MERGE_EOD_SYSTEM;
			} else {
				return "";
			}
		} else {
			if (checkSystem(sysNo, false)) {
				mergeScript = Const.SQL_MERGE_RAM_SYSTEM;
			} else {
				return "";
			}
		}

		mergeScript = mergeScript.replace("{param1}", sysNo);

		return mergeScript;
	}

	private static String replaceQueryOper(HashMap<String, Object> l) {
		String mergeScript = Const.SQL_MERGE_OPER;

		String operCode = Func.toString(l.get("OPER_CODE"));
		String name = Func.toString(l.get("NAME"));
		String name2 = Func.toString(l.get("NAME2"));
		String sysNo = Func.toString(l.get("SYS_NO"));
		String lookup = Func.toString(l.get("LOOKUP"));
		String funcName = Func.toString(l.get("FUNC_NAME"));
		String auditLevel = Func.toString(l.get("AUDIT_LEVEL"));
		String logReqData = Func.toString(l.get("LOG_REQ_DATA"));
		String logResData = Func.toString(l.get("LOG_RES_DATA"));
		String location = Func.toString(l.get("LOCATION"));

		mergeScript = mergeScript.replace("{param1}", operCode).replace("{param2}", name).replace("{param3}", name2)
				.replace("{param4}", sysNo).replace("{param5}", lookup).replace("{param6}", funcName)
				.replace("{param7}", auditLevel).replace("{param8}", logReqData).replace("{param9}", logResData)
				.replace("{param10}", location);

		return mergeScript;
	}

	private static String replaceQueryCache(HashMap<String, Object> l) {
		String mergeScript = Const.SQL_MERGE_CACHE;

		String tableName = Func.toString(l.get("TABLE_NAME"));
		String pkName = Func.toString(l.get("PK_NAME"));
		String expTime = Func.toString(l.get("EXP_TIME"));
		String cacheSize = Func.toString(l.get("CACHE_SIZE"));

		mergeScript = mergeScript.replace("{param1}", tableName).replace("{param2}", pkName)
				.replace("{param3}", expTime).replace("{param4}", cacheSize);

		return mergeScript;
	}

	private static String replaceQueryBulgType(HashMap<String, Object> l) {
		String mergeScript = Const.SQL_MERGE_BULG_TYPE;

		String objType = Func.toString(l.get("OBJ_TYPE"));
		String name = Func.toString(l.get("NAME"));
		String name2 = Func.toString(l.get("NAME2"));
		String purgeCond = Func.toString(l.get("PURGE_COND"));
		String keepDays = Func.toString(l.get("KEEP_DAYS"));
		String keepTxns = Func.toString(l.get("KEEP_TXNS"));

		mergeScript = mergeScript.replace("{param1}", objType).replace("{param2}", name).replace("{param3}", name2)
				.replace("{param4}", purgeCond).replace("{param5}", keepDays).replace("{param6}", keepTxns);

		return mergeScript;
	}

	private static String replaceQueryBulgField(HashMap<String, Object> l) {
		String mergeScript = Const.SQL_MERGE_BULG_FIELD;

		String objType = Func.toString(l.get("OBJ_TYPE"));
		String fieldId = Func.toString(l.get("FIELD_ID"));
		String name = Func.toString(l.get("NAME"));
		String name2 = Func.toString(l.get("NAME2"));

		mergeScript = mergeScript.replace("{param1}", objType).replace("{param2}", fieldId).replace("{param3}", name)
				.replace("{param4}", name2);

		return mergeScript;
	}

	private static String replaceQueryPriv(HashMap<String, Object> l) {
		String mergeScript = Const.SQL_MERGE_PRIV;

		String privCode = Func.toString(l.get("PRIV_CODE"));
		String sysNo = Func.toString(l.get("SYS_NO"));
		String name = Func.toString(l.get("NAME"));
		String name2 = Func.toString(l.get("NAME2"));
		String privType = Func.toString(l.get("PRIV_TYPE"));
		String parentPrivCode = Func.toString(l.get("PARENT_PRIV_CODE"));

		mergeScript = mergeScript.replace("{param1}", privCode).replace("{param2}", sysNo).replace("{param3}", name)
				.replace("{param4}", name2).replace("{param5}", privType).replace("{param6}", parentPrivCode);

		return mergeScript;
	}

	private static String replaceQueryOperPriv(HashMap<String, Object> l) {
		String mergeScript = Const.SQL_MERGE_OPER_PRIV;

		String operCode = Func.toString(l.get("OPER_CODE"));
		String privCode = Func.toString(l.get("PRIV_CODE"));

		mergeScript = mergeScript.replace("{param1}", operCode).replace("{param2}", privCode);

		return mergeScript;
	}

	private static boolean checkSystem(String sysNo, boolean isEod) {
		boolean retBool = false;

		String sql = "select * from ram_systems where sys_no = " + sysNo;

		if (isEod) {
			sql = "select * from eod_systems where sys_no = " + sysNo;
		}

		List<HashMap<String, Object>> lstRes = ExecuteQuery(sql);

		if (lstRes != null && !lstRes.isEmpty()) {
			retBool = true;
		}

		return retBool;
	}

	private static boolean chkFileName(String name) {
		if (name.startsWith("SRC_FNC") || name.startsWith("SRC_PKB") || name.startsWith("SRC_PKS")
				|| name.startsWith("SRC_TYPE")) {
			return true;
		} else {
			return false;
		}
	}

	// pkg, type, func баазын өөрчилөлт (SRC_FNC, SRC_TYPE, SRC_PKB, SRC_PKS) байх
	// юм бол модулын код оруулж дотор нь timestamp коммент хэсгээр оруулан.
	private static void chgExistingDbChange() {
		String path = "D:\\nes-server\\loan.b\\db_old";
		String destPath = "D:\\nes-server\\loan.b\\db";

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && chkFileName(listOfFiles[i].getName())) {
				chgExistingDbChangeSingle(listOfFiles[i].getPath(), destPath, listOfFiles[i].getName());
			}
		}
	}

	private static void chgExistingDbChangeSingle(String filePath, String distPath, String fileName) {
		String sql = "";

		List<String> l = readFileInList(filePath);

		Iterator<String> itr = l.iterator();
		while (itr.hasNext()) {
			String newLine = itr.next();

			if (newLine != null && !newLine.equals("")) {
				if (sql != null && !sql.equals("")) {
					sql = sql + newLine + "{newLine}";

				} else {
					sql = newLine + "{newLine}";
				}
			}
		}
		sql = sql.replace("{newLine}", "\r\n");

		String subLayer = "";
		if (fileName.startsWith("SRC_FNC")) {
			subLayer = "40";
		} else if (fileName.startsWith("SRC_TYPE")) {
			subLayer = "30";
		} else {
			subLayer = "70";
		}
		fileName = fileName.toLowerCase();
		fileName = fileName.split("\\.sql")[0];
		fileName = fileName.split("src_")[1];
		fileName = "src_loan.b_" + fileName;
		String timeStamp = fileName + "$" + subLayer + Func.toDateTimeStr(new Date(), "yyMMddHHmmss");

		sql = "--" + timeStamp + "\r\n" + sql;
		distPath = distPath + File.separator + fileName + ".sql";
		writeToFile(distPath, sql);
		print('"' + timeStamp + '"' + ",");
	}

	// Лист жагсаалт болгох
	private static void listStr(List<String> listStr) {

		String res = "('" + String.join("', '", listStr) + "')";

		print(res);
	}

	// sublayer төрлийн файлын нэр харуулах
	private static void printFileName(String path, String subLayer) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().startsWith(subLayer)) {
				print('"' + listOfFiles[i].getName().split("\\.sql")[0] + '"' + ",");
			}
		}
	}

	// байлын замаас sublayer-т байгаа баазын өөрчилөлтүүдийг нэг мөр болгох
	private static void onelineAll(String path, String subLayer) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().startsWith(subLayer)) {
				oneline(listOfFiles[i].getPath());
			}
		}
	}

	// regex шалгах
	private static void regexChecker() {
		String text = "select :POLICY_CODE, :p2, :MTRX|DAY|AGE from Customer where name = :pp3 ";
		String ptrn = "(\\:[\\w|]+)";

		regexChecker(text, ptrn);
	}

	private static void regexChecker(String text, String pattern) {
		List<String> parameters = new ArrayList<>();
		Pattern ptrn = Pattern.compile(pattern);
		Matcher m = ptrn.matcher(text);
		while (m.find()) {
			String match = m.group(1);
			match = match.replaceAll("\\:", "");
			parameters.add(match);
		}
		System.out.println("Parameters: " + parameters);
		text = text.replaceAll("\\|", "");
		print(text);
	}

	// Төрсөн өдөр тооцох
	private static int calculateAge(Date birthDate) {
		int years = 0;
		int months = 0;

		Calendar birthDay = Calendar.getInstance();
		birthDay.setTimeInMillis(birthDate.getTime());

		long currentTime = System.currentTimeMillis();
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(currentTime);

		years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
		int currMonth = now.get(Calendar.MONTH) + 1;
		int birthMonth = birthDay.get(Calendar.MONTH) + 1;

		months = currMonth - birthMonth;

		if (months < 0) {
			years--;
			months = 12 - birthMonth + currMonth;
			if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
				months--;
		} else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
			years--;
			months = 11;
		}

		if (now.get(Calendar.DATE) == birthDay.get(Calendar.DATE)) {
			if (months == 12) {
				years++;
				months = 0;
			}
		}
		return years;
	}

	// Trunc round
	private static double truncate(double x) {
		return Math.floor(x * 100) / 100;
	}

	// Нэг мөрөнд оруулах
	private static void oneline() {
		String filePath = "D:\\nes-server\\asr.b\\db\\80200324195130_asr.b_add_ntf_data.sql";
		oneline(filePath);
	}

	private static void oneline(String filePath) {

		String sql = "";

		List<String> l = readFileInList(filePath);

		Iterator<String> itr = l.iterator();
		boolean isFirstLine = true;
		while (itr.hasNext()) {
			boolean isLastLine = false;
			String newLine = itr.next();
			newLine = newLine.replaceAll("\\s+", " ");

			if (newLine != null && !newLine.equals("")) {
				if (newLine.contains("~~")) {
					isLastLine = true;
				}

				if (sql != null && !sql.equals("")) {
					if (isFirstLine) {
						sql = sql + newLine;
						isFirstLine = false;
					} else {
						sql = sql + " " + newLine;
					}
					if (isLastLine) {
						sql = sql + "{newLine}";
						isFirstLine = true;
					}
				} else {
					sql = newLine;
				}
			}
		}
		sql = sql.replaceAll("\\s+", " ");
		sql = sql.replace("{newLine}", "\r\n");

		writeToFile(filePath, sql);
	}

	// бэлэн байсан баазын өөрчилөлтөөс ерөнхий тохиргооны нэр авах
	private static void getConfig(String filePath) {
		List<String> l = readFileInList(filePath);

		Iterator<String> itr = l.iterator();
		while (itr.hasNext()) {
			String newLine = itr.next();
			newLine = newLine.replaceAll("\\s+", " ");

			if (newLine != null && !newLine.equals("")) {
				getStrWithRegex(newLine, "SYS_NO, '", "' as ITEM_CODE");
			}
		}
	}

	// Файлаас мөр өөр нь салгаж жагсаалт болгох
	private static List<String> readFileInList(String fileName) {

		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		}

		catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	// Файл руу текс бичих
	private static void writeToFile(String filePath, String text) {
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
			bw.write(text);
			bw.flush();
			bw.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + filePath + "'");
		}
	}

	// view дотхор комент, BEQUEATH DEFINER хэсгийг хасах
	private static void cleanAndWriteSrcFile(String filePath, String startString) throws FileNotFoundException {
		cleanAndWriteSrcFile(filePath, startString, false);
	}

	private static void cleanAndWriteSrcFile(String filePath, String startString, boolean removeComment)
			throws FileNotFoundException {
		String sql = "";

		List<String> l = readFileInList(filePath);

		Iterator<String> itr = l.iterator();
		while (itr.hasNext()) {
			if (sql != null && !sql.equals("")) {
				sql = sql + "{newLine}" + itr.next();
			} else {
				sql = itr.next();
			}
		}

		if (removeComment) {
			sql = removeWithRegex(sql, "/*", "*/");
		}
		sql = removeWithRegex(sql, "(", "BEQUEATH DEFINER");
		sql = sql.replace(";", "");
		sql = sql.trim();

		sql = sql.replace("{newLine}", "\r\n");

		sql = startString + "\r\n" + sql;

		writeToFile(filePath, sql);
	}

	// өгөгдсөн фатерны хоорондохыг цэвэрлэх
	private static String removeWithRegex(String str, String p1, String p2) {
		String regexString = Pattern.quote(p1) + "(.*?)" + Pattern.quote(p2);

		Pattern pattern = Pattern.compile(regexString);
		Matcher matcher = pattern.matcher(str);

		while (matcher.find()) {
			String textInBetween = matcher.group(1);

			str = str.replace(p1 + textInBetween + p2, "");
		}
		return str;
	}

	// patter-уудын хоорондох тэмдэгтийг авах
	private static String getStrWithRegex(String str, String p1, String p2) {
		String regexString = Pattern.quote(p1) + "(.*?)" + Pattern.quote(p2);

		Pattern pattern = Pattern.compile(regexString);
		Matcher matcher = pattern.matcher(str);
		String textInBetween = "";

		while (matcher.find()) {
			textInBetween = matcher.group(1);
		}
		return textInBetween;
	}

	// Текстээс огноо гаргаж авах
	private static Date str2Date(String pDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
		Date dt = null;
		try {
			dt = formatter.parse(pDate);
		} catch (ParseException ex) {
			System.out.println(String.format("data: %s, err: %s", pDate, ex.getMessage()));
		}
		return dt;
	}

	// file-ын урд src нэмж байгаа
	private static void chgFileType(String filePath, String moduleCode) throws IOException {
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				Path copied = Paths
						.get(filePath + File.separator + "src_" + moduleCode + "_" + listOfFiles[i].getName());
				Path originalPath = listOfFiles[i].toPath();
				try {
					Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// timestamp нэмэх
	private static void addAndGetTimestamp(String filePath, String subLayer) throws IOException {
		addAndGetTimestamp(filePath, subLayer, true);
	}

	private static void addAndGetTimestamp(String filePath, String subLayer, boolean isView) throws IOException {
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String[] fileName = listOfFiles[i].getName().split("\\.sql");

				String timestamp = fileName[0] + "$" + subLayer + Func.toDateTimeStr(new Date(), "yyMMddHHmmss");
				String startup = '"' + timestamp + '"' + ",";
				timestamp = "-- " + timestamp;
				cleanAndWriteSrcFile(listOfFiles[i].getPath(), timestamp, isView);

				print(startup);
			}
		}
	}

	// тайлангын файлын зам авах
	private static void getRptPath(String rptPath) {
		File folder = new File(rptPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getPath().contains(".prpt")) {
				String ret = listOfFiles[i].getPath().replace("C:\\Users\\badamsereedari.t\\Documents\\", "");
				print(ret);
			} else if (listOfFiles[i].isDirectory()) {
				getRptPath(listOfFiles[i].getPath());
			}
		}
	}

	// тайлангын файлын зам солих
	private static void rptChgFolder() {
		String rptPath = "C:\\Users\\badamsereedari.t\\Documents\\test\\CBS_STANDARD";
		rptChgFolder(rptPath);
	}

	private static void rptChgFolder(String rptPath) {

		File folder = new File(rptPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {

			} else if (listOfFiles[i].isDirectory()) {
				rptChgFolderSingle(listOfFiles[i].getPath(), rptPath);
			}
		}
	}

	private static void rptChgFolderSingle(String path, String rootPath) {
		String prevFolder = path;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		boolean hasFile = false;

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				print(listOfFiles[i].getName());
				copyToPrevFolder(listOfFiles[i], rootPath);
				hasFile = true;
			} else if (listOfFiles[i].isDirectory()) {
				rptChgFolderSingle(listOfFiles[i].getPath(), rootPath);
			}
		}

		if (hasFile) {
			deleteFolder(prevFolder);
		}
	}

	private static void copyToPrevFolder(File file, String rootPath) {
		String copyPath = file.getParentFile().getParentFile().getPath();
		if (!rootPath.equals(copyPath)) {
			Path copied = Paths.get(copyPath + File.separator + file.getName());
			Path originalPath = file.toPath();
			try {
				Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
				if (!file.delete()) {
					print("Failed to delete old file! file: " + file.getName());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void deleteFolder(String path) {
		File file = new File(path);
		deleteDir(file);
	}

	private static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				if (!Files.isSymbolicLink(f.toPath())) {
					deleteDir(f);
				}
			}
		}
		file.delete();
	}

	private static void print(String msg) {
		System.out.println(msg);
	}

	// KENDO маст шалгалт
	private static String kendoMaskToRegex(String kendoMask) {

		StringBuilder sbRegex = new StringBuilder();
		int count = 0;
		char lastChar = Character.MIN_VALUE;
		if (null != kendoMask && !kendoMask.isEmpty()) {
			for (char ch : kendoMask.toCharArray()) {
				if (ch == lastChar) {
					count++;
				} else {
					sbRegex = new StringBuilder(sbRegex.toString().replace("COUNT", String.valueOf(count)));
					count = 0;
					switch (ch) {
					// Тоо. 0 -с 9 хүртэл цифр заавал оруулна.
					case '0':
						sbRegex.append("(\\d){COUNT}");
						break;
					// Тоо болон хоосон зай. 0 -с 9 хүртэлх цифр оруулж болно.
					case '9':
						sbRegex.append("(\\d|\\s){COUNT}");
						break;
					// Тоо болон хоосон зай. Дээрх "9" гэсэн дүрэмтэй ижил, харин дээр нь нэмээд (+)
					// болон (-) тэмдгүүдийг оруулж болно.
					case '#':
						sbRegex.append("(\\d|\\s|+|-){COUNT}");
						break;
					// Үсэг. Жижиг (а-я) болон ТОМ (А-Я) үсэг заавал оруулна.
					case 'M':
						sbRegex.append("[а-яА-Я]{COUNT}");
						break;
					case 'L':
						sbRegex.append("[а-яА-Яa-zA-Z]{COUNT}");
						break;
					// Үсэг болон хоосон зай. Жижиг (а-я) болон ТОМ (А-Я) үсэг оруулж болно.
					case '?':
						sbRegex.append("[а-яА-Яa-zA-Z|\\s]{COUNT}");
						break;
					// Тэмдэгт. Бүх төрлийн тэмдэгт эсвэл хоосон зай заавал оруулна.
					case '&':
						sbRegex.append("(\\S){COUNT}");
						break;
					// Тэмдэгт эсвэл хоосон зай. Бүх төрлийн тэмдэгт оруулж болно.
					case 'C':
						sbRegex.append("(.){COUNT}");
						break;
					// Тоо болон үсэг. Зөвхөн үсэг юм уу эсвэл тоо оруулна.
					case 'A':
						sbRegex.append("[а-яА-Яa-zA-Z0-9]{COUNT}");
						break;
					// Тоо, үсэг, болон хоосон зай. Зөвхөн үсэг юм уу тоо эсвэл хоосон зай оруулж
					// болно.
					case 'a':
						sbRegex.append("[а-яА-Яa-zA-Z0-9|\\s]{COUNT}");
						break;
					// Бутархайн эсвэл мянганы орон, он сар өдөр, тусгаарлагч.
					case '.':
					case ',':
					case ':':
					case ';':
					case '-':
					case '/':
					case '$':
					case '(':
					case ')':
						sbRegex.append("(\\").append(ch).append("){COUNT}");
						break;
					default:
						sbRegex.append("(").append(ch).append("){COUNT}");
						break;
					}
					lastChar = ch;
					count++;
				}
			}
			if (count > 0) {
				sbRegex = new StringBuilder(sbRegex.toString().replace("COUNT", String.valueOf(count)));
			}
		}

		return sbRegex.toString();
	}

	// html -> pdf
	private static String htmlToPdf() {
		String htmlPath = "D:\\Workspace\\VATS-EMAIL.HTML";
		String pdfPath = "D:\\Workspace\\PDF_test.pdf";

		return htmlToPdf(htmlPath, pdfPath);
	}

	private static String htmlToPdf(String htmlPath, String pdfPath) {
		Document document = new Document();
		// step 2
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
			// step 3
			document.open();
			// step 4
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(htmlPath));
			// step 5
			document.close();
		} catch (Exception e1) {
			print(e1.getMessage());
		}

		return pdfPath;
	}

	// qr-ын зургийн замийг авж base64 болгох
	@SuppressWarnings("resource")
	private static String qrToBase64(String qrPath) throws IOException {
		String base64String = "";

		File file = new File(qrPath);
		FileInputStream fileInputStreamReader = new FileInputStream(file);
		byte[] bytes = new byte[(int) file.length()];
		fileInputStreamReader.read(bytes);

		base64String = Func.encodeAsBase64(bytes);

		return base64String;
	}

	private static void goSubFolders() {
		File folder = new File("D:\\nes-server");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				try {
					PersistenceChecker.checkEntities(listOfFiles[i].getPath());
				} catch (Exception e) {
					print(e.getMessage());
				}
			}
		}
	}

	private static void putModules() {
		hm.put("GEN", "GEN.S");
		hm.put("NOTE", "NOTE.S");
		hm.put("PB", "PB.S");
		hm.put("IRC", "IRC.S");
		hm.put("FEE", "FEE.S");
		hm.put("EOD", "EOD.S");
		hm.put("PL", "PL.S");
		hm.put("CCY", "CCY.S");
		hm.put("GLIP", "GLIP.S");
		hm.put("BCOM", "BCOM.C");
		hm.put("CIF", "CIF.B");
		hm.put("BAC", "BAC.B");
		hm.put("CASH", "CASH.B");
		hm.put("GL", "GL.B");
		hm.put("CASA", "CASA.B");
		hm.put("TD", "TD.B");
		hm.put("CCA", "CCA.B");
		hm.put("SHR", "SHR.B");
		hm.put("CT", "CT.B");
		hm.put("LOS", "LOS.B");
		hm.put("COLL", "COLL.B");
		hm.put("LINE", "LINE.B");
		hm.put("LOAN", "LOAN.B");
		hm.put("PRVN", "PRVN.B");
		hm.put("CQM", "CQM.B");
		hm.put("TLLR", "TLLR.A");
		hm.put("PROC", "PROC.A");
		hm.put("SDI", "SDI.A");
		hm.put("CIB", "CIB.A");
		hm.put("CCO", "CCO.A");
		hm.put("BL", "BL.S");
		hm.put("TMW", "TMW.M");
		hm.put("NMW", "NMW.M");
		hm.put("OD", "OD.B");
		hm.put("APAY", "APAY.B");
		hm.put("VATS", "VATS.S");
		hm.put("RBD", "RBD.A");
		hm.put("ARCV", "ARCV.B");
		hm.put("ASR", "ASR.B");
	}

	private static void putModuleSystem() {
		ms.put("GEN", "1010");
		ms.put("EOD", "1062");
		ms.put("IRC", "1014");
		ms.put("CIF", "1020");
		ms.put("PL", "1392");
		ms.put("CCY", "1013");
		ms.put("PB", "1362");
		ms.put("FEE", "1051");
		ms.put("GLIP", "1011");
		ms.put("BCOM", "1390");
		ms.put("COLL", "1309");
		ms.put("BAC", "1301");
		ms.put("CASH", "1412");
		ms.put("GL", "1030");
		ms.put("CASA", "1305");
		ms.put("TD", "1306");
		ms.put("CCA", "1319");
		ms.put("SHR", "1310");
		ms.put("CT", "1302");
		ms.put("LOS", "1312");
		ms.put("LINE", "1314");
		ms.put("LOAN", "1308");
		ms.put("PRVN", "1303");
		ms.put("CQM", "1324");
		ms.put("TLLR", "1360");
		ms.put("PROC", "1364");
		ms.put("SDI", "1311");
		ms.put("CIB", "1313");
		ms.put("CCO", "1317");
		ms.put("BL", "1053");
		ms.put("TMW", "1350");
		ms.put("NMW", "1351");
		ms.put("OD", "1307");
		ms.put("APAY", "1327");
		ms.put("VATS", "1033");
		ms.put("RBD", "1066");
		ms.put("ARCV", "1326");
		ms.put("ASR", "1212");
	}

	private static List<HashMap<String, Object>> ExecuteQuery(String sql) {
		return ExecuteQuery(sql, false);
	}

	private static List<HashMap<String, Object>> ExecuteQuery(String sql, boolean isIndex) {
		HashMap<String, Object> tmpGeneratedParam = null;
		return ExternalDB.exeSQL(sql, tmpGeneratedParam, dbPort, dbName, dbPassword, isIndex);
	}
}