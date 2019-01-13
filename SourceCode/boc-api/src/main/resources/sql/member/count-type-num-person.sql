SELECT
  UNION_ID unionId,
  SUM(CASE
      WHEN NATIONAL_ID = 'VN'
        THEN 1
      ELSE 0
      END) numPersonVN,
  SUM(CASE
      WHEN NATIONAL_ID <> 'VN'
        THEN 1
      ELSE 0
      END) numPersonNN
FROM QLDV_UNIONS_MEMBER
WHERE UNION_ID = :union_id
GROUP BY UNION_ID