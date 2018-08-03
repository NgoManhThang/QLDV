--------------------------------------------------------
--  File created - Tuesday-July-31-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table BOC_UNIT_USER
--------------------------------------------------------

  CREATE TABLE "BOC_UNIT_USER" ("UNIT_USER_ID" NUMBER, "UNIT_ID" NUMBER, "USER_ID" NUMBER) 

   COMMENT ON COLUMN "BOC_UNIT_USER"."UNIT_USER_ID" IS 'Id của bảng'
   COMMENT ON COLUMN "BOC_UNIT_USER"."UNIT_ID" IS 'Id của đơn vị'
   COMMENT ON COLUMN "BOC_UNIT_USER"."USER_ID" IS 'Id của người dùng'
REM INSERTING into BOC_UNIT_USER
SET DEFINE OFF;
Insert into BOC_UNIT_USER (UNIT_USER_ID,UNIT_ID,USER_ID) values (1641,1,1341);
Insert into BOC_UNIT_USER (UNIT_USER_ID,UNIT_ID,USER_ID) values (1584,123,3);
Insert into BOC_UNIT_USER (UNIT_USER_ID,UNIT_ID,USER_ID) values (1462,70,1821);
Insert into BOC_UNIT_USER (UNIT_USER_ID,UNIT_ID,USER_ID) values (1583,1,1);
Insert into BOC_UNIT_USER (UNIT_USER_ID,UNIT_ID,USER_ID) values (1621,70,1473);
Insert into BOC_UNIT_USER (UNIT_USER_ID,UNIT_ID,USER_ID) values (1625,70,1983);
Insert into BOC_UNIT_USER (UNIT_USER_ID,UNIT_ID,USER_ID) values (1484,124,1977);
--------------------------------------------------------
--  DDL for Index BOC_UNIT_USER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "BOC_UNIT_USER_PK" ON "BOC_UNIT_USER" ("UNIT_USER_ID")
--------------------------------------------------------
--  Constraints for Table BOC_UNIT_USER
--------------------------------------------------------

  ALTER TABLE "BOC_UNIT_USER" MODIFY ("UNIT_USER_ID" NOT NULL ENABLE)
  ALTER TABLE "BOC_UNIT_USER" ADD CONSTRAINT "BOC_UNIT_USER_PK" PRIMARY KEY ("UNIT_USER_ID") USING INDEX  ENABLE
