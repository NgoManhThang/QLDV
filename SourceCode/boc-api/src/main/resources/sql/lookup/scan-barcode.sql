SELECT
  T1.UNION_MEMBER_ID                  unionMemberId,
  T5.UNION_ID                         unionId,
  T5.UNION_NAME                       unionName,
  T6.PARTNER_NAME                     partnerName,
  T1.FULL_NAME                        fullName,
  T2.DECODE                           nationalName,
  T1.MEMBER_ID                        memberId,
  TO_CHAR(T5.FROM_DATE, 'dd/MM/yyyy') fromDate,
  TO_CHAR(T5.TO_DATE, 'dd/MM/yyyy')   toDate,
  T1.LAPTOP_ID                        laptopId,
  T3.FILE_ID                          fileIdCMT,
  T4.FILE_ID                          fileIdComputer,
  T1.BAR_CODE_USER                    barCodeUser,
  T1.BAR_CODE_COMPUTER                barCodeComputer
FROM QLDV_UNIONS_MEMBER T1
  LEFT JOIN QLDV_CODE T2 ON (T1.NATIONAL_ID = T2.CODE AND T2.CODE_GROUP = 'NATIONAL')
  LEFT JOIN QLDV_FILES T3 ON (T1.UNION_MEMBER_ID = T3.GROUP_ID AND T3.GROUP_FILE = '2')
  LEFT JOIN QLDV_FILES T4 ON (T1.UNION_MEMBER_ID = T4.GROUP_ID AND T4.GROUP_FILE = '3')
  LEFT JOIN QLDV_UNIONS T5 ON T1.UNION_ID = T5.UNION_ID
  LEFT JOIN QLDV_PARTNER T6 ON T6.PARTNER_ID = T5.PARTNER_ID
