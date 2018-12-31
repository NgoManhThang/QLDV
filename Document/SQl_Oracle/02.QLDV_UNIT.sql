--------------------------------------------------------
--  File created - Friday-March-30-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table UNIT
--------------------------------------------------------

  CREATE TABLE "QLDV_UNIT" ("UNIT_ID" NUMBER, "UNIT_NAME" VARCHAR2(200), "UNIT_CODE" VARCHAR2(50), "PARENT_UNIT_ID" NUMBER, "DESCRIPTION" VARCHAR2(1000), "STATUS" NUMBER) ;

   COMMENT ON COLUMN "QLDV_UNIT"."UNIT_ID" IS 'ID đơn vị';
   COMMENT ON COLUMN "QLDV_UNIT"."UNIT_NAME" IS 'Tên đơn vị';
   COMMENT ON COLUMN "QLDV_UNIT"."UNIT_CODE" IS 'Mã đơn vị';
   COMMENT ON COLUMN "QLDV_UNIT"."PARENT_UNIT_ID" IS 'ID đơn vị cha';
   COMMENT ON COLUMN "QLDV_UNIT"."DESCRIPTION" IS 'Ghi chú';
   COMMENT ON COLUMN "QLDV_UNIT"."STATUS" IS 'Trạng thái';
   COMMENT ON TABLE "QLDV_UNIT"  IS 'Danh sách đơn vị';
Insert into QLDV_UNIT (UNIT_ID,UNIT_NAME,UNIT_CODE,PARENT_UNIT_ID,DESCRIPTION,STATUS) values (421857,'Phòng Kỹ thuật VTNET','421857.KT_VTM',411343,null,1);
Insert into QLDV_UNIT (UNIT_ID,UNIT_NAME,UNIT_CODE,PARENT_UNIT_ID,DESCRIPTION,STATUS) values (421111,'Phòng Quản lý Chính sách','421111.QLCS-CNTT-VTNET',430479,null,1);
Insert into QLDV_UNIT (UNIT_ID,UNIT_NAME,UNIT_CODE,PARENT_UNIT_ID,DESCRIPTION,STATUS) values (430479,'Đội Quận 3 TT Mạng HCM','003_430479',443382,null,1);
Insert into QLDV_UNIT (UNIT_ID,UNIT_NAME,UNIT_CODE,PARENT_UNIT_ID,DESCRIPTION,STATUS) values (411347,'Phòng Cơ điện','411347.CD-QHTK-VTM',411343,null,1);
Insert into QLDV_UNIT (UNIT_ID,UNIT_NAME,UNIT_CODE,PARENT_UNIT_ID,DESCRIPTION,STATUS) values (430480,'DV1','DV1',430479,null,1);
Insert into QLDV_UNIT (UNIT_ID,UNIT_NAME,UNIT_CODE,PARENT_UNIT_ID,DESCRIPTION,STATUS) values (430481,'DV2','DV2',430479,null,1);
COMMIT;
--------------------------------------------------------
--  DDL for Index UNIT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "QLDV_UNIT_PK" ON "QLDV_UNIT" ("UNIT_ID");
--------------------------------------------------------
--  Constraints for Table UNIT
--------------------------------------------------------

  ALTER TABLE "QLDV_UNIT" MODIFY ("UNIT_ID" NOT NULL ENABLE);
  ALTER TABLE "QLDV_UNIT" ADD CONSTRAINT "QLDV_UNIT_PK" PRIMARY KEY ("UNIT_ID") USING INDEX  ENABLE;
