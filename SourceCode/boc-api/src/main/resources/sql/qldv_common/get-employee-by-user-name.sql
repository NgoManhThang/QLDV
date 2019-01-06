SELECT
  em.EMPLOYEE_ID  userId,
  em.USER_NAME    userName,
  em.PASSWORD     password,
  em.CODE         code,
  em.FULL_NAME    fullName,
  em.PHONE_NUMBER phone,
  em.EMAIL        email,
  em.POSITION     position,
  em.CREATE_USER  createUser,
  em.CREATE_DATE  createDate,
  em.UPDATE_USER  updateUser,
  em.UPDATE_DATE  updateDate,
  em.STATUS       status,
  files.FILE_ID   fileId,
  files.FILE_PATH pathImage
FROM QLDV_EMPLOYEE em
  LEFT JOIN QLDV_FILES files ON em.EMPLOYEE_ID = files.GROUP_ID AND files.GROUP_FILE = '1'
WHERE USER_NAME = :p_user_name AND ROWNUM = 1