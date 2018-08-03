SELECT 
  BR.UNIT_ID unitId,
  BR.UNIT_CODE unitCode,
  BR.UNIT_NAME unitName,
  BR.STATUS status,
  BR.PARENT_UNIT_ID parentUnitId,
  (
    SELECT 
      case 
        when (CL.PROVINCE_CODE is null AND CL.DISTRICT_CODE is null) then '0'
        when (CL.DISTRICT_CODE is null) then '1'
        else '2'
      end regionLevel
    FROM CATA_LOCATION CL 
    WHERE (CL.KV_CODE = BR.UNIT_CODE AND CL.PROVINCE_CODE is null AND CL.DISTRICT_CODE is null) 
    or (CL.PROVINCE_CODE = BR.UNIT_CODE AND CL.DISTRICT_CODE is null) 
    or (CL.DISTRICT_CODE = BR.UNIT_CODE)
  ) regionLevel
FROM BOC_UNIT BR
INNER JOIN BOC_UNIT_USER BRU ON BRU.UNIT_ID = BR.UNIT_ID
INNER JOIN BOC_USER BU ON BU.USER_ID = BRU.USER_ID
WHERE BU.USER_NAME = :userName