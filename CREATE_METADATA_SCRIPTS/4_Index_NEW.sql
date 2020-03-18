WITH X AS (SELECT AC1.TABLE_NAME, ac1.index_name, AC1.INDEX_TYPE, REPLACE ( LTRIM ( RTRIM (REPLACE (REPLACE (REPLACE (DBMS_LOB.SUBSTR (DBMS_METADATA.get_ddl ('INDEX', ac1.index_name), 4000, 1), '"' || (SELECT SYS_CONTEXT ('userenv', 'current_schema') x FROM DUAL) || '".', ''), '"', ''), CHR (10), ''))), ';', '') DDLTXT, LISTAGG (col1.COLUMN_NAME, ',') WITHIN GROUP (ORDER BY col1.COLUMN_POSITION ASC) COLS FROM user_indexes ac1 JOIN USER_IND_COLUMNS col1 ON col1.index_name = ac1.index_name WHERE ac1.table_name LIKE UPPER ('&&tableprefix') || '%' GROUP BY AC1.TABLE_NAME, ac1.index_name, AC1.INDEX_TYPE) SELECT 'DECLARE ' || 'BEGIN IF (CHK_IX_WITH_COLS (''' || index_name || ''',''' || table_name || ''',''' || cols || ''') <= 0) THEN ' || 'EXECUTE IMMEDIATE ''' || SUBSTR(DDLTXT,1,CASE WHEN INSTR(DDLTXT,'PCTFREE') > 0 THEN INSTR(DDLTXT,'PCTFREE')-4 ELSE LENGTH(DDLTXT) END) || '''; END IF; END;~~' CREATESCRIPT FROM X