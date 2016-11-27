DROP SYNONYM "CMS_DRAFT";
DROP SYNONYM "CMS_DRAFTCHANGE";
DROP SYNONYM "CMS_PERMISSION";
DROP SYNONYM "CMS_PERSONA";
DROP SYNONYM "CMS_PERSONA_PERMISSION";
DROP SYNONYM "CMS_USERPERSONA";

DROP TABLE "DRAFTCHANGE";
DROP TABLE "DRAFT";
DROP TABLE "PERSONA_PERMISSION";
DROP TABLE "USERPERSONA";
DROP TABLE "PERMISSION";
DROP TABLE "PERSONA";

drop sequence DRAFT_CHANGE_SEQUENCE;
drop sequence DRAFT_SEQUENCE;
drop sequence PERSONA_ID_SEQ;
