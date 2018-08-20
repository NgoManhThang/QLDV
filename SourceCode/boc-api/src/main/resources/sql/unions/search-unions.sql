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
  T4.DECODE                                              status
FROM QLDV_UNIONS T1
  LEFT JOIN QLDV_PARTNER T2 ON T1.PARTNER_ID = T2.PARTNER_ID
  LEFT JOIN QLDV_EMPLOYEE T3 ON T1.EMPLOYEE_ID = T3.EMPLOYEE_ID
  LEFT JOIN QLDV_CODE T4 ON T1.STATUS = T4.CODE AND T4.CODE_GROUP = 'STATUS_UNIONS'
WHERE 1 = 1