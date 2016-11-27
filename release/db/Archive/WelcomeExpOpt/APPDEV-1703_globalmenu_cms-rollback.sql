delete from RELATIONSHIPDESTINATION where RELATIONSHIPDEFINITION_ID like 'GlobalMenuSection%';
delete from RELATIONSHIPDESTINATION where RELATIONSHIPDEFINITION_ID like 'GlobalMenuItem%';
delete from RELATIONSHIPDESTINATION where CONTENTTYPE_ID = 'GlobalMenuItem';

delete from RELATIONSHIPDEFINITION where CONTENTTYPE_ID = 'GlobalMenuSection';
delete from RELATIONSHIPDEFINITION where CONTENTTYPE_ID = 'GlobalMenuItem';

delete from ATTRIBUTEDEFINITION where CONTENTTYPE_ID = 'GlobalMenuSection';
delete from ATTRIBUTEDEFINITION where CONTENTTYPE_ID = 'GlobalMenuItem';

delete from ATTRIBUTEDEFINITION where NAME like 'GLOBAL_MENU_%_LABEL';

delete from LOOKUP where LOOKUPTYPE_CODE = 'GlobalMenuItem.LAYOUT';

delete from LOOKUPTYPE where CODE = 'GlobalMenuItem.LAYOUT';

delete from CONTENTTYPE where ID = 'GlobalMenuSection';
delete from CONTENTTYPE where ID = 'GlobalMenuItem';

