WITH province_list as
(
  SELECT trim(regexp_substr(:p_province_code_list, '[^,]+', 1, LEVEL)) region_code
  FROM dual
  CONNECT BY LEVEL <= regexp_count(:p_province_code_list, ',')+1
),
district_list as
(
  SELECT trim(regexp_substr(:p_district_code_list, '[^,]+', 1, LEVEL)) region_code
  FROM dual
  CONNECT BY LEVEL <= regexp_count(:p_district_code_list, ',')+1
),
region_info as 
(
  SELECT DISTINCT
    CASE
      WHEN :p_province_code_list IS NULL THEN CL.PROVINCE_CODE
      WHEN :p_province_code_list IS NOT NULL AND :p_district_code_list IS NULL THEN CL.PROVINCE_CODE
      WHEN :p_province_code_list IS NOT NULL AND :p_district_code_list IS NOT NULL THEN CL.DISTRICT_CODE
    END regionCode,
    CASE
      WHEN :p_province_code_list IS NULL THEN CL.PROVINCE_NAME
      WHEN :p_province_code_list IS NOT NULL AND :p_district_code_list IS NULL THEN CL.PROVINCE_NAME
      WHEN :p_province_code_list IS NOT NULL AND :p_district_code_list IS NOT NULL THEN CL.DISTRICT_NAME
    END regionName
  FROM CATA_LOCATION CL
  WHERE
  	(CL.STATUS = 1 OR CL.STATUS = 2) AND 
    (:p_province_code_list IS NULL OR CL.PROVINCE_CODE IN (SELECT region_code FROM province_list))
    AND (:p_district_code_list IS NULL OR CL.DISTRICT_CODE IN (SELECT region_code FROM district_list))
)
SELECT 
  BT.TARGET_ID targetId,
  BT.TARGET_MONTH targetMonth,
  BT.TARGET_TYPE targetType,
  BT.REGION_CODE regionCode,
  BT.TARGET_NUM targetNum,
  BT.WARNING_1 warning1,
  BT.WARNING_2 warning2,
  BT.CREATED_USER_NAME createdUserName,
  BT.CREATED_DATE createdDate,
  RI.regionName regionName,
  BU.FULL_NAME createdFullName,
  BC.DECODE targetName,
  TO_CHAR(BT.CREATED_DATE, 'DD/MM/YYYY') createdDateString,
  BT.MONTH_YEAR monthYear
FROM BOC_TARGET BT
INNER JOIN BOC_CODE BC ON BC.CODE = BT.TARGET_TYPE
INNER JOIN region_info RI ON RI.regionCode = BT.REGION_CODE
LEFT JOIN BOC_USER BU ON BU.USER_NAME = BT.CREATED_USER_NAME
WHERE 1 = 1 