--------------------------------------------------------
--  File created - Friday-March-30-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table CONFIG_PROPERTY
--------------------------------------------------------

  CREATE TABLE "QLDV_CONFIG_PROPERTY" ("KEY" NVARCHAR2(200), "VALUE" NVARCHAR2(2000), "DESCRIPTION" VARCHAR2(2000));
Insert into QLDV_CONFIG_PROPERTY (KEY,VALUE,DESCRIPTION) values ('APP_PASS_VOFFICE','76-118-186-121-30-181-97-240-213-93-16-145-103-162-27-41-66-72-242-80-120-187-233-91-62-244-62-213-244-188-4-244','app pass voffice');
Insert into QLDV_CONFIG_PROPERTY (KEY,VALUE,DESCRIPTION) values ('APP_CODE_VOFFICE','81-178-81-79-100-51-59-153','app code voffice');
Insert into QLDV_CONFIG_PROPERTY (KEY,VALUE,DESCRIPTION) values ('FORM_DOCUMENT_VOFFICE','7','hinh thuc van ban cua voffice');
Insert into QLDV_CONFIG_PROPERTY (KEY,VALUE,DESCRIPTION) values ('REGISTER_VOFFICE','150916','ky hieu van ban cua voffice');
Insert into QLDV_CONFIG_PROPERTY (KEY,VALUE,DESCRIPTION) values ('SENDER_VOFFICE','VHKT_HC','Ten he thong day voffice');
Insert into QLDV_CONFIG_PROPERTY (KEY,VALUE,DESCRIPTION) values ('PERIOD_SIGN_VOFFICE','1','chu ky trinh ky');
Insert into QLDV_CONFIG_PROPERTY (KEY,VALUE,DESCRIPTION) values ('INPUT_VOFFICE','keyencryp','Ma hoa pass nhan vien cua voffice');
Insert into QLDV_CONFIG_PROPERTY (KEY,VALUE,DESCRIPTION) values ('AREA_VOFFICE','2','nganh cua voffice');
Insert into QLDV_CONFIG_PROPERTY (KEY,VALUE,DESCRIPTION) values ('URL_QLDV_VOFFICE',null,'url hoan cong');
Insert into QLDV_CONFIG_PROPERTY (KEY,VALUE,DESCRIPTION) values ('LENGTH_BARCODE','6','Độ dài barcode');
Insert into QLDV_CONFIG_PROPERTY (KEY,VALUE,DESCRIPTION) values ('TYPE_BARCODE','1','1-Toàn ký tự số 2-Toàn ký tự chữ 3-Cả ký tự số và chữ ');
commit;
--------------------------------------------------------
--  DDL for Index CONFIG_PROPERTY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "QLDV_CONFIG_PROPERTY_PK" ON "QLDV_CONFIG_PROPERTY" ("KEY");
--------------------------------------------------------
--  Constraints for Table CONFIG_PROPERTY
--------------------------------------------------------

  ALTER TABLE "QLDV_CONFIG_PROPERTY" ADD CONSTRAINT "QLDV_CONFIG_PROPERTY_PK" PRIMARY KEY ("KEY") USING INDEX  ENABLE;
  ALTER TABLE "QLDV_CONFIG_PROPERTY" MODIFY ("KEY" NOT NULL ENABLE);
