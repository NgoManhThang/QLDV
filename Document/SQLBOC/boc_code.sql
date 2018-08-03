--------------------------------------------------------
--  File created - Tuesday-July-31-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table BOC_CODE
--------------------------------------------------------

  CREATE TABLE "BOC_CODE" ("CODE_GROUP" VARCHAR2(30), "CODE" VARCHAR2(50), "DECODE" VARCHAR2(200), "PARENT_CODE" VARCHAR2(50), "CODE_LEVEL" NUMBER, "NOTE" VARCHAR2(200), "REF_DATA" VARCHAR2(255), "DIRECTION" VARCHAR2(20)) 

   COMMENT ON COLUMN "BOC_CODE"."CODE_GROUP" IS 'Nhóm loại code'
   COMMENT ON COLUMN "BOC_CODE"."CODE" IS 'Mã code'
   COMMENT ON COLUMN "BOC_CODE"."DECODE" IS 'Giá trị code'
   COMMENT ON COLUMN "BOC_CODE"."PARENT_CODE" IS 'Mã code cấp trên (nếu có)'
   COMMENT ON COLUMN "BOC_CODE"."CODE_LEVEL" IS 'Cấp (đánh số từ 1 với các code dạng cây)'
   COMMENT ON COLUMN "BOC_CODE"."NOTE" IS 'Ghi chú'
   COMMENT ON COLUMN "BOC_CODE"."REF_DATA" IS '1: Khối điện lực; 2: Khối cao thế;
3: 21 tỉnh + TT 
CNTT;
4: Khối điện lực và Khối cao thế'
   COMMENT ON COLUMN "BOC_CODE"."DIRECTION" IS 'Hướng target'
   COMMENT ON TABLE "BOC_CODE"  IS 'Các danh mục dùng trong BOC'
REM INSERTING into BOC_CODE
SET DEFINE OFF;
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','REVEUNE','Doanh thu',null,1,'1',null,null);
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','ELECTRIC_PAYABLE_RATE','Tỷ lệ thu tiền điện','REVEUNE',2,'2','1','1');
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','AVG_SELLING_PRICE','Giá bán bình quân','REVEUNE',2,'3','1','1');
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','LOSS_ELECTRIC','Tổn thất điện',null,1,'4',null,null);
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','ELECTRIC_COMMERCIAL','Điện thương phẩm','REVEUNE',2,'5','1','1');
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','CUSTOMER_CARE','Chăm sóc khách hàng',null,1,'6',null,null);
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','RATE_REQUEST_OVERDUE_PROCESSING','Tỷ lệ phiếu yêu cầu xử lý quá hạn','CUSTOMER_CARE',2,'7','3','-1');
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','RATE_REQUEST_MULTIPLE','Tỷ lệ phiếu yêu cầu nhiều lần','CUSTOMER_CARE',2,'8','3','-1');
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','LOSS_ELECTRIC_POWER','Chỉ tiêu tổn thất điện năng khối điện lực','LOSS_ELECTRIC',2,'9','4','-1');
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','LOSS_ELECTRIC_POWER_ACCUMULATED','Chỉ tiêu tổn thất điện năng lũy kế khối điện lực','LOSS_ELECTRIC',2,'10','4','-1');
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','TRUST_PROVIDE','Độ tin cậy cung cấp điện',null,1,'11',null,null);
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('POSITION','NV','Nhân viên',null,null,null,null,null);
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','TRUST_PROVIDE_SAIDI','Độ tin cậy cung cấp điện SAIDI khối cao thế','TRUST_PROVIDE',2,'12','4','-1');
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','NETWORK_PROBLEM_110','Suất sự cố mạng lưới 110kV',null,1,'13',null,null);
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','NETWORK_PROBLEM_110_1','Chỉ tiêu về suất sự cố đường dây 110kV kéo dài','NETWORK_PROBLEM_110',2,'14','2','-1');
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('POSITION','TP','Trưởng phòng',null,null,null,null,null);
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('BOC_GROUP','NETWORK_PROBLEM_110_3','Chỉ tiêu về suất sự cố trạm biến áp 110kV','NETWORK_PROBLEM_110',2,'15','2','-1');
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('POSITION','GD','Giám đốc',null,null,null,null,null);
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('COMPANY','CNTT','Trung tâm CNTT',null,null,null,null,null);
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('COMPANY','CTDL','Công ty điện lực',null,null,null,null,null);
Insert into BOC_CODE (CODE_GROUP,CODE,DECODE,PARENT_CODE,CODE_LEVEL,NOTE,REF_DATA,DIRECTION) values ('COMPANY','CCTLDCT','Công ty lưới điện cao thế',null,null,null,null,null);
--------------------------------------------------------
--  DDL for Index BOC_CODE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "BOC_CODE_PK" ON "BOC_CODE" ("CODE_GROUP", "CODE")
--------------------------------------------------------
--  DDL for Index BOC_CODE_CODE_GROUP
--------------------------------------------------------

  CREATE INDEX "BOC_CODE_CODE_GROUP" ON "BOC_CODE" ("CODE_GROUP")
--------------------------------------------------------
--  DDL for Index BOC_CODE_PARENT_CODE
--------------------------------------------------------

  CREATE INDEX "BOC_CODE_PARENT_CODE" ON "BOC_CODE" ("PARENT_CODE")
--------------------------------------------------------
--  Constraints for Table BOC_CODE
--------------------------------------------------------

  ALTER TABLE "BOC_CODE" MODIFY ("CODE_GROUP" NOT NULL ENABLE)
  ALTER TABLE "BOC_CODE" MODIFY ("CODE" NOT NULL ENABLE)
  ALTER TABLE "BOC_CODE" ADD CONSTRAINT "KPI_CODE_PK" PRIMARY KEY ("CODE_GROUP", "CODE") USING INDEX (CREATE UNIQUE INDEX "BOC_CODE_PK" ON "BOC_CODE" ("CODE_GROUP", "CODE") )  ENABLE
