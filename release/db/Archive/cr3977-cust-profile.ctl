LOAD DATA
INFILE *
APPEND
INTO TABLE PROFILE
( 
email boundFILLER char(100),
customer_id position(1:16) char(16) "(select id from cust.fdcustomer where erp_customer_id=(select id from cust.customer where user_id=ltrim(rtrim(lower(:email)))))",
profile_type position(17:17) char(1) "(select 'S' from dual)",
profile_name position(18:57) char(40) "(select 'fourth_order_cos_survey' from dual)",
profile_value position(58:312) char(255) "(select 'SKIP' from dual)"
)
BEGINDATA
ROSEMARIE_G_SALAIR@ML.COM
chris.conforti@dowjones.com
jsantos@gjacademy.org
rhan@pclearn.com
muriel@ssquared.com
dspiegel@goldentree.com
leighmonet@hotmail.com
dmathiesen@riotting.com
leslie-ann.byfield@oracle.com
michelle@crownsuitesnyc.com
rachael_honowitz@timeinc.com
sean_ryan@timeinc.com
hwhipple@sterlingstamos.com
mtifrere@mcgarrybowen.com
sherna.lapid@dowjones.com
tluckenbill@company3.com
debra@thirdpoint.com
mary.young@dowjones.com
DanNet2000@aol.com
ATG9998@aol.com
angela.breed@permira.com
hyang@perrycap.com
C.Meirowitz@verizon.net
mwagman@digitas.com
robindhutson@yahoo.com
jheron@haverford.edu
michelle.mallon@turbochef.com
joel.russell@harkess-ord.com
hjmcdonald@gibney.com
afrancis@focuspointe.net
colleenr@sherinstitute.com
eabeleda@rockbay.com
jeanlouise@sbcglobal.net
thoelena@hotmail.com
rstout@westfield.com
SFortner@PutnamLovellNBF.com