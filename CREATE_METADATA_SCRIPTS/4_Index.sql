WITH X AS (SELECT AC1.TABLE_NAME, ac1.index_name, AC1.INDEX_TYPE, REPLACE ( LTRIM ( RTRIM ( REPLACE ( REPLACE ( REPLACE ( DBMS_LOB.SUBSTR ( DBMS_METADATA.get_ddl ('INDEX', ac1.index_name), 4000, 1), '"' || (SELECT SYS_CONTEXT ('userenv', 'current_schema') x FROM DUAL) || '".', ''), '"', ''), CHR (10), ''))), ';', '') DDLTXT FROM user_indexes ac1 WHERE ac1.table_name LIKE UPPER ('&&tableprefix') || '%') SELECT 'DECLARE ' || 'BEGIN IF (CHK_IX (''' || index_name || ''') <= 0) THEN ' || 'EXECUTE IMMEDIATE ''' || DDLTXT || ''';  END IF; END;~~' AS CREATESCRIPT  FROM X