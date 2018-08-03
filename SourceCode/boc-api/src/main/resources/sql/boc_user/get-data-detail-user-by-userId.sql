WITH list_unit as (
  SELECT
    UNIT_ID unitId,
    UNIT_NAME unitName,
    UNIT_CODE unitCode,
    PARENT_UNIT_ID parentUnitId,
    MAP_UNIT_ID mapUnitId,
    DESCRIPTION description,
    STATUS status
  FROM BOC_UNIT
  CONNECT BY PRIOR UNIT_ID = PARENT_UNIT_ID
  START WITH UNIT_CODE = :p_unit_code
),
list_user_by_unit as (
  SELECT buu.USER_ID userId
  FROM BOC_UNIT_USER buu
  INNER JOIN list_unit lu ON lu.unitId = buu.UNIT_ID
)
SELECT  
	BU.USER_ID userId,
	BU.CODE code,
	BU.FULL_NAME fullName,
	BU.PHONE_NUMBER phoneNumber,
	BU.EMAIL email,
	BU.POSITION position,
	BU.CREATE_USER createUser,
	BU.CREATE_DATE createDate,
	BU.UPDATE_USER updateUser,
	BU.UPDATE_DATE updateDate,
	BU.USER_NAME userName,
	BU.STATUS status,
	BU.WORKING_DATE workingDate,
	BU.COMPANY company,
	BF.FILE_ID fileId,
	(
		SELECT LISTAGG(BRU.ROLE_ID, ';') WITHIN GROUP (ORDER BY BRU.ROLE_ID) 
		FROM BOC_ROLE_USER BRU 
		WHERE BRU.USER_ID = BU.USER_ID
	) listRoleString,
	(
		SELECT LISTAGG(BUU.UNIT_ID, ';') WITHIN GROUP (ORDER BY BUU.UNIT_ID) 
		FROM BOC_UNIT_USER BUU 
		WHERE BUU.USER_ID = BU.USER_ID
	) listUnitString,
	BU.ROLE_TARGET roleTarget,
	BU.ROLE_TARGET listRoleTargetString,
  	(SELECT BCT.DECODE FROM BOC_CODE BCT WHERE BCT.CODE_GROUP = 'POSITION' AND BCT.CODE = BU.POSITION) positionName,
  	(SELECT BCT.DECODE FROM BOC_CODE BCT WHERE BCT.CODE_GROUP = 'COMPANY' AND BCT.CODE = BU.COMPANY) companyName
FROM BOC_USER BU
LEFT JOIN BOC_FILES BF ON BF.GROUP_ID = BU.USER_ID AND BF.GROUP_FILE = 1 
INNER JOIN list_user_by_unit lubu ON lubu.userId = BU.USER_ID
WHERE BU.STATUS = 1 AND BU.USER_ID = :userId