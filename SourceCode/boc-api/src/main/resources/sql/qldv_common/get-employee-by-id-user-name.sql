SELECT
  T1.EMPLOYEE_ID employeeId,
  T1.CODE        code,
  T1.FULL_NAME   fullName,
  T1.USER_NAME   userName,
  T1.PHONE_NUMBER phone
FROM QLDV_EMPLOYEE T1
WHERE T1.STATUS = 1
      AND (:p_id IS NULL OR (:p_id IS NOT NULL AND T1.EMPLOYEE_ID = :p_id))