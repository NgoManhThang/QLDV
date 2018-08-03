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
  BU.COMPANY      company
FROM BOC_USER BU
WHERE BU.STATUS = 1 AND BU.USER_NAME = :userName