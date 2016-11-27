update cust.case_subject set cartons_req='X' where code in (
  select subject_code as code
  from cust.complaint_code where code in ('MISBOX','WRNBOX','MISFRZ','DAMBOX','RTLBWRBX')
)
