WITH group_list AS
(
    SELECT trim(regexp_substr(:p_group_list, '[^,]+', 1, LEVEL)) code_group
    FROM dual
    CONNECT BY LEVEL <= regexp_count(:p_group_list, ',') + 1
)
SELECT
  CODE_GROUP codeGroup,
  CODE       code,
  DECODE     decode,
  CODE_LEVEL codeLevel,
  STATUS     status
FROM qldv_code
WHERE STATUS = 1
      AND (
        :p_group_list IS NULL OR
        (:p_group_list IS NOT NULL AND CODE_GROUP IN (SELECT * FROM group_list))
      )