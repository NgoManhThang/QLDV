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
  BT.MONTH_YEAR monthYear
FROM BOC_TARGET BT
WHERE 1 = 1 
AND BT.TARGET_MONTH = :targetMonth 
AND BT.REGION_CODE = :regionCode 
AND BT.TARGET_TYPE = :targetType 