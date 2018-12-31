--------------------------------------------------------
--  File created - Friday-March-30-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table SIGN_PERSON
--------------------------------------------------------

  CREATE TABLE "QLDV_SIGN_PERSON" ("SIGN_PERSON_ID" NUMBER, "FULL_NAME" VARCHAR2(100), "UNIT_NAME" VARCHAR2(200), "SHOW_SIGN" NUMBER, "ISSUED" NUMBER, "STATEMENT_ID" NUMBER, "CODE" VARCHAR2(50), "ADORGID" VARCHAR2(50), "SIGNER_POSITION" NUMBER) ;

   COMMENT ON COLUMN "QLDV_SIGN_PERSON"."SIGN_PERSON_ID" IS 'ID của bảng';
   COMMENT ON COLUMN "QLDV_SIGN_PERSON"."FULL_NAME" IS 'Tên người ký';
   COMMENT ON COLUMN "QLDV_SIGN_PERSON"."UNIT_NAME" IS 'Đơn vị';
   COMMENT ON COLUMN "QLDV_SIGN_PERSON"."SHOW_SIGN" IS 'Hiển thị chữ ký (1 là hiển thị, 0 là không hiển thị)';
   COMMENT ON COLUMN "QLDV_SIGN_PERSON"."ISSUED" IS 'Ban hành (1 là hiển thị, 0 là không hiển thị)';
   COMMENT ON COLUMN "QLDV_SIGN_PERSON"."STATEMENT_ID" IS 'ID của bảng tờ trình';
   COMMENT ON COLUMN "QLDV_SIGN_PERSON"."CODE" IS 'Mã người ký';
   COMMENT ON COLUMN "QLDV_SIGN_PERSON"."ADORGID" IS 'Mã đơn vị của ng ký';
   COMMENT ON COLUMN "QLDV_SIGN_PERSON"."SIGNER_POSITION" IS 'Vị trí người kí';
--------------------------------------------------------
--  DDL for Index SIGN_PERSON_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "QLDV_SIGN_PERSON_PK" ON "QLDV_SIGN_PERSON" ("SIGN_PERSON_ID");
--------------------------------------------------------
--  Constraints for Table SIGN_PERSON
--------------------------------------------------------

  ALTER TABLE "QLDV_SIGN_PERSON" MODIFY ("SIGN_PERSON_ID" NOT NULL ENABLE);
  ALTER TABLE "QLDV_SIGN_PERSON" MODIFY ("STATEMENT_ID" NOT NULL ENABLE);
  ALTER TABLE "QLDV_SIGN_PERSON" ADD CONSTRAINT "QLDV_SIGN_PERSON_PK" PRIMARY KEY ("SIGN_PERSON_ID") USING INDEX  ENABLE;
