--Disconnect the exisitng all Beer and Wine Restrictions from the Municipality Info
DELETE FROM DLV.MUNICIPALITY_RESTRICTION_DATA;

--NY State Alcohol Restrictions
INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'RRN',
 'NY State Beer Restrictions',
 null,
 to_date('01/01/1970', 'MM/DD/YYYY'),
 to_date('01/01/2099 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'BER',
 'Beer not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );

INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER'  and NAME= 'NY State Beer Restrictions'),
 1,'06:30 AM','08:00 AM');
 
 INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'RRN',
 'NY State Wine Restrictions',
 null,
 to_date('01/01/1970', 'MM/DD/YYYY'),
 to_date('01/01/2099 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NY State Wine Restrictions'),
 1,'06:30 AM','12:00 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NY State Wine Restrictions'),
 1,'08:00 PM','11:30 PM');
 
 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NY State Wine Restrictions'),
 2,'06:30 AM','08:00 AM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NY State Wine Restrictions'),
 3,'06:30 AM','08:00 AM');
 
 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'NY State Wine Restrictions'),
 4,'06:30 AM','08:00 AM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NY State Wine Restrictions'),
 5,'06:30 AM','08:00 AM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'NY State Wine Restrictions'),
 6,'06:30 AM','08:00 AM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'NY State Wine Restrictions'),
 7,'06:30 AM','08:00 AM'); 

--NY State X-Mas Restriction
INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'OTR',
 'NY State X-Mas Day Wine Restrictions',
 null,
 to_date('12/25/2011', 'MM/DD/YYYY'),
 to_date('12/25/2011 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'NY State X-Mas Day Wine Restrictions'),
 0,'06:30 AM','11:30 PM'); 

--Create Mapping between municipality info and restriction tables.

 INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER' and NAME= 'NY State Beer Restrictions'));

 INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'NY State Wine Restrictions'));

 INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'NY State X-Mas Day Wine Restrictions'));

INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'RRN',
 'WESTCHESTER County Beer Restrictions',
 null,
 to_date('01/01/1970', 'MM/DD/YYYY'),
 to_date('01/01/2099 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'BER',
 'Beer not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );
  
  INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER'  and NAME= 'WESTCHESTER County Beer Restrictions'),
 1,'06:30 AM','08:00 AM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER'  and NAME= 'WESTCHESTER County Beer Restrictions'),
 2,'06:30 AM','08:00 AM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER'  and NAME= 'WESTCHESTER County Beer Restrictions'),
 3,'06:30 AM','08:00 AM');
 
 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES (DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER' and NAME= 'WESTCHESTER County Beer Restrictions'),
 4,'06:30 AM','08:00 AM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER'  and NAME= 'WESTCHESTER County Beer Restrictions'),
 5,'06:30 AM','08:00 AM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER' and NAME= 'WESTCHESTER County Beer Restrictions'),
 6,'06:30 AM','08:00 AM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER' and NAME= 'WESTCHESTER County Beer Restrictions'),
 7,'06:30 AM','08:00 AM'); 


INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'RRN',
 'WESTCHESTER County Wine Restrictions',
 null,
 to_date('01/01/1970', 'MM/DD/YYYY'),
 to_date('01/01/2099 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );
  
  INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'WESTCHESTER County Wine Restrictions'),
 1,'06:30 AM','12:00 PM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'WESTCHESTER County Wine Restrictions'),
 1,'09:00 PM','11:30 PM');
 
 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'WESTCHESTER County Wine Restrictions'),
 2,'06:30 AM','08:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'WESTCHESTER County Wine Restrictions'),
 2,'09:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'WESTCHESTER County Wine Restrictions'),
 3,'06:30 AM','08:00 AM');
 
    INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'WESTCHESTER County Wine Restrictions'),
 3,'09:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES (DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'WESTCHESTER County Wine Restrictions'),
 4,'06:30 AM','08:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'WESTCHESTER County Wine Restrictions'),
 4,'09:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'WESTCHESTER County Wine Restrictions'),
 5,'06:30 AM','08:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'WESTCHESTER County Wine Restrictions'),
 5,'09:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'WESTCHESTER County Wine Restrictions'),
 6,'06:30 AM','08:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'WESTCHESTER County Wine Restrictions'),
 6,'10:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'WESTCHESTER County Wine Restrictions'),
 7,'06:30 AM','08:00 AM'); 

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'WESTCHESTER County Wine Restrictions'),
 7,'10:00 PM','11:30 PM');

--WESCHESTER X-Mas Restriction
INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'OTR',
 'WESTCHESTER County X-Mas Day Wine Restrictions',
 null,
 to_date('12/25/2011', 'MM/DD/YYYY'),
 to_date('12/25/2011 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County X-Mas Day Wine Restrictions'),
 0,'06:30 AM','11:30 PM'); 

--WESCHESTER Week Day followed by X-Mas Restriction
INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'OTR',
 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions',
 null,
 to_date('12/26/2011', 'MM/DD/YYYY'),
 to_date('12/26/2011 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions'),
 2,'06:30 AM','08:00 AM'); 

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions'),
 2,'10:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions'),
 3,'06:30 AM','08:00 AM'); 

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions'),
 3,'10:00 PM','11:30 PM');
 
  INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions'),
 4,'06:30 AM','08:00 AM'); 

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions'),
 4,'10:00 PM','11:30 PM');
 
  INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions'),
 5,'06:30 AM','08:00 AM'); 

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions'),
 5,'10:00 PM','11:30 PM');
 
  INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions'),
 6,'06:30 AM','08:00 AM'); 

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions'),
 6,'10:00 PM','11:30 PM');
 
INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'WESTCHESTER' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER' and NAME= 'WESTCHESTER County Beer Restrictions'));

 INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'WESTCHESTER' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'WESTCHESTER County Wine Restrictions'));

 INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'WESTCHESTER' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County X-Mas Day Wine Restrictions'));

INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'WESTCHESTER' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'WESTCHESTER County Week Day after X-Mas Day Wine Restrictions'));
 
INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'RRN',
 'SUFFOLK County Beer Restrictions',
 null,
 to_date('01/01/1970', 'MM/DD/YYYY'),
 to_date('01/01/2099 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'BER',
 'Beer not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );
  
  INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER'  and NAME= 'SUFFOLK County Beer Restrictions'),
 1,'06:30 AM','08:00 AM');

INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'RRN',
 'SUFFOLK County Wine Restrictions',
 null,
 to_date('01/01/1970', 'MM/DD/YYYY'),
 to_date('01/01/2099 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );
  
  INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'SUFFOLK County Wine Restrictions'),
 1,'06:30 AM','12:00 PM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'SUFFOLK County Wine Restrictions'),
 1,'09:00 PM','11:30 PM');
 
 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'SUFFOLK County Wine Restrictions'),
 2,'06:30 AM','10:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'SUFFOLK County Wine Restrictions'),
 2,'09:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'SUFFOLK County Wine Restrictions'),
 3,'06:30 AM','10:00 AM');
 
    INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'SUFFOLK County Wine Restrictions'),
 3,'09:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES (DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'SUFFOLK County Wine Restrictions'),
 4,'06:30 AM','10:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'SUFFOLK County Wine Restrictions'),
 4,'09:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'SUFFOLK County Wine Restrictions'),
 5,'06:30 AM','10:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'SUFFOLK County Wine Restrictions'),
 5,'09:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'SUFFOLK County Wine Restrictions'),
 6,'06:30 AM','10:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'SUFFOLK County Wine Restrictions'),
 6,'10:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'SUFFOLK County Wine Restrictions'),
 7,'06:30 AM','10:00 AM'); 

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'SUFFOLK County Wine Restrictions'),
 7,'10:00 PM','11:30 PM');

--SUFFOLK X-Mas Restriction
INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'OTR',
 'SUFFOLK County X-Mas Day Wine Restrictions',
 null,
 to_date('12/25/2011', 'MM/DD/YYYY'),
 to_date('12/25/2011 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'SUFFOLK County X-Mas Day Wine Restrictions'),
 0,'06:30 AM','11:30 PM'); 

--SUFFOLK Entire Month of December Restrictions
INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'OTR',
 'SUFFOLK County December Month Wine Restrictions',
 null,
 to_date('12/01/2011', 'MM/DD/YYYY'),
 to_date('12/31/2011 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'SUFFOLK County December Month Wine Restrictions'),
 0,'06:30 AM','10:00 AM'); 

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'SUFFOLK County December Month Wine Restrictions'),
 0,'10:00 PM','11:30 PM'); 

-- SUFFOLK TKG EVE Restrictions
INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'OTR',
 'SUFFOLK County TKG EVE Wine Restrictions',
 null,
 to_date('11/23/2011', 'MM/DD/YYYY'),
 to_date('11/23/2011 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'SUFFOLK County TKG EVE Wine Restrictions'),
 0,'06:30 AM','10:00 AM'); 

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'SUFFOLK County TKG EVE Wine Restrictions'),
 0,'10:00 PM','11:30 PM'); 

  INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'SUFFOLK' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER' and NAME= 'SUFFOLK County Beer Restrictions'));

  INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'SUFFOLK' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'SUFFOLK County Wine Restrictions'));

  INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'SUFFOLK' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'SUFFOLK County X-Mas Day Wine Restrictions'));

  INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'SUFFOLK' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'SUFFOLK County December Month Wine Restrictions'));

  INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'SUFFOLK' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'SUFFOLK County TKG EVE Wine Restrictions'));

INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'RRN',
 'NASSAU County Beer Restrictions',
 null,
 to_date('01/01/1970', 'MM/DD/YYYY'),
 to_date('01/01/2099 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'BER',
 'Beer not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );
  
  INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER'  and NAME= 'NASSAU County Beer Restrictions'),
 1,'06:30 AM','08:00 AM');
 
 INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'RRN',
 'NASSAU County Wine Restrictions',
 null,
 to_date('01/01/1970', 'MM/DD/YYYY'),
 to_date('01/01/2099 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );
  
  INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NASSAU County Wine Restrictions'),
 1,'06:30 AM','12:00 PM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NASSAU County Wine Restrictions'),
 1,'09:00 PM','11:30 PM');
 
 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NASSAU County Wine Restrictions'),
 2,'06:30 AM','08:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NASSAU County Wine Restrictions'),
 2,'09:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NASSAU County Wine Restrictions'),
 3,'06:30 AM','08:00 AM');
 
    INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NASSAU County Wine Restrictions'),
 3,'09:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'NASSAU County Wine Restrictions'),
 4,'06:30 AM','08:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NASSAU County Wine Restrictions'),
 4,'09:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NASSAU County Wine Restrictions'),
 5,'06:30 AM','08:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NASSAU County Wine Restrictions'),
 5,'10:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'NASSAU County Wine Restrictions'),
 6,'06:30 AM','08:00 AM');

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NASSAU County Wine Restrictions'),
 6,'10:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'NASSAU County Wine Restrictions'),
 7,'06:30 AM','08:00 AM'); 

   INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN'  and NAME= 'NASSAU County Wine Restrictions'),
 7,'10:00 PM','11:30 PM');

--NASSAU X-Mas Restriction
INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'OTR',
 'NASSAU County X-Mas Day Wine Restrictions',
 null,
 to_date('12/25/2011', 'MM/DD/YYYY'),
 to_date('12/25/2011 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'NASSAU County X-Mas Day Wine Restrictions'),
 0,'06:30 AM','11:30 PM'); 

--NASSAU Week Day(Mon,Tues,Wed) followed by X-Mas Restriction
INSERT INTO DLV.RESTRICTED_DAYS (
   ID, TYPE, NAME, 
   DAY_OF_WEEK, START_TIME, END_TIME, 
   REASON, MESSAGE, CRITERION, 
   HOLIDAY_ID, DAYS_TO_ADD, MEDIA_PATH) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 'OTR',
 'NASSAU County Week Day after X-Mas Day Wine Restrictions',
 null,
 to_date('12/26/2011', 'MM/DD/YYYY'),
 to_date('12/26/2011 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS PM'),
 'WIN',
 'Wine not delivered for the selected delivery day and time',
 'DELIVERY',
  null,
  null,
  null );

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'NASSAU County Week Day after X-Mas Day Wine Restrictions'),
 2,'06:30 AM','08:00 AM'); 

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'NASSAU County Week Day after X-Mas Day Wine Restrictions'),
 2,'10:00 PM','11:30 PM');

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'NASSAU County Week Day after X-Mas Day Wine Restrictions'),
 3,'06:30 AM','08:00 AM'); 

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'NASSAU County Week Day after X-Mas Day Wine Restrictions'),
 3,'10:00 PM','11:30 PM');
 
  INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'NASSAU County Week Day after X-Mas Day Wine Restrictions'),
 4,'06:30 AM','08:00 AM'); 

 INSERT INTO DLV.RESTRICTION_DETAIL (
   ID, RESTRICTION_ID, DAY_OF_WEEK, 
   RES_START_TIME, RES_END_TIME) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'NASSAU County Week Day after X-Mas Day Wine Restrictions'),
 4,'10:00 PM','11:30 PM');

   INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'NASSAU' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='BER' and NAME= 'NASSAU County Beer Restrictions'));

   INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'NASSAU' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='RRN' and REASON='WIN' and NAME= 'NASSAU County Wine Restrictions'));

   INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'NASSAU' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'NASSAU County X-Mas Day Wine Restrictions'));

   INSERT INTO DLV.MUNICIPALITY_RESTRICTION_DATA (
   ID, MUNICIPALITY_ID, RESTRICTION_ID) 
VALUES ( DLV.SYSTEM_SEQ.nextval,
 (SELECT ID FROM DLV.MUNICIPALITY_INFO WHERE STATE='NY' and COUNTY = 'NASSAU' and CITY IS NULL),
 (SELECT ID FROM DLV.RESTRICTED_DAYS WHERE TYPE='OTR' and REASON='WIN' and NAME= 'NASSAU County Week Day after X-Mas Day Wine Restrictions'));
