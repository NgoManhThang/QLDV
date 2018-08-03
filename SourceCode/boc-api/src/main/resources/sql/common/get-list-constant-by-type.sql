SELECT
  BC.CODE constantCode,
  BC.DECODE constantName
FROM BOC_CODE BC
WHERE BC.CODE_GROUP = :constantType