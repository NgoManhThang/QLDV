SELECT DISTINCT
  CL.PROVINCE_CODE code,
  CL.PROVINCE_NAME decode
FROM CATA_LOCATION CL
WHERE (CL.STATUS = 1 OR CL.STATUS = 2)
AND :p_region_level = 0 
or (:p_region_level = 1 and :p_unit_code = CL.PROVINCE_CODE) 
or (:p_region_level = 2 and :p_unit_code = CL.DISTRICT_CODE)
order by NLSSORT(CL.PROVINCE_NAME, 'nls_sort = Vietnamese')