--------------------------------------------------------
--  File created - Friday-March-30-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table CODE_DECODE
--------------------------------------------------------

  CREATE TABLE "QLDV_CODE_DECODE" ("CODE_GROUP" VARCHAR2(30), "CODE" VARCHAR2(50), "DECODE" VARCHAR2(200), "PARENT_CODE" VARCHAR2(50), "CODE_LEVEL" NUMBER, "NOTE" VARCHAR2(200), "REF_DATA" VARCHAR2(500), "STATUS" NUMBER) ;

   COMMENT ON COLUMN "QLDV_CODE_DECODE"."CODE_GROUP" IS 'Nhóm loại code';
   COMMENT ON COLUMN "QLDV_CODE_DECODE"."CODE" IS 'Mã code';
   COMMENT ON COLUMN "QLDV_CODE_DECODE"."DECODE" IS 'Giá trị code';
   COMMENT ON COLUMN "QLDV_CODE_DECODE"."PARENT_CODE" IS 'Mã code cấp trên (nếu có)';
   COMMENT ON COLUMN "QLDV_CODE_DECODE"."CODE_LEVEL" IS 'Cấp (đánh số từ 1 với các code dạng cây)';
   COMMENT ON COLUMN "QLDV_CODE_DECODE"."NOTE" IS 'Ghi chú';
   COMMENT ON COLUMN "QLDV_CODE_DECODE"."REF_DATA" IS 'Dữ liệu tham chiếu';
   COMMENT ON COLUMN "QLDV_CODE_DECODE"."STATUS" IS 'Trạng thái';
   COMMENT ON TABLE "QLDV_CODE_DECODE"  IS 'Danh sách config dùng chung';
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('RANK_GR','HDLD','HĐLĐ',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('RANK_GR','CNVQP','CNVQP',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('RANK_GR','QNCN','QNCN',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('RANK_GR','SQ','SQ',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('STATUS_EMPLOY','1','Hoạt động',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('STATUS_EMPLOY','0','Không hoạt động',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('PARTNER_TYPE','DTVN','Đối tác Việt Nam',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('PARTNER_TYPE','DTNN','Đối tác nước ngoài',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('PARTNER_TYPE','LDPT','Lao động phổ thông',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_TYPE','DK','Cấp tập đoàn',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_TYPE','DXCTT','Cấp VTNET',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_TYPE','DXKTT','Đột xuất không tờ trình',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_STATUS','DT','Dự thảo',null,1,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_STATUS','DGBGĐV','Đã gửi ban giám đốc đơn vị',null,2,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_STATUS','ĐVĐD','Đơn vị đã duyệt',null,3,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_STATUS','ĐVTCD','Đơn vị từ chối duyệt',null,4,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_STATUS','ĐDL1','KTTC đã duyệt',null,5,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_STATUS','TCDL1','KTTC từ chối',null,6,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_STATUS','ĐDL2','KSNB đã duyệt',null,7,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_STATUS','TCDL2','KSNB từ chối ',null,8,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('SMS','SMS_GATEWAY_ID','5',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('SMS','APP_CONTENT','Doan [TEN_DOAN] da duoc gui toi d/c de phe duyet, de nghi d/c kiem tra xu ly',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('SMS','REJECT_CONTENT','Doan [TEN_DOAN] da bi reject, de nghi d/c kiem tra xu ly',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('UNION_STATUS','DHT','Đã cấp tờ trình',null,9,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('REGENCY','NV','Nhân viên',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('REGENCY','TP','Trưởng phòng',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('REGENCY','TT_PDL','Trạm trưởng TTr PĐL',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('REGENCY','TT_HHT','Trạm trưởng TTr HHT',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('REGENCY','TT_PLM','Trạm trưởng TTr PLM',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('REGENCY','TT_NTH','Trạm trưởng TTr NTH',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('REGENCY','TT_PVN','Trạm trưởng TTr PVN',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('PERMISSION_APPR','0','Duyệt cấp đơn vị',null,1,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('PERMISSION_APPR','1','Duyệt cấp KTTC',null,2,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('PERMISSION_APPR','2','Duyệt cấp KSNB',null,3,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('SIGN_STATUS','DRAFF','Dự thảo',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('SIGN_STATUS','REJECT','Từ chối',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('SIGN_STATUS','SUBMITTED','Đã trình ký',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('SIGN_STATUS','ISSUED','Đã ban hành',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('MEMBER_TYPE','VN_VN','Người Việt làm đối tác Việt Nam',null,1,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('MEMBER_TYPE','VN_NN','Người Việt làm đối tác nước ngoài',null,2,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('MEMBER_TYPE','NN_NN','Người nước ngoài làm đối tác nước ngoài',null,3,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('MEMBER_TYPE','LDPT','Lao động phổ thông',null,4,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('NATIONAL','VN','Việt Nam',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('SMS','APP_COMFLETE','Doan [TEN_DOAN] da thong qua cap duyet cuoi cung',null,null,null,null,1);
Insert into QLDV_CODE_DECODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,STATUS) values ('NATIONAL','LAO','Lào',null,null,null,null,1);
COMMIT;
--------------------------------------------------------
--  DDL for Index CODE_DECODE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "QLDV_CODE_DECODE_PK" ON "QLDV_CODE_DECODE" ("CODE_GROUP", "CODE");
--------------------------------------------------------
--  Constraints for Table CODE_DECODE
--------------------------------------------------------

  ALTER TABLE "QLDV_CODE_DECODE" MODIFY ("CODE_GROUP" NOT NULL ENABLE);
  ALTER TABLE "QLDV_CODE_DECODE" MODIFY ("CODE" NOT NULL ENABLE);
  ALTER TABLE "QLDV_CODE_DECODE" ADD CONSTRAINT "QLDV_CODE_DECODE_PK" PRIMARY KEY ("CODE_GROUP", "CODE");
