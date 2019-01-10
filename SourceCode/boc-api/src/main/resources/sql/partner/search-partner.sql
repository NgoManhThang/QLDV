SELECT
  T1.PARTNER_ID           partnerId,
  T1.PARTNER_CODE         partnerCode,
  T1.PARTNER_NAME         partnerName,
  T1.PARTNER_TYPE         partnerType,
  T2.DECODE               partnerTypeName,
  T1.STATUS               status,
  T3.DECODE               statusName,
  T1.CREATE_USER          createUser,
  T1.CREATE_DATE          createDate,
  T1.UPDATE_USER          updateUser,
  T1.UPDATE_DATE          updateDate,
  T1.REPRESENT_NAME       representName,
  T1.PHONE_REPRESENT      phoneRepresent,
  LISTAGG(T4.UNION_ID, '-')
  WITHIN GROUP (
    ORDER BY T4.UNION_ID) unionIds
FROM QLDV_PARTNER T1
  LEFT JOIN QLDV_CODE T2 ON (T1.PARTNER_TYPE = T2.CODE AND T2.CODE_GROUP = 'PARTNER_TYPE')
  LEFT JOIN QLDV_CODE T3 ON (T1.STATUS = T3.CODE AND T3.CODE_GROUP = 'STATUS_COMMON')
  LEFT JOIN QLDV_UNIONS T4 ON T1.PARTNER_ID = T4.PARTNER_ID
WHERE 1 = 1
      AND (:p_partner_code IS NULL OR (:p_partner_code IS NOT NULL AND upper(T1.PARTNER_CODE) LIKE :p_partner_code))
      AND (:p_partner_name IS NULL OR (:p_partner_name IS NOT NULL AND upper(T1.PARTNER_NAME) LIKE :p_partner_name))
      AND (:p_partner_type IS NULL OR (:p_partner_type IS NOT NULL AND T1.PARTNER_TYPE = :p_partner_type))
      AND (:p_partner_status IS NULL OR (:p_partner_status IS NOT NULL AND T1.STATUS = :p_partner_status))
GROUP BY T1.PARTNER_ID, T1.PARTNER_CODE, T1.PARTNER_NAME, T1.PARTNER_TYPE, T2.DECODE, T1.STATUS, T3.DECODE,
  T1.CREATE_USER, T1.CREATE_DATE,
  T1.UPDATE_USER, T1.UPDATE_DATE, T1.REPRESENT_NAME, T1.PHONE_REPRESENT