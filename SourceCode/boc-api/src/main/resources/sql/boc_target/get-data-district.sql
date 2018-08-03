WITH province_list as
(
  SELECT trim(regexp_substr(:p_province_code_list, '[^,]+', 1, LEVEL)) province_code
  FROM dual
  CONNECT BY LEVEL <= regexp_count(:p_province_code_list, ',')+1
)
SELECT DISTINCT
  CL.DISTRICT_CODE code,
  CL.DISTRICT_NAME decode
FROM CATA_LOCATION CL
WHERE CL.STATUS = 1 AND CL.DISTRICT_CODE IS NOT NULL AND CL.PROVINCE_CODE IN (SELECT province_code FROM province_list)
AND (:p_region_level = 0 or :p_region_level = 1 or (:p_region_level = 2 and :p_unit_code = CL.DISTRICT_CODE))
order by NLSSORT(CL.DISTRICT_NAME, 'nls_sort = Vietnamese')