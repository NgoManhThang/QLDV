--------------------------------------------------------
--  File created - Friday-March-30-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table PLACE
--------------------------------------------------------

  CREATE TABLE "QLDV_PLACE" ("PLACE_ID" NUMBER, "PLACE_CODE" VARCHAR2(50), "PLACE_NAME" VARCHAR2(200), "AREA" VARCHAR2(50), "DESCRIPTION" VARCHAR2(1000), "STATUS" NUMBER) ;

   COMMENT ON COLUMN "QLDV_PLACE"."PLACE_ID" IS 'ID bảng';
   COMMENT ON COLUMN "QLDV_PLACE"."PLACE_CODE" IS 'Mã địa điểm';
   COMMENT ON COLUMN "QLDV_PLACE"."PLACE_NAME" IS 'Tên địa điểm';
   COMMENT ON COLUMN "QLDV_PLACE"."AREA" IS 'Khu vực';
   COMMENT ON COLUMN "QLDV_PLACE"."DESCRIPTION" IS 'Ghi chú';
   COMMENT ON COLUMN "QLDV_PLACE"."STATUS" IS 'Trạng thái';
   COMMENT ON TABLE "QLDV_PLACE"  IS 'Danh sách địa điểm';
--------------------------------------------------------
--  DDL for Index PLACE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "QLDV_PLACE_PK" ON "QLDV_PLACE" ("PLACE_ID");
--------------------------------------------------------
--  Constraints for Table PLACE
--------------------------------------------------------

  ALTER TABLE "QLDV_PLACE" MODIFY ("PLACE_ID" NOT NULL ENABLE);
  ALTER TABLE "QLDV_PLACE" ADD CONSTRAINT "QLDV_PLACE_PK" PRIMARY KEY ("PLACE_ID") USING INDEX  ENABLE;
