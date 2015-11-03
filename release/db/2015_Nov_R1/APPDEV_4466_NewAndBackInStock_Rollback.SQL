CREATE OR REPLACE procedure ERPS.refresh_new_and_back
is
begin
    DBMS_MVIEW.REFRESH('erps.new_products');
    DBMS_MVIEW.REFRESH('erps.back_in_stock_products');
end;
/