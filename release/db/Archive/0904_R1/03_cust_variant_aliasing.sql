alter table "SS_VARIANTS" add( ALIAS_ID VARCHAR2(16) );
alter table "SS_VARIANTS" add constraint SS_VARIANTS_ALIAS_FK foreign key("ALIAS_ID") references "SS_VARIANTS"("ID") ENABLE;
alter table "SS_VARIANTS" add constraint SS_VARIANTS_ALIAS check(( "TYPE" = 'alias' AND "ALIAS_ID" IS NOT NULL ) OR ( "TYPE" <> 'alias' AND "ALIAS_ID" IS NULL )) ENABLE;

