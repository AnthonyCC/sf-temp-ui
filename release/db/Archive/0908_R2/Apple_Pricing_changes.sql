
--Adding a new column 'DISPLAY_IND' to the table 'ERPS.SALESUNIT' related to Apple Pricing feature[APPDEV-209].

alter table ERPS.SALESUNIT add DISPLAY_IND char(1) default 'N' null;