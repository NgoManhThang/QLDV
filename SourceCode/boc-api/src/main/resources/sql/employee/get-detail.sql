SELECT
  EMPLOYEE_ID  employeeId,
  USER_NAME    userName,
  PASSWORD     password,
  CODE         code,
  FULL_NAME    fullName,
  PHONE_NUMBER phone,
  EMAIL        email,
  POSITION     position,
  CREATE_USER  createUser,
  CREATE_DATE  createDate,
  UPDATE_USER  updateUser,
  UPDATE_DATE  updateDate,
  STATUS       status
FROM QLDV_EMPLOYEE
WHERE EMPLOYEE_ID = :p_id