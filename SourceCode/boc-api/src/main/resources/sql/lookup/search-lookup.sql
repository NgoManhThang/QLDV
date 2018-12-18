WITH
    partner_list AS (
      SELECT trim(regexp_substr(:partner_id_list, '[^,]+', 1, LEVEL)) partner_id
      FROM dual
      CONNECT BY LEVEL <= regexp_count(:partner_id_list, ',') + 1
  )
SELECT
  T1.UNION_ID                         unionId,
  T1.UNION_NAME                       unionName,
  T1.PARTNER_ID                       partnerId,
  T2.PARTNER_NAME                     partnerName,
  T1.VIETNAMESE_NUMBER                vietnameseNumber,
  T1.FOREIGNER_NUMBER                 foreignerNumber,
  TO_CHAR(T1.FROM_DATE, 'dd/MM/yyyy') fromDate,
  TO_CHAR(T1.TO_DATE, 'dd/MM/yyyy')   toDate
FROM QLDV_UNIONS T1
  LEFT JOIN QLDV_PARTNER T2 ON T1.PARTNER_ID = T2.PARTNER_ID
WHERE T1.STATUS = 'APPROVE' AND
      (
        :union_name IS NULL OR (:union_name IS NOT NULL AND upper(T1.UNION_NAME) LIKE :union_name)
      )
      AND (
        :partner_id_list IS NULL OR (:partner_id_list IS NOT NULL AND T1.PARTNER_ID IN (SELECT *
                                                                                        FROM partner_list))
      )
      AND (
        :from_date_from IS NULL OR
        (:from_date_from IS NOT NULL AND trunc(T1.FROM_DATE) >= TO_DATE(:from_date_from, 'YYYYMMDD'))
      )
      AND (
        :from_date_to IS NULL OR
        (:from_date_to IS NOT NULL AND trunc(T1.FROM_DATE) <= TO_DATE(:from_date_to, 'YYYYMMDD'))
      )
      AND (
        :to_date_from IS NULL OR (:to_date_from IS NOT NULL AND trunc(T1.TO_DATE) >= TO_DATE(:to_date_from, 'YYYYMMDD'))
      )
      AND (
        :to_date_to IS NULL OR (:to_date_to IS NOT NULL AND trunc(T1.TO_DATE) <= TO_DATE(:to_date_to, 'YYYYMMDD'))
      )