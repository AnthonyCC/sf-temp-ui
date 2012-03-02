
CREATE TABLE ORDER_RATE_HOLIDAY
(
  DELIVERY_DATE  DATE
)

alter table mis.order_rate_holiday add PRIMARY KEY(delivery_date)
          

create new user called dashboard and give read access to the following tables

grant select on cust.sale to dashboard;

grant select on cust.salesaction to dashboard;

grant select on cust.deliveryinfo to dashboard;

grant select on mis.order_rate to dashboard;

grant select on transp.wave_instance to dashboard;

grant select on mis.order_rate_holiday to dashboard, appdev ; 

grant select on mis.bounce_event to dashboard;

grant select on mis.roll_event to dashboard;

grant select on mis.session_event to dashboard;
