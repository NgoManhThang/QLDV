SELECT
  T1.PARTNER_ID   partnerId,
  T1.PARTNER_CODE partnerCode,
  T1.PARTNER_NAME partnerName,
  T1.PARTNER_TYPE partnerType,
  T2.DECODE       partnerTypeName,
  T1.STATUS       status,
  T1.CREATE_USER  createUser,
  T1.CREATE_DATE  createDate,
  T1.UPDATE_USER  updateUser,
  T1.UPDATE_DATE  updateDate
FROM QLDV_PARTNER T1 LEFT JOIN QLDV_CODE T2
    ON (T1.PARTNER_TYPE = T2.CODE AND T2.CODE_GROUP = 'PARTNER_TYPE')
WHERE T1.STATUS = 1
      AND (:p_partner_code IS NULL OR (:p_partner_code IS NOT NULL AND upper(T1.PARTNER_CODE) LIKE :p_partner_code))
      AND (:p_partner_name IS NULL OR (:p_partner_name IS NOT NULL AND upper(T1.PARTNER_NAME) LIKE :p_partner_name))
      AND (:p_partner_type IS NULL OR (:p_partner_type IS NOT NULL AND T1.PARTNER_TYPE = :p_partner_type))