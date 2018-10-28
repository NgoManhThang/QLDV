SELECT
  BU.USER_ID      userId,
  BU.CODE         code,
  BU.FULL_NAME    fullName,
  BU.PHONE_NUMBER phoneNumber,
  BU.EMAIL        email,
  BU.POSITION     position,
  BU.CREATE_USER  createUser,
  BU.CREATE_DATE  createDate,
  BU.UPDATE_USER  updateUser,
  BU.UPDATE_DATE  updateDate,
  BU.USER_NAME    userName,
  BU.PASSWORD     password,
  BU.STATUS       status,
  BU.COMPANY      company,
  files.FILE_ID   fileId
FROM BOC_USER BU
  LEFT JOIN QLDV_FILES files ON files.GROUP_ID = BU.USER_ID AND files.GROUP_FILE = '1'
WHERE BU.STATUS = 1 AND BU.USER_NAME = :userName