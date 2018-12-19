SELECT T1.UNION_MEMBER_ID unionMemberId,
       T1.FULL_NAME       fullName,
       T2.DECODE          nationalId,
       T1.MEMBER_ID       memberId,
       T1.LAPTOP_ID       laptopId,
       T1.APPR_STATUS     apprStatus,
       T1.REASON_NOT_APP  reasonNotApp,
       T3.FILE_ID         fileIdCMT,
       T4.FILE_ID         fileIdComputer
FROM QLDV_UNIONS_MEMBER T1
       LEFT JOIN QLDV_CODE T2 ON (T1.NATIONAL_ID = T2.CODE AND T2.CODE_GROUP = 'NATIONAL')
       LEFT JOIN QLDV_FILES T3 ON (T1.UNION_MEMBER_ID = T3.GROUP_ID AND T3.GROUP_FILE = '2')
       LEFT JOIN QLDV_FILES T4 ON (T1.UNION_MEMBER_ID = T4.GROUP_ID AND T4.GROUP_FILE = '3')
WHERE T1.UNION_ID = :union_id