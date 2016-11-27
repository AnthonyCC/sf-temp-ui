CREATE OR REPLACE procedure TRANSP.REFRESH_MVIEW(wt_name in varchar2) as
begin
DBMS_MVIEW.REFRESH(wt_name, 'A', '', TRUE, FALSE, 0,0,0, FALSE);
end;

GRANT EXECUTE ON TRANSP.REFRESH_MVIEW TO  fdtrn_stprd01;

