CREATE OR REPLACE FUNCTION rows_2_column
(
    p_cursor sys_refcursor,
    p_del VARCHAR2 := ','
) RETURN VARCHAR2
IS
    l_value   VARCHAR2(32767);
    l_result  VARCHAR2(32767);
BEGIN
    LOOP
        FETCH p_cursor INTO l_value;
        EXIT WHEN p_cursor%NOTFOUND;
        IF l_result IS NOT NULL THEN
            l_result := l_result || p_del;
        END IF;
        l_result := l_result || l_value;
    END LOOP;
    RETURN l_result;
END rows_2_column;
/
show errors; 

GRANT EXECUTE ON CUST.rows_2_column TO FDSTORE_PRDA;
GRANT EXECUTE ON CUST.rows_2_column TO FDSTORE_PRDB;
GRANT EXECUTE ON CUST.rows_2_column TO APPDEV;
