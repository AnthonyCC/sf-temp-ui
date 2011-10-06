
CREATE OR REPLACE PROCEDURE mis.DELETE_TIMESLOT_EVENT
IS 
v_cnt number:=0 ; 
begin 

  for rec in (select id from mis.timeslot_event_hdr l where L.EVENT_DTM <trunc(sysdate-15))     loop 
    delete from mis.timeslot_event_dtl dtl where DTL.TIMESLOT_LOG_ID =rec.id ;
    v_cnt:=v_cnt+1 ; 
    if mod (v_cnt,10000) = 0 then 
      commit ; 
    end if ; 
   end loop ; 
   delete from mis.timeslot_event_hdr l where L.EVENT_DTM <trunc(sysdate-15);
   commit ; 
end ;
/
