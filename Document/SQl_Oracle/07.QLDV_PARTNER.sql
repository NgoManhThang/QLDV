--------------------------------------------------------
--  File created - Friday-March-30-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table PARTNER
--------------------------------------------------------

  CREATE TABLE "QLDV_PARTNER" ("PARTNER_ID" NUMBER, "PARTNER_NAME" VARCHAR2(200), "PARTNER_TYPE" VARCHAR2(50), "REPRESENT_NAME" VARCHAR2(200), "REPRESENT_PHONE" VARCHAR2(50), "CREATE_USER" VARCHAR2(50), "CREATE_DATE" TIMESTAMP (6), "UPDATE_USER" VARCHAR2(50), "UPDATE_DATE" TIMESTAMP (6)) ;

   COMMENT ON COLUMN "QLDV_PARTNER"."PARTNER_ID" IS 'ID của bảng';
   COMMENT ON COLUMN "QLDV_PARTNER"."PARTNER_NAME" IS 'Tên đối tác';
   COMMENT ON COLUMN "QLDV_PARTNER"."PARTNER_TYPE" IS 'Loại đối tác';
   COMMENT ON COLUMN "QLDV_PARTNER"."REPRESENT_NAME" IS 'Tên đại diện';
   COMMENT ON COLUMN "QLDV_PARTNER"."REPRESENT_PHONE" IS 'Số điện thoại đại diện';
   COMMENT ON COLUMN "QLDV_PARTNER"."CREATE_USER" IS 'Người tạo';
   COMMENT ON COLUMN "QLDV_PARTNER"."CREATE_DATE" IS 'Ngày giờ tạo';
   COMMENT ON COLUMN "QLDV_PARTNER"."UPDATE_USER" IS 'Người cập nhật';
   COMMENT ON COLUMN "QLDV_PARTNER"."UPDATE_DATE" IS 'Ngày giờ cập nhật';
   COMMENT ON TABLE "QLDV_PARTNER"  IS 'Danh sách đối tác';
--------------------------------------------------------
--  DDL for Index PARTNER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "QLDV_PARTNER_PK" ON "QLDV_PARTNER" ("PARTNER_ID");
--------------------------------------------------------
--  Constraints for Table PARTNER
--------------------------------------------------------

  ALTER TABLE "QLDV_PARTNER" MODIFY ("PARTNER_ID" NOT NULL ENABLE);
  ALTER TABLE "QLDV_PARTNER" ADD CONSTRAINT "QLDV_PARTNER_PK" PRIMARY KEY ("PARTNER_ID") USING INDEX  ENABLE;
