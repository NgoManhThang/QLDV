WITH
    partner_list AS (
      SELECT trim(regexp_substr(:partner_id_list, '[^,]+', 1, LEVEL)) partner_id
      FROM dual
      CONNECT BY LEVEL <= regexp_count(:partner_id_list, ',') + 1
  ),
    union_type_list AS (
      SELECT trim(regexp_substr(:union_type_code, '[^,]+', 1, LEVEL)) union_type
      FROM dual
      CONNECT BY LEVEL <= regexp_count(:union_type_code, ',') + 1
  ),
    union_status AS (
      SELECT trim(regexp_substr(:union_status_code, '[^,]+', 1, LEVEL)) union_status
      FROM dual
      CONNECT BY LEVEL <= regexp_count(:union_status_code, ',') + 1
  )
SELECT
  T1.UNION_ID                                            unionId,
  T1.UNION_NAME                                          unionName,
  T1.PARTNER_ID                                          partnerId,
  T2.PARTNER_NAME                                        partnerName,
  T1.VIETNAMESE_NUMBER                                   vietnameseNumber,
  T1.FOREIGNER_NUMBER                                    foreignerNumber,
  TO_CHAR(T1.FROM_DATE, 'dd/MM/yyyy')                    fromDate,
  TO_CHAR(T1.TO_DATE, 'dd/MM/yyyy')                      toDate,
  T1.REPRESENT_NAME || ' (' || T1.REPRESENT_PHONE || ')' representUnion,
  T3.FULL_NAME || ' (' || T3.PHONE_NUMBER || ')'         representCompany,
  T1.CREATE_USER                                         createUser,
  T4.DECODE                                              status,
  T1.STATUS                                              statusValue
FROM QLDV_UNIONS T1
  LEFT JOIN QLDV_PARTNER T2 ON T1.PARTNER_ID = T2.PARTNER_ID
  LEFT JOIN QLDV_EMPLOYEE T3 ON T1.EMPLOYEE_ID = T3.EMPLOYEE_ID
  LEFT JOIN QLDV_CODE T4 ON T1.STATUS = T4.CODE AND T4.CODE_GROUP = 'STATUS_UNIONS'
  LEFT JOIN QLDV_CODE T5 ON T1.UNION_TYPE = T5.CODE AND T5.CODE_GROUP = 'UNIONS_TYPE'
WHERE
  (
    :union_name IS NULL OR (:union_name IS NOT NULL AND upper(T1.UNION_NAME) LIKE :union_name)
  )
  AND (
    :partner_id_list IS NULL OR (:partner_id_list IS NOT NULL AND T1.PARTNER_ID IN (SELECT *
                                                                                    FROM partner_list))
  )
  AND (
    :union_type_code IS NULL OR (:union_type_code IS NOT NULL AND T5.CODE IN (SELECT *
                                                                              FROM union_type_list))
  )
  AND (
    :union_status_code IS NULL OR (:union_status_code IS NOT NULL AND T4.CODE IN (SELECT *
                                                                                  FROM union_status))
  )
  AND (
    :from_date_from IS NULL OR
    (:from_date_from IS NOT NULL AND trunc(T1.FROM_DATE) >= TO_DATE(:from_date_from, 'YYYYMMDD'))
  )
  AND (
    :from_date_to IS NULL OR (:from_date_to IS NOT NULL AND trunc(T1.FROM_DATE) <= TO_DATE(:from_date_to, 'YYYYMMDD'))
  )
  AND (
    :to_date_from IS NULL OR (:to_date_from IS NOT NULL AND trunc(T1.TO_DATE) >= TO_DATE(:to_date_from, 'YYYYMMDD'))
  )
  AND (
    :to_date_to IS NULL OR (:to_date_to IS NOT NULL AND trunc(T1.TO_DATE) <= TO_DATE(:to_date_to, 'YYYYMMDD'))
  )