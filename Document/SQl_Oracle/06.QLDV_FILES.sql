--------------------------------------------------------
--  File created - Friday-March-30-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table FILES
--------------------------------------------------------

  CREATE TABLE "QLDV_FILES" ("FILE_ID" NUMBER, "GROUP_FILE" NUMBER, "FILE_NAME" VARCHAR2(200), "FILE_PATH" VARCHAR2(200), "CREATE_USER" VARCHAR2(50), "CREATE_DATE" TIMESTAMP (6), "GROUP_ID" NUMBER, "FILE_SIZE" NUMBER); 

   COMMENT ON COLUMN "QLDV_FILES"."FILE_ID" IS 'ID cua file';
   COMMENT ON COLUMN "QLDV_FILES"."GROUP_FILE" IS 'Loai nhom file: 1-Thông tin nhân sự -Đối tác cung cấp; 2- CMT/Hộ chiếu/Thẻ nhân viên; 3- Máy tính; 4- File trình ký; 5- Phụ lục cho trình ký;';
   COMMENT ON COLUMN "QLDV_FILES"."FILE_NAME" IS 'Ten file';
   COMMENT ON COLUMN "QLDV_FILES"."FILE_PATH" IS 'Duong dan file';
   COMMENT ON COLUMN "QLDV_FILES"."CREATE_USER" IS 'Nguoi tao';
   COMMENT ON COLUMN "QLDV_FILES"."CREATE_DATE" IS 'Ngay tao';
   COMMENT ON COLUMN "QLDV_FILES"."GROUP_ID" IS 'ID ban ghi';
   COMMENT ON COLUMN "QLDV_FILES"."FILE_SIZE" IS 'Size file';
--------------------------------------------------------
--  DDL for Index FILE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "QLDV_FILE_PK" ON "QLDV_FILES" ("FILE_ID");
--------------------------------------------------------
--  Constraints for Table FILES
--------------------------------------------------------

  ALTER TABLE "QLDV_FILES" MODIFY ("FILE_ID" NOT NULL ENABLE);
  ALTER TABLE "QLDV_FILES" ADD CONSTRAINT "QLDV_FILE_PK" PRIMARY KEY ("FILE_ID") USING INDEX  ENABLE;
  ALTER TABLE "QLDV_FILES" MODIFY ("GROUP_ID" NOT NULL ENABLE);
