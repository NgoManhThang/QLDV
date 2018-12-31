--------------------------------------------------------
--  File created - Friday-March-30-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table VOFFICE_SIGN_LOG
--------------------------------------------------------

  CREATE TABLE "QLDV_VOFFICE_SIGN_LOG" ("VO_SL_ID" NUMBER, "VO_SL_STATEMENT_ID" NUMBER, "VO_SL_TRANS_CODE" VARCHAR2(200), "VO_SL_RESULT_CODE" NUMBER, "VO_SL_RESULT_OBJ" VARCHAR2(2000), "VO_SL_CREATE_DATE" DATE) ;

   COMMENT ON COLUMN "QLDV_VOFFICE_SIGN_LOG"."VO_SL_ID" IS 'ID cua history';
   COMMENT ON COLUMN "QLDV_VOFFICE_SIGN_LOG"."VO_SL_STATEMENT_ID" IS 'Statement id';
   COMMENT ON COLUMN "QLDV_VOFFICE_SIGN_LOG"."VO_SL_TRANS_CODE" IS 'Transaction code gui sang Voffice';
   COMMENT ON COLUMN "QLDV_VOFFICE_SIGN_LOG"."VO_SL_RESULT_CODE" IS 'Result Code';
   COMMENT ON COLUMN "QLDV_VOFFICE_SIGN_LOG"."VO_SL_RESULT_OBJ" IS 'Data Voffice truyen sang';
   COMMENT ON COLUMN "QLDV_VOFFICE_SIGN_LOG"."VO_SL_CREATE_DATE" IS 'Ngay tao';
--------------------------------------------------------
--  DDL for Index VOFFICE_SIGN_LOG_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "QLDV_VOFFICE_SIGN_LOG_PK" ON "QLDV_VOFFICE_SIGN_LOG" ("VO_SL_ID");
--------------------------------------------------------
--  Constraints for Table VOFFICE_SIGN_LOG
--------------------------------------------------------

  ALTER TABLE "QLDV_VOFFICE_SIGN_LOG" MODIFY ("VO_SL_ID" NOT NULL ENABLE);
  ALTER TABLE "QLDV_VOFFICE_SIGN_LOG" MODIFY ("VO_SL_STATEMENT_ID" NOT NULL ENABLE);
  ALTER TABLE "QLDV_VOFFICE_SIGN_LOG" MODIFY ("VO_SL_TRANS_CODE" NOT NULL ENABLE);
  ALTER TABLE "QLDV_VOFFICE_SIGN_LOG" ADD CONSTRAINT "QLDV_VOFFICE_SIGN_LOG_PK" PRIMARY KEY ("VO_SL_ID") USING INDEX  ENABLE;
