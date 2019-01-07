SELECT
  em.EMPLOYEE_ID  employeeId,
  em.USER_NAME    userName,
  em.CODE         codeEmployee,
  em.FULL_NAME    fullName,
  em.PHONE_NUMBER phone,
  em.EMAIL        email,
  files.FILE_ID   fileId,
  files.FILE_PATH pathImage,
  LISTAGG(UN.EMPLOYEE_ID, '-') WITHIN GROUP (ORDER BY UN.EMPLOYEE_ID) unionIds
FROM QLDV_EMPLOYEE em
  LEFT JOIN QLDV_FILES files ON em.EMPLOYEE_ID = files.GROUP_ID AND files.GROUP_FILE = '1'
  LEFT JOIN QLDV_UNIONS un ON em.EMPLOYEE_ID = un.EMPLOYEE_ID
WHERE em.STATUS = 1
      AND (
        :p_user_name IS NULL OR (:p_user_name IS NOT NULL AND upper(em.USER_NAME) LIKE :p_user_name)
      )
      AND (
        :p_full_name IS NULL OR (:p_full_name IS NOT NULL AND upper(em.FULL_NAME) LIKE :p_full_name)
      )
GROUP BY em.EMPLOYEE_ID, em.USER_NAME, em.CODE, em.FULL_NAME, em.PHONE_NUMBER, files.FILE_ID, files.FILE_PATH, em.EMAIL