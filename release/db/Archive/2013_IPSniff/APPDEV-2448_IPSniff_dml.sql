Insert into CUST.SS_SITE_FEATURE (ID,TITLE,PREZ_TITLE,PREZ_DESC,SMART_SAVING) values ('COS_HOME','Corporate FreshDirect Home',null,null,'0');

Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID,ARCHIVED) values ('cos-favorites-nocosfilter',null,'COS_HOME','scripted',null,'N');

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('cos-favorites-nocosfilter','exponent','0.5');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('cos-favorites-nocosfilter','generator','RecursiveNodes("fd_favs_cos")');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('cos-favorites-nocosfilter','prez_desc','These are just a few of our most-loved products.');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('cos-favorites-nocosfilter','prez_title','FRESHDIRECT FAVORITES');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('cos-favorites-nocosfilter','sampling_strat','power');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('cos-favorites-nocosfilter','scoring','Popularity_Discretized;QualityRating_Discretized2');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('cos-favorites-nocosfilter','top_perc','100');

Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C1',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C2',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C3',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C4',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C5',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C6',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C7',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C8',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C9',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C10',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C11',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C12',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C13',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C14',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C15',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C16',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C17',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C18',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C19',sysdate,'cos-favorites-nocosfilter');
Insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID,"DATE",VARIANT_ID) values ('C20',sysdate,'cos-favorites-nocosfilter');
