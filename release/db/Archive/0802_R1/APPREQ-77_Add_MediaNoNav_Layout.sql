---
--- [APPREQ-77]
---
--- Insert new entry to "LAYOUT" enum list
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('Product.LAYOUT','97','Media No Nav','Media No Nav',29);
-- Add MEDIA_CONTENT relationship to Product
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('MEDIA_CONTENT','Product.MEDIA_CONTENT','Product','T','F','F','Media Content','One');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('Product.MEDIA_CONTENT','Html',NULL,'Product.MEDIA_CONTENT.Html');
