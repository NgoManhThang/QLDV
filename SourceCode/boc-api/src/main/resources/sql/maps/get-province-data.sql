SELECT DISTINCT 
  PD.PROVINCE_CODE provinceCode,
  PD.PROVINCE_VNAME provinceVname
FROM BOC_REGION_MAP PD
WHERE PD.PROVINCE_CODE = PD.DISTRICT_CODE AND PD.STATUS = 1 
AND (:p_unit_code is null or :p_unit_code = 'PB') or PD.PROVINCE_CODE = :p_unit_code
ORDER BY NLSSORT(PD.PROVINCE_VNAME, 'nls_sort = Vietnamese')