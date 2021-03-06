grant SELECT on PERSONA_ID_SEQ to FDSTORE;
grant SELECT on DRAFT_CHANGE_SEQUENCE to FDSTORE;
grant SELECT on DRAFT_SEQUENCE to FDSTORE;

grant SELECT, INSERT, DELETE, UPDATE on Draft to FDSTORE;
grant SELECT, INSERT, DELETE, UPDATE on DraftChange to FDSTORE;
grant SELECT, INSERT, DELETE, UPDATE on Permission to FDSTORE;
grant SELECT, INSERT, DELETE, UPDATE on Persona to FDSTORE;
grant SELECT, INSERT, DELETE, UPDATE on Persona_Permission to FDSTORE;
grant SELECT, INSERT, DELETE, UPDATE on UserPersona to FDSTORE;

grant SELECT on PERSONA_ID_SEQ to FDSTORE_PRDA;
grant SELECT on DRAFT_CHANGE_SEQUENCE to FDSTORE_PRDA;
grant SELECT on DRAFT_SEQUENCE to FDSTORE_PRDA;

grant SELECT, INSERT, DELETE, UPDATE on Draft to FDSTORE_PRDA;
grant SELECT, INSERT, DELETE, UPDATE on DraftChange to FDSTORE_PRDA;
grant SELECT, INSERT, DELETE, UPDATE on Permission to FDSTORE_PRDA;
grant SELECT, INSERT, DELETE, UPDATE on Persona to FDSTORE_PRDA;
grant SELECT, INSERT, DELETE, UPDATE on Persona_Permission to FDSTORE_PRDA;
grant SELECT, INSERT, DELETE, UPDATE on UserPersona to FDSTORE_PRDA;

