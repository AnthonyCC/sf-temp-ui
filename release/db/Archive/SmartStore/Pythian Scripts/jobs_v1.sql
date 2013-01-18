DECLARE
  X NUMBER;
BEGIN
  SYS.DBMS_JOB.SUBMIT
    ( job       => X 
     ,what      => 'dbms_refresh.refresh(''SS_ANALYSIS.ORDERLINE_SNAP1'');'
     ,next_date => sysdate
     ,interval  => 'sysdate + 1/24 '
     ,no_parse  => TRUE
    );
END;
/

DECLARE
  X NUMBER;
BEGIN
  SYS.DBMS_JOB.SUBMIT
    ( job       => X 
     ,what      => 'dbms_refresh.refresh(''SS_ANALYSIS.ALL_NODES_SNAP1'');'
     ,next_date => sysdate
     ,interval  => 'sysdate + 1/24 '
     ,no_parse  => TRUE
    );
END;
/

DECLARE
  X NUMBER;
BEGIN
  SYS.DBMS_JOB.SUBMIT
    ( job       => X 
     ,what      => 'dbms_refresh.refresh(''SS_ANALYSIS.RELATIONSHIP_SNAP1'');'
     ,next_date => sysdate
     ,interval  => 'sysdate + 1/24 '
     ,no_parse  => TRUE
    );
END;
/

DECLARE
  X NUMBER;
BEGIN
  SYS.DBMS_JOB.SUBMIT
    ( job       => X 
     ,what      => 'dbms_refresh.refresh(''SS_ANALYSIS.SALE_SNAP1'');'
     ,next_date => sysdate
     ,interval  => 'sysdate + 1/24 '
     ,no_parse  => TRUE
    );
END;
/

DECLARE
  X NUMBER;
BEGIN
  SYS.DBMS_JOB.SUBMIT
    ( job       => X 
     ,what      => 'dbms_refresh.refresh(''SS_ANALYSIS.CONTENTNODE_SNAP1'');'
     ,next_date => sysdate
     ,interval  => 'sysdate + 1/24 '
     ,no_parse  => TRUE
    );
END;
/

DECLARE
  X NUMBER;
BEGIN
  SYS.DBMS_JOB.SUBMIT
    ( job       => X 
     ,what      => 'dbms_refresh.refresh(''SS_ANALYSIS.SALESACTION_SNAP1'');'
     ,next_date => sysdate
     ,interval  => 'sysdate + 1/24 '
     ,no_parse  => TRUE
    );
END;
/

commit;
