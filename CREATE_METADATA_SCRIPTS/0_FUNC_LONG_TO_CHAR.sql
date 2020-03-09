CREATE OR REPLACE FUNCTION LONG_TO_CHAR
     ( in_table_name varchar,
              in_column varchar2,
              in_column_name varchar2,
              in_tab_name varchar2)
  RETURN varchar AS
  text_c1 varchar2(32767);
  sql_cur varchar2(2000);
  begin
          sql_cur := 'select '||in_column||' from
          '||in_table_name||' where column_name = ' ||
          chr(39)||in_column_name||chr(39) ||' AND TABLE_NAME=' ||
          chr(39)||in_tab_name||chr(39); --1 AND ROWNUM = 1';
          dbms_output.put_line (sql_cur);
          execute immediate sql_cur into text_c1;
          text_c1 := substr(text_c1, 1, 4000);
          RETURN TEXT_C1;
  END;
  
  
  
