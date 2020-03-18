package tools;

public class Const {
	public static final String SQL_CONST = "SELECT * FROM {module}_CONST";
	public static final String SQL_DICTIONARY = "SELECT * FROM GEN_DICTIONARY WHERE DICT_CODE LIKE '{module}%'";
	public static final String SQL_GEN_SYSTEM = "SELECT * FROM GEN_SYSTEM WHERE SYS_NO = {system}";
	public static final String SQL_ADM_SYSTEM = "SELECT * FROM ADM_SYSTEM WHERE SYS_NO = {system}";
	public static final String SQL_EOD_SYSTEM = "SELECT * FROM EOD_SYSTEMS WHERE SYS_NO = {system}";
	public static final String SQL_RAM_SYSTEM = "SELECT * FROM RAM_SYSTEMS WHERE SYS_NO = {system}";
	public static final String SQL_MSG = "SELECT * FROM ADM_MSG WHERE SYS_NO = {system}";
	public static final String SQL_SCREEN = "SELECT * FROM ADM_SCREEN WHERE SYS_NO = {system}";
	public static final String SQL_CACHE = "SELECT * FROM NES_CACHE_CONF WHERE TABLE_NAME LIKE '{module}%' OR TABLE_NAME LIKE 'VW_{module}%'";
	public static final String SQL_BULG_TYPE = "SELECT * FROM BULG_OBJ_TYPE WHERE OBJ_TYPE LIKE '{module}%'";
	public static final String SQL_BULG_FIELD = "SELECT * FROM BULG_OBJ_FIELD WHERE OBJ_TYPE LIKE '{module}%'";
	public static final String SQL_CONFIG = "SELECT * FROM GEN_CONFIG WHERE SYS_NO = {system}";
	public static final String SQL_OPER = "SELECT * FROM ADM_OPERATION WHERE SYS_NO = {system}";
	public static final String SQL_PRIV = "SELECT * FROM ADM_PRIVILEGE WHERE SYS_NO = {system}";
	public static final String SQL_OPER_PRIV = "SELECT * FROM ADM_OPER_PRIV WHERE OPER_CODE LIKE '{system}%'";
	public static final String SQL_VIEW = "SELECT VIEW_NAME, TEXT_VC FROM ALL_VIEWS WHERE (UPPER(VIEW_NAME) LIKE UPPER('VW_{module}%') "
			+ " OR UPPER(VIEW_NAME) LIKE UPPER('VW_DICT_{module}%')) AND OWNER = '{dbName}'";
	public static final String SQL_PROC_OPER = "SELECT * FROM ADM_OPERATION WHERE SYS_NO = 1364 and upper(lookup) like upper('%{module}BatchRemote%')";
	public static final String SQL_INDEX = "BEGIN DBMS_METADATA.set_transform_param (DBMS_METADATA.session_transform, 'SQLTERMINATOR', TRUE); "
			+ " DBMS_METADATA.set_transform_param (DBMS_METADATA.session_transform, 'PRETTY', TRUE); DBMS_METADATA.set_transform_param "
			+ " (DBMS_METADATA.session_transform, 'SEGMENT_ATTRIBUTES', FALSE); DBMS_METADATA.set_transform_param "
			+ " (DBMS_METADATA.session_transform, 'STORAGE', FALSE); END;";

	public static final String SQL_MERGE_CONST = "MERGE INTO {module}_CONST A USING (SELECT '{param1}' "
			+ " as TABLE_NAME, '{param2}' as COL_NAME, '{param3}' "
			+ " as COL_VALUE, '{param4}' as NAME, '{param5}' as NAME2, {param6} "
			+ " as ORDER_NO FROM DUAL) B ON (A.TABLE_NAME = B.TABLE_NAME "
			+ " and A.COL_NAME = B.COL_NAME and A.COL_VALUE = B.COL_VALUE) "
			+ " WHEN NOT MATCHED THEN INSERT (TABLE_NAME, COL_NAME, COL_VALUE, "
			+ " NAME, NAME2, ORDER_NO) VALUES (B.TABLE_NAME, B.COL_NAME, B.COL_VALUE, "
			+ " B.NAME, B.NAME2, B.ORDER_NO) WHEN MATCHED THEN UPDATE SET A.NAME = B.NAME, "
			+ " A.NAME2 = B.NAME2, A.ORDER_NO = B.ORDER_NO~~";
	public static final String SQL_MERGE_DICTIONARY = "MERGE INTO GEN_DICTIONARY A USING (SELECT '{param1}' as DICT_CODE, '"
			+ " {param2}' as VIEW_NAME, '{param3}' as TABLE_NAME, {param4} as IS_MULTILANG FROM DUAL) B "
			+ " ON (A.DICT_CODE = B.DICT_CODE) WHEN NOT MATCHED THEN INSERT ( DICT_CODE, VIEW_NAME, "
			+ " TABLE_NAME, IS_MULTILANG) VALUES ( B.DICT_CODE, B.VIEW_NAME, B.TABLE_NAME, B.IS_MULTILANG) "
			+ " WHEN MATCHED THEN UPDATE SET A.VIEW_NAME = B.VIEW_NAME, A.TABLE_NAME = B.TABLE_NAME, "
			+ " A.IS_MULTILANG = B.IS_MULTILANG~~";
	public static final String SQL_MERGE_GEN_SYSTEM = "MERGE INTO GEN_SYSTEM A USING (SELECT {param1} as SYS_NO, '{param1}' "
			+ " as NAME, '{param2}' as NAME2, {param3} as STATUS, 1 as CREATED_BY, SYSDATE as CREATED_DATETIME, 1 as MODIFIED_BY, "
			+ " SYSDATE as MODIFIED_DATETIME, {param4} as ORDER_NO FROM DUAL) B ON (A.SYS_NO = B.SYS_NO) WHEN NOT MATCHED THEN "
			+ " INSERT (SYS_NO, NAME, NAME2, STATUS, CREATED_BY, CREATED_DATETIME, MODIFIED_BY, MODIFIED_DATETIME, ORDER_NO) "
			+ " VALUES (B.SYS_NO, B.NAME, B.NAME2, B.STATUS, B.CREATED_BY, B.CREATED_DATETIME, B.MODIFIED_BY, B.MODIFIED_DATETIME, "
			+ " B.ORDER_NO) WHEN MATCHED THEN UPDATE SET A.NAME = B.NAME, A.NAME2 = B.NAME2, A.STATUS = B.STATUS, "
			+ " A.CREATED_BY = B.CREATED_BY, A.CREATED_DATETIME = B.CREATED_DATETIME, A.MODIFIED_BY = B.MODIFIED_BY, "
			+ " A.MODIFIED_DATETIME = B.MODIFIED_DATETIME, A.ORDER_NO = B.ORDER_NO~~";
	public static final String SQL_MERGE_ADM_SYSTEM = "MERGE INTO ADM_SYSTEM A USING (SELECT {param1} as SYS_NO FROM DUAL) B "
			+ " ON (A.SYS_NO = B.SYS_NO) WHEN NOT MATCHED THEN INSERT (SYS_NO) VALUES (B.SYS_NO)~~";
	public static final String SQL_MERGE_EOD_SYSTEM = "MERGE INTO EOD_SYSTEMS A USING (SELECT {param1} as SYS_NO, 0 as STEP_COUNT FROM DUAL) B "
			+ " ON (A.SYS_NO = B.SYS_NO) WHEN NOT MATCHED THEN INSERT (SYS_NO, STEP_COUNT) VALUES (B.SYS_NO, B.STEP_COUNT)~~";
	public static final String SQL_MERGE_RAM_SYSTEM = "MERGE INTO RAM_SYSTEMS A USING (SELECT {param1} as SYS_NO FROM DUAL) B "
			+ " ON (A.SYS_NO = B.SYS_NO) WHEN NOT MATCHED THEN INSERT (SYS_NO) VALUES (B.SYS_NO)~~";
	public static final String SQL_MERGE_MSG = "MERGE INTO ADM_MSG A USING (SELECT {param1} as MSG_CODE, {param2} as SYS_NO, "
			+ " {param3} as MSG_TYPE, '{param4}' as MSG_DESC, '{param5}' as MSG_DESC2, 1 as MODIFIED_BY, "
			+ " SYSDATE as MODIFIED_DATETIME FROM DUAL) B ON (A.MSG_CODE = B.MSG_CODE) WHEN NOT MATCHED THEN INSERT ( MSG_CODE, "
			+ " SYS_NO, MSG_TYPE, MSG_DESC, MSG_DESC2, MODIFIED_BY, MODIFIED_DATETIME) VALUES ( B.MSG_CODE, B.SYS_NO, B.MSG_TYPE, "
			+ " B.MSG_DESC, B.MSG_DESC2, B.MODIFIED_BY, B.MODIFIED_DATETIME) WHEN MATCHED THEN UPDATE SET A.SYS_NO = B.SYS_NO, "
			+ " A.MSG_TYPE = B.MSG_TYPE, A.MSG_DESC = B.MSG_DESC, A.MSG_DESC2 = B.MSG_DESC2, A.MODIFIED_BY = B.MODIFIED_BY, "
			+ " A.MODIFIED_DATETIME = B.MODIFIED_DATETIME~~";
	public static final String SQL_MERGE_SCREEN = "MERGE INTO ADM_SCREEN A USING (SELECT '{param1}' as SCR_CODE, {param2} as SYS_NO, "
			+ " '{param3}' as SCR_KEY, '{param4}' as SCR_NAME, '{param5}' as SCR_NAME2, '{param6}' as SCR_DESC, {param7} as IS_MENU, "
			+ " 1 as MODIFIED_BY, SYSDATE as MODIFIED_DATETIME, '{param8}' as MENU_NAME, '{param9}' as MENU_NAME2 FROM DUAL) B ON "
			+ " (A.SCR_CODE = B.SCR_CODE) WHEN NOT MATCHED THEN INSERT ( SCR_CODE, SYS_NO, SCR_KEY, SCR_NAME, SCR_NAME2, SCR_DESC, "
			+ " IS_MENU, MODIFIED_BY, MODIFIED_DATETIME, MENU_NAME, MENU_NAME2) VALUES ( B.SCR_CODE, B.SYS_NO, B.SCR_KEY, B.SCR_NAME, "
			+ " B.SCR_NAME2, B.SCR_DESC, B.IS_MENU, B.MODIFIED_BY, B.MODIFIED_DATETIME, B.MENU_NAME, B.MENU_NAME2) WHEN MATCHED "
			+ " THEN UPDATE SET A.SYS_NO = B.SYS_NO, A.SCR_KEY = B.SCR_KEY, A.SCR_NAME = B.SCR_NAME, A.SCR_NAME2 = B.SCR_NAME2, "
			+ " A.SCR_DESC = B.SCR_DESC, A.IS_MENU = B.IS_MENU, A.MODIFIED_BY = B.MODIFIED_BY, A.MODIFIED_DATETIME = B.MODIFIED_DATETIME, "
			+ " A.MENU_NAME = B.MENU_NAME, A.MENU_NAME2 = B.MENU_NAME2~~";
	public static final String SQL_MERGE_CACHE = "MERGE INTO NES_CACHE_CONF A USING (SELECT '{param1}' as TABLE_NAME, '{param2}' as "
			+ " PK_NAME, {param3} as EXP_TIME, {param4} as CACHE_SIZE FROM DUAL) B ON (A.TABLE_NAME = B.TABLE_NAME) WHEN NOT MATCHED "
			+ " THEN INSERT ( TABLE_NAME, PK_NAME, EXP_TIME, CACHE_SIZE) VALUES ( B.TABLE_NAME, B.PK_NAME, B.EXP_TIME, B.CACHE_SIZE) "
			+ " WHEN MATCHED THEN UPDATE SET A.PK_NAME = B.PK_NAME~~";
	public static final String SQL_MERGE_BULG_TYPE = "MERGE INTO BULG_OBJ_TYPE A USING (SELECT '{param1}' as OBJ_TYPE, '{param2}' as "
			+ " NAME, '{param3}' as NAME2, '{param4}' as PURGE_COND, {param5} as KEEP_DAYS, {param6} as KEEP_TXNS FROM DUAL) B ON "
			+ " (A.OBJ_TYPE = B.OBJ_TYPE) WHEN NOT MATCHED THEN INSERT (OBJ_TYPE, NAME, NAME2, PURGE_COND, KEEP_DAYS, KEEP_TXNS) VALUES "
			+ " (B.OBJ_TYPE, B.NAME, B.NAME2, B.PURGE_COND, B.KEEP_DAYS, B.KEEP_TXNS)~~";
	public static final String SQL_MERGE_BULG_FIELD = "MERGE INTO BULG_OBJ_FIELD A USING (SELECT '{param1}' as OBJ_TYPE, '{param2}' "
			+ " as FIELD_ID, '{param3}' as NAME, '{param4}' as NAME2 FROM DUAL) B ON (A.OBJ_TYPE = B.OBJ_TYPE and A.FIELD_ID = B.FIELD_ID) "
			+ " WHEN NOT MATCHED THEN INSERT (OBJ_TYPE, FIELD_ID, NAME, NAME2) VALUES (B.OBJ_TYPE, B.FIELD_ID, B.NAME, B.NAME2)~~";
	public static final String SQL_MERGE_OPER = "MERGE INTO ADM_OPERATION A USING (SELECT '{param1}' as OPER_CODE, '{param2}' as NAME, "
			+ " '{param3}' as NAME2, {param4} as SYS_NO, '{param5}' as LOOKUP, '{param6}' as FUNC_NAME, {param7} as AUDIT_LEVEL, {param8} "
			+ " as LOG_REQ_DATA, {param9} as LOG_RES_DATA, '{param10}' as LOCATION FROM DUAL) B ON (A.OPER_CODE = B.OPER_CODE) WHEN NOT "
			+ " MATCHED THEN INSERT ( OPER_CODE, NAME, NAME2, SYS_NO, LOOKUP, FUNC_NAME, AUDIT_LEVEL, LOG_REQ_DATA, LOG_RES_DATA, LOCATION) "
			+ " VALUES ( B.OPER_CODE, B.NAME, B.NAME2, B.SYS_NO, B.LOOKUP, B.FUNC_NAME, B.AUDIT_LEVEL, B.LOG_REQ_DATA, B.LOG_RES_DATA, "
			+ " B.LOCATION) WHEN MATCHED THEN UPDATE SET A.NAME = B.NAME, A.NAME2 = B.NAME2, A.SYS_NO = B.SYS_NO, A.LOOKUP = B.LOOKUP, "
			+ " A.FUNC_NAME = B.FUNC_NAME, A.AUDIT_LEVEL = B.AUDIT_LEVEL, A.LOG_REQ_DATA = B.LOG_REQ_DATA, A.LOG_RES_DATA = B.LOG_RES_DATA, "
			+ " A.LOCATION = B.LOCATION~~";
	public static final String SQL_MERGE_PRIV = "MERGE INTO ADM_PRIVILEGE A USING (SELECT '{param1}' as PRIV_CODE, {param2} as SYS_NO, "
			+ " '{param3}' as NAME, '{param4}' as NAME2, {param5} as PRIV_TYPE, '{param6}' as PARENT_PRIV_CODE FROM DUAL) B ON "
			+ " (A.PRIV_CODE = B.PRIV_CODE) WHEN NOT MATCHED THEN INSERT ( PRIV_CODE, SYS_NO, NAME, NAME2, PRIV_TYPE, PARENT_PRIV_CODE) "
			+ " VALUES ( B.PRIV_CODE, B.SYS_NO, B.NAME, B.NAME2, B.PRIV_TYPE, B.PARENT_PRIV_CODE) WHEN MATCHED THEN UPDATE SET "
			+ " A.SYS_NO = B.SYS_NO, A.NAME = B.NAME, A.NAME2 = B.NAME2, A.PRIV_TYPE = B.PRIV_TYPE, A.PARENT_PRIV_CODE = B.PARENT_PRIV_CODE~~";
	public static final String SQL_MERGE_OPER_PRIV = "MERGE INTO ADM_OPER_PRIV A USING (SELECT '{param1}' as OPER_CODE, '{param2}' as "
			+ " PRIV_CODE FROM DUAL) B ON (A.OPER_CODE = B.OPER_CODE and A.PRIV_CODE = B.PRIV_CODE) WHEN NOT MATCHED THEN INSERT "
			+ " (OPER_CODE, PRIV_CODE) VALUES (B.OPER_CODE, B.PRIV_CODE)~~";
	public static final String SQL_MERGE_CONFIG = "SELECT 'DECLARE BEGIN PK_GEN.MERGE_GEN_CONFIG (' || CASE WHEN company_code = '0' THEN '''0'', "
			+ " ' ELSE '' END || sys_no || ', ' || '''' || ITEM_CODE || ''', ' || '''' || ITEM_NAME || ''', ' || '''' || ITEM_NAME2 || ''', ' || '''' "
			+ " || ITEM_DESC || ''', ' || '''' || ITEM_DESC2 || ''', ' || ITEM_TYPE || ', ' || '''' || ITEM_MASK || ''', ' || ITEM_MIN || ', ' || "
			+ " ITEM_MAX || ', ' || READ_ONLY || ', ' || '''' || ITEM_VALUE || ''', ' || CAT_ID || ', ' || '''' || DICT_CODE || ''', ' || ORDER_NO || "
			+ " '); END;~~' as mrg_query   FROM gen_config  WHERE company_code IN (SELECT '0' AS company_code FROM DUAL UNION ALL SELECT MIN (company_code) "
			+ " company_code FROM gen_company WHERE company_code <> '0') AND sys_no = {system}";

	public static final String SQL_MERGE_PROD_TYPE = "select 'MERGE INTO BCOM_PROD_TYPE A USING (SELECT ' || '''' || PROD_TYPE || ''' AS PROD_TYPE, ' "
			+ "|| SYS_NO  || ' AS SYS_NO, '    || '''' || NAME || ''' AS NAME, ' || '''' || NAME2 || ''' AS NAME2, ' || '''' || PRINC_BAL_TYPE_CODE "
			+ "|| ''' AS PRINC_BAL_TYPE_CODE, ' || '''' || INT_BAL_TYPE_CODE || ''' AS INT_BAL_TYPE_CODE, ' || '''' || P_FINE_BAL_TYPE_CODE "
			+ "|| ''' AS P_FINE_BAL_TYPE_CODE, ' || '''' || I_FINE_BAL_TYPE_CODE || ''' AS I_FINE_BAL_TYPE_CODE ' || 'FROM DUAL) B ON "
			+ "(A.PROD_TYPE = B.PROD_TYPE) WHEN NOT MATCHED THEN INSERT (PROD_TYPE, SYS_NO, NAME, NAME2, PRINC_BAL_TYPE_CODE, INT_BAL_TYPE_CODE, "
			+ "P_FINE_BAL_TYPE_CODE, I_FINE_BAL_TYPE_CODE) VALUES (B.PROD_TYPE, B.SYS_NO, B.NAME, B.NAME2, B.PRINC_BAL_TYPE_CODE, B.INT_BAL_TYPE_CODE, "
			+ "B.P_FINE_BAL_TYPE_CODE, B.I_FINE_BAL_TYPE_CODE)~~' mrg_query from BCOM_PROD_TYPE where sys_no = {system}";
	public static final String SQL_MERGE_BAL_TYPES = "select 'MERGE INTO BCOM_BAL_TYPES A USING (SELECT ' || '''' || PROD_TYPE || ''' AS PROD_TYPE, ' "
			+ "|| '''' || BAL_TYPE_CODE || ''' AS BAL_TYPE_CODE, ' || '''' || NAME || ''' AS NAME, ' || '''' || NAME2 || ''' AS NAME2, ' || '''' "
			+ "|| WITH_GL || ''' AS WITH_GL, ' || '''' || CALC_INT || ''' AS CALC_INT, ' || '''' || BAL_CHAR || ''' AS BAL_CHAR, ' || SYS_NO  "
			+ "|| ' AS SYS_NO, ' || IS_OFFSET  || ' AS IS_OFFSET, ' || '''' || ON_OFF || ''' AS ON_OFF, ' || IN_CUST_STMT  || ' AS IN_CUST_STMT, ' "
			+ "|| IN_BANK_STMT  || ' AS IN_BANK_STMT, ' || TOTAL_CUST_STMT  || ' AS TOTAL_CUST_STMT, ' || TOTAL_BANK_STMT  || ' AS TOTAL_BANK_STMT, ' "
			+ "|| IN_SLIP  || ' AS IN_SLIP, ' || '''' || SPECIAL_BAL_TYPE || ''' AS SPECIAL_BAL_TYPE, ' || '''' || DEPR_OPT || ''' AS DEPR_OPT, ' "
			+ "|| '''' || DEPR_DEST_BAL_TYPE_CODE || ''' AS DEPR_DEST_BAL_TYPE_CODE, ' || '''' || DEPR_CALC_BAL_TYPE_CODE || ''' AS DEPR_CALC_BAL_TYPE_CODE, ' "
			+ "|| '''' || RATE_TYPE || ''' AS RATE_TYPE, ' || ALLOW_NEG_BAL  || ' AS ALLOW_NEG_BAL, ' || HAS_VBAL  || ' AS HAS_VBAL, ' || '''' "
			+ "|| FINE_INT_TYPE_CODE || ''' AS FINE_INT_TYPE_CODE, ' || '''' || FINE_BAL_TYPE_CODE || ''' AS FINE_BAL_TYPE_CODE, ' || REVAL_OPT  "
			+ "|| ' AS REVAL_OPT, ' || '''' || DEP_PROC_TYPE || ''' AS DEP_PROC_TYPE ' || 'FROM DUAL) B ON (A.PROD_TYPE = B.PROD_TYPE and "
			+ "A.BAL_TYPE_CODE = B.BAL_TYPE_CODE) WHEN NOT MATCHED THEN INSERT (PROD_TYPE, BAL_TYPE_CODE, NAME, NAME2, WITH_GL, CALC_INT, "
			+ "BAL_CHAR, SYS_NO, IS_OFFSET, ON_OFF, IN_CUST_STMT, IN_BANK_STMT, TOTAL_CUST_STMT, TOTAL_BANK_STMT, IN_SLIP, SPECIAL_BAL_TYPE, "
			+ "DEPR_OPT, DEPR_DEST_BAL_TYPE_CODE, DEPR_CALC_BAL_TYPE_CODE, RATE_TYPE, ALLOW_NEG_BAL, HAS_VBAL, FINE_INT_TYPE_CODE, FINE_BAL_TYPE_CODE, "
			+ "REVAL_OPT, DEP_PROC_TYPE) VALUES (B.PROD_TYPE, B.BAL_TYPE_CODE, B.NAME, B.NAME2, B.WITH_GL, B.CALC_INT, B.BAL_CHAR, B.SYS_NO, B.IS_OFFSET, "
			+ "B.ON_OFF, B.IN_CUST_STMT, B.IN_BANK_STMT, B.TOTAL_CUST_STMT, B.TOTAL_BANK_STMT, B.IN_SLIP, B.SPECIAL_BAL_TYPE, B.DEPR_OPT, B.DEPR_DEST_BAL_TYPE_CODE, "
			+ "B.DEPR_CALC_BAL_TYPE_CODE, B.RATE_TYPE, B.ALLOW_NEG_BAL, B.HAS_VBAL, B.FINE_INT_TYPE_CODE, B.FINE_BAL_TYPE_CODE, B.REVAL_OPT, B.DEP_PROC_TYPE)~~' "
			+ "as mrg_query from BCOM_BAL_TYPES where sys_no = {system}";
	public static final String SQL_MERGE_TXN_CODE = "SELECT 'MERGE INTO BCOM_TXN_CODE A USING (SELECT ' || SYS_NO || ' AS SYS_NO, ' || '''' || PROD_TYPE || ''' AS "
			+ "PROD_TYPE, ' || '''' || TXN_CODE || ''' AS TXN_CODE, ' || '''' || NAME || ''' AS NAME, ' || '''' || NAME2 || ''' AS NAME2, ' || '''' || TXN_TYPE "
			+ "|| ''' AS TXN_TYPE, ' || '''' || TXN_GROUP || ''' AS TXN_GROUP, ' || IN_CUST_STMT || ' AS IN_CUST_STMT, ' || IN_BANK_STMT || ' AS IN_BANK_STMT ' "
			+ "|| 'FROM DUAL) B ON (A.OPER_CODE = B.OPER_CODE) WHEN NOT MATCHED THEN INSERT (OPER_CODE, NAME, NAME2, STATUS, SYS_NO, CAT_CODE) VALUES (B.OPER_CODE, "
			+ "B.NAME, B.NAME2, B.STATUS, B.SYS_NO, B.CAT_CODE)~~' MRG_QUERY FROM BCOM_TXN_CODE WHERE SYS_NO = {system}";
	public static final String SQL_MERGE_BAL_TYPE = "SELECT 'MERGE INTO BCOM_BAL_TYPE A USING (SELECT ' || '''' || BAL_TYPE_CODE || ''' AS BAL_TYPE_CODE, ' || '''' "
			+ "|| NAME || ''' AS NAME, ' || '''' || NAME2 || ''' AS NAME2, ' || SYS_NO || ' AS SYS_NO, ' || '''' || WITH_GL || ''' AS WITH_GL, ' || '''' || CALC_INT "
			+ "|| ''' AS CALC_INT, ' || '''' || BAL_CHAR || ''' AS BAL_CHAR, ' || '''' || BAL_CHAR1 || ''' AS BAL_CHAR1 ' || 'FROM DUAL) B ON (A.BAL_TYPE_CODE = "
			+ "B.BAL_TYPE_CODE) WHEN NOT MATCHED THEN INSERT (BAL_TYPE_CODE, NAME, NAME2, SYS_NO, WITH_GL, CALC_INT, BAL_CHAR, BAL_CHAR1) VALUES (B.BAL_TYPE_CODE, "
			+ "B.NAME, B.NAME2, B.SYS_NO, B.WITH_GL, B.CALC_INT, B.BAL_CHAR, B.BAL_CHAR1)~~' MRG_QUERY FROM BCOM_BAL_TYPE WHERE SYS_NO = {system}";
	public static final String SQL_MERGE_INT = "SELECT 'MERGE INTO BCOM_INT A USING (SELECT ' || '''' || PROD_TYPE || ''' AS PROD_TYPE, ' || '''' || INT_TYPE_CODE "
			+ "|| ''' AS INT_TYPE_CODE, ' || '''' || TYPE || ''' AS TYPE, ' || '''' || NAME || ''' AS NAME, ' || '''' || NAME2 || ''' AS NAME2, ' || '''' || "
			+ "DEST_BAL_TYPE || ''' AS DEST_BAL_TYPE, ' || '''' || DEST_CONT_BAL_TYPE || ''' AS DEST_CONT_BAL_TYPE, ' || '''' || MAIN_INT || ''' AS MAIN_INT, ' "
			+ "|| SYS_NO || ' AS SYS_NO, ' || ORDER_NO || ' AS ORDER_NO ' || 'FROM DUAL) B ON (A.INT_TYPE_CODE = B.INT_TYPE_CODE and A.PROD_TYPE = B.PROD_TYPE) "
			+ "WHEN NOT MATCHED THEN INSERT (PROD_TYPE, INT_TYPE_CODE, TYPE, NAME, DEST_BAL_TYPE, DEST_CONT_BAL_TYPE, MAIN_INT, SYS_NO, NAME2, ORDER_NO) VALUES "
			+ "(B.PROD_TYPE, B.INT_TYPE_CODE, B.TYPE, B.NAME, B.DEST_BAL_TYPE, B.DEST_CONT_BAL_TYPE, B.MAIN_INT, B.SYS_NO, B.NAME2, B.ORDER_NO)~~' MRG_QUERY "
			+ "FROM BCOM_INT WHERE SYS_NO = {system}";
	public static final String SQL_MERGE_STMT_EXCLUSION = "SELECT 'MERGE INTO BCOM_STMT_EXCLUSION A USING (SELECT ' || '''' || A.PROD_TYPE || ''' AS PROD_TYPE, ' "
			+ "|| '''' || A.TXN_CODE || ''' AS TXN_CODE, ' || '''' || A.BAL_TYPE_CODE || ''' AS BAL_TYPE_CODE, ' || A.EXCLUDE_CUST_STMT || ' AS EXCLUDE_CUST_STMT, ' "
			+ "|| A.EXCLUDE_BANK_STMT || ' AS EXCLUDE_BANK_STMT ' || 'FROM DUAL) B ON (A.PROD_TYPE = B.PROD_TYPE and A.TXN_CODE = B.TXN_CODE and A.BAL_TYPE_CODE = "
			+ "B.BAL_TYPE_CODE) WHEN NOT MATCHED THEN INSERT ( PROD_TYPE, TXN_CODE, BAL_TYPE_CODE, EXCLUDE_CUST_STMT, EXCLUDE_BANK_STMT) VALUES (B.PROD_TYPE, "
			+ "B.TXN_CODE, B.BAL_TYPE_CODE, B.EXCLUDE_CUST_STMT, B.EXCLUDE_BANK_STMT)~~' MRG_QUERY FROM BCOM_STMT_EXCLUSION A LEFT JOIN BCOM_PROD_TYPE B ON "
			+ "B.PROD_TYPE = A.PROD_TYPE WHERE B.SYS_NO = {system}";
	public static final String SQL_MERGE_CLS = "SELECT 'MERGE INTO BCOM_CLS A USING (SELECT ' || CLASS_NO || ' AS CLASS_NO, ' || 'COMPANY_CODE, ' || SYS_NO "
			+ "|| ' AS SYS_NO, ' || '''' || NAME || ''' AS NAME, ' || '''' || NAME2 || ''' AS NAME2 ' || 'FROM GEN_COMPANY) B ON (A.CLASS_NO = B.CLASS_NO and "
			+ "A.COMPANY_CODE = B.COMPANY_CODE and A.SYS_NO = B.SYS_NO) WHEN NOT MATCHED THEN INSERT (CLASS_NO, COMPANY_CODE, SYS_NO, NAME, NAME2) VALUES "
			+ "(B.CLASS_NO, B.COMPANY_CODE, B.SYS_NO, B.NAME, B.NAME2)~~' MRG_QUERY FROM BCOM_CLS WHERE SYS_NO = {system} AND COMPANY_CODE = '10'";

	public static final String SQL_MERGE_PL_OPER = "select 'MERGE INTO PL_OPER A USING (SELECT ' || '''' || OPER_CODE || ''' AS OPER_CODE, ' || '''' || NAME || "
			+ "''' AS NAME, ' || '''' || NAME2 || ''' AS NAME2, ' || STATUS || ' AS STATUS, ' || SYS_NO || ' AS SYS_NO, ' || '''' || CAT_CODE || ''' AS "
			+ "CAT_CODE ' || 'FROM DUAL) B ON (A.OPER_CODE = B.OPER_CODE) WHEN NOT MATCHED THEN INSERT (OPER_CODE, NAME, NAME2, STATUS, SYS_NO, CAT_CODE) "
			+ "VALUES (B.OPER_CODE, B.NAME, B.NAME2, B.STATUS, B.SYS_NO, B.CAT_CODE)~~' mrg_query from PL_OPER where sys_no = {system}";
	public static final String SQL_MERGE_NMW_OPER_COL = "SELECT 'MERGE INTO NMW_OPER_COL A USING (SELECT ' || '''' || OPER_CODE || ''' AS OPER_CODE, ' || '''' "
			+ "|| COL_NAME || ''' AS COL_NAME, ' || '''' || NAME || ''' AS NAME, ' || '''' || NAME2 || ''' AS NAME2, ' || COL_TYPE || ' AS COL_TYPE, ' || 0 || ' "
			+ "AS CHK_BL, ' || '''' || CHK_BL_TYPE || ''' AS CHK_BL_TYPE, ' || ORDER_NO || ' AS ORDER_NO, ' || CREATED_BY || ' AS CREATED_BY, ' || 'SYSDATE' || ' "
			+ "AS CREATED_DATETIME, ' || MODIFIED_BY || ' AS MODIFIED_BY, ' || 'SYSDATE' || ' AS MODIFIED_DATETIME' || ' FROM DUAL) B ON (A.OPER_CODE = "
			+ "B.OPER_CODE ' || 'and A.COL_NAME = B.COL_NAME) WHEN NOT MATCHED ' || 'THEN INSERT (OPER_CODE, COL_NAME, NAME, NAME2, ' || 'COL_TYPE, CHK_BL, "
			+ "CHK_BL_TYPE, ORDER_NO, ' || 'CREATED_BY, CREATED_DATETIME, MODIFIED_BY, ' || 'MODIFIED_DATETIME) VALUES (B.OPER_CODE, B.COL_NAME, ' || 'B.NAME, "
			+ "B.NAME2, B.COL_TYPE, B.CHK_BL, B.CHK_BL_TYPE, ' || 'B.ORDER_NO, B.CREATED_BY, B.CREATED_DATETIME, ' || 'B.MODIFIED_BY, B.MODIFIED_DATETIME)~~' "
			+ "mrg_query FROM NMW_OPER_COL WHERE OPER_CODE LIKE '{system}%'";
	public static final String SQL_MERGE_NMW_OPERATIONS = "select 'MERGE INTO NMW_OPERATIONS A USING (SELECT ' || '''' || OPER_CODE || ''' AS OPER_CODE, ' "
			+ "|| '''' || SOURCE_TYPE || ''' AS SOURCE_TYPE, ' || '''' || SCR_CODE || ''' AS SCR_CODE, ' || '''' || MAIN_SCR_CODE || ''' AS MAIN_SCR_CODE ' "
			+ "|| 'FROM DUAL) B ON (A.OPER_CODE = B.OPER_CODE) WHEN NOT MATCHED THEN INSERT (OPER_CODE, SOURCE_TYPE, SCR_CODE, MAIN_SCR_CODE) VALUES (B.OPER_CODE, "
			+ "B.SOURCE_TYPE, B.SCR_CODE, B.MAIN_SCR_CODE)~~' mrg_query from NMW_OPERATIONS where oper_code like '{system}%'";

	private Const() {
	}
}
