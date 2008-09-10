--connect to devint1 as sysdba

sqlplus / as sysdba

drop materialized view log on cms.contentnode;
create materialized view log on cms.contentnode;

drop materialized view log on cms.all_nodes;
create materialized view log on cms.all_nodes;

drop materialized view log on cms.relationship;
create materialized view log on cms.relationship;

drop materialized view log on cust.customer;
create materialized view log on cust.customer;

drop materialized view log on cust.sale;
create materialized view log on cust.sale;

drop materialized view log on cust.salesaction;
create materialized view log on cust.salesaction;

drop materialized view log on cust.orderline;
create materialized view log on cust.orderline;
