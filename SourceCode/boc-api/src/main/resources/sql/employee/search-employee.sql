SELECT
  em.EMPLOYEE_ID  employeeId,
  em.USER_NAME    userName,
  em.CODE         codeEmployee,
  em.FULL_NAME    fullName,
  em.PHONE_NUMBER phone,
  em.EMAIL        email
FROM QLDV_EMPLOYEE em
WHERE em.STATUS = 1
      AND (
        :p_user_name IS NULL OR (:p_user_name IS NOT NULL AND upper(em.USER_NAME) LIKE :p_user_name)
      )
      AND (
        :p_full_name IS NULL OR (:p_full_name IS NOT NULL AND upper(em.FULL_NAME) LIKE :p_full_name)
      )