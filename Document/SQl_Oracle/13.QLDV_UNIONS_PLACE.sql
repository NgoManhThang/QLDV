--------------------------------------------------------
--  File created - Friday-March-30-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table UNIONS_PLACE
--------------------------------------------------------

  CREATE TABLE "QLDV_UNIONS_PLACE" ("UNION_PLACE_ID" NUMBER, "UNION_ID" NUMBER, "PLACE_ID" NUMBER, "CREATE_USER" VARCHAR2(50), "CREATE_DATE" TIMESTAMP (6), "UPDATE_USER" VARCHAR2(50), "UPDATE_DATE" TIMESTAMP (6)) ;

   COMMENT ON COLUMN "QLDV_UNIONS_PLACE"."UNION_PLACE_ID" IS 'ID của bảng';
   COMMENT ON COLUMN "QLDV_UNIONS_PLACE"."UNION_ID" IS 'ID của bảng UNION';
   COMMENT ON COLUMN "QLDV_UNIONS_PLACE"."PLACE_ID" IS 'ID bảng địa điểm';
   COMMENT ON COLUMN "QLDV_UNIONS_PLACE"."CREATE_USER" IS 'Người tạo';
   COMMENT ON COLUMN "QLDV_UNIONS_PLACE"."CREATE_DATE" IS 'Ngày giờ tạo';
   COMMENT ON COLUMN "QLDV_UNIONS_PLACE"."UPDATE_USER" IS 'Người cập nhật';
   COMMENT ON COLUMN "QLDV_UNIONS_PLACE"."UPDATE_DATE" IS 'Ngày giờ cập nhật';
   COMMENT ON TABLE "QLDV_UNIONS_PLACE"  IS 'Danh sách địa điểm đoàn có thể đến';
--------------------------------------------------------
--  DDL for Index UNIONS_PLACE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "QLDV_UNIONS_PLACE_PK" ON "QLDV_UNIONS_PLACE" ("UNION_PLACE_ID");
--------------------------------------------------------
--  Constraints for Table UNIONS_PLACE
--------------------------------------------------------

  ALTER TABLE "QLDV_UNIONS_PLACE" MODIFY ("UNION_PLACE_ID" NOT NULL ENABLE);
  ALTER TABLE "QLDV_UNIONS_PLACE" MODIFY ("UNION_ID" NOT NULL ENABLE);
  ALTER TABLE "QLDV_UNIONS_PLACE" MODIFY ("PLACE_ID" NOT NULL ENABLE);
  ALTER TABLE "QLDV_UNIONS_PLACE" ADD CONSTRAINT "QLDV_UNIONS_PLACE_PK" PRIMARY KEY ("UNION_PLACE_ID") USING INDEX  ENABLE;
