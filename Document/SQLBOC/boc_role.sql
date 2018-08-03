--------------------------------------------------------
--  File created - Tuesday-July-31-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table BOC_ROLE
--------------------------------------------------------

  CREATE TABLE "BOC_ROLE" ("ROLE_ID" NUMBER, "ROLE_NAME" VARCHAR2(200), "ROLE_CODE" VARCHAR2(200), "STATUS" NUMBER, "PARENT_ROLE_ID" VARCHAR2(20)) 

   COMMENT ON COLUMN "BOC_ROLE"."ROLE_ID" IS 'Id của quyền'
   COMMENT ON COLUMN "BOC_ROLE"."ROLE_NAME" IS 'Tên quyền'
   COMMENT ON COLUMN "BOC_ROLE"."ROLE_CODE" IS 'Mã quyền'
   COMMENT ON COLUMN "BOC_ROLE"."STATUS" IS 'Trạng thái'
   COMMENT ON COLUMN "BOC_ROLE"."PARENT_ROLE_ID" IS 'Id của quyền cha'
REM INSERTING into BOC_ROLE
SET DEFINE OFF;
Insert into BOC_ROLE (ROLE_ID,ROLE_NAME,ROLE_CODE,STATUS,PARENT_ROLE_ID) values (1,'Quản lý người dùng','MANAGER_USER',1,null);
Insert into BOC_ROLE (ROLE_ID,ROLE_NAME,ROLE_CODE,STATUS,PARENT_ROLE_ID) values (2,'Quản lý mục tiêu','MANAGER_TARGET',1,null);
Insert into BOC_ROLE (ROLE_ID,ROLE_NAME,ROLE_CODE,STATUS,PARENT_ROLE_ID) values (3,'Thêm người dùng','MANAGER_USER_ADD',1,'1');
Insert into BOC_ROLE (ROLE_ID,ROLE_NAME,ROLE_CODE,STATUS,PARENT_ROLE_ID) values (4,'Sửa người dùng','MANAGER_USER_EDIT',1,'1');
Insert into BOC_ROLE (ROLE_ID,ROLE_NAME,ROLE_CODE,STATUS,PARENT_ROLE_ID) values (5,'Xóa người dùng','MANAGER_USER_DELETE',1,'1');
Insert into BOC_ROLE (ROLE_ID,ROLE_NAME,ROLE_CODE,STATUS,PARENT_ROLE_ID) values (6,'Export mục tiêu','MANAGER_TARGET_EXPORT',1,'2');
Insert into BOC_ROLE (ROLE_ID,ROLE_NAME,ROLE_CODE,STATUS,PARENT_ROLE_ID) values (7,'Import mục tiêu','MANAGER_TARGET_IMPORT',1,'2');
Insert into BOC_ROLE (ROLE_ID,ROLE_NAME,ROLE_CODE,STATUS,PARENT_ROLE_ID) values (8,'Xóa mục tiêu','MANAGER_TARGET_DELETE',1,'2');
--------------------------------------------------------
--  DDL for Index BOC_ROLE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "BOC_ROLE_PK" ON "BOC_ROLE" ("ROLE_ID")
--------------------------------------------------------
--  Constraints for Table BOC_ROLE
--------------------------------------------------------

  ALTER TABLE "BOC_ROLE" MODIFY ("ROLE_ID" NOT NULL ENABLE)
  ALTER TABLE "BOC_ROLE" MODIFY ("STATUS" NOT NULL ENABLE)
  ALTER TABLE "BOC_ROLE" ADD CONSTRAINT "BOC_ROLE_PK" PRIMARY KEY ("ROLE_ID") USING INDEX  ENABLE
