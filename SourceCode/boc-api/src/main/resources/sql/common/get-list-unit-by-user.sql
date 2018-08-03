SELECT
  UNIT_ID unitId,
  UNIT_NAME unitName,
  UNIT_CODE unitCode,
  case 
    when :p_unit_code = UNIT_CODE then null
    else PARENT_UNIT_ID
  end parentUnitId,
  MAP_UNIT_ID mapUnitId,
  DESCRIPTION description,
  STATUS status
FROM BOC_UNIT
CONNECT BY PRIOR UNIT_ID = PARENT_UNIT_ID
START WITH UNIT_CODE = :p_unit_code