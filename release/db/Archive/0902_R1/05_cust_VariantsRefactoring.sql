ALTER TABLE "SS_VARIANTS" MODIFY "CONFIG_ID" VARCHAR2(16) NULL;

ALTER TABLE "SS_VARIANTS" ADD "TYPE" VARCHAR2(16) DEFAULT '';

UPDATE "SS_VARIANTS" "v" SET "TYPE" = (SELECT "TYPE" FROM "SS_SERVICE_CONFIGS" "sc" WHERE "v"."CONFIG_ID" = "sc"."ID");

ALTER TABLE "SS_VARIANTS" MODIFY "TYPE" VARCHAR2(16) DEFAULT '' NOT NULL;


