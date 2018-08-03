WITH role_target_list as
(
  SELECT trim(regexp_substr((SELECT BU.ROLE_TARGET FROM BOC_USER BU WHERE BU.USER_NAME = :userName), '[^;]+', 1, LEVEL)) roleTargetId
  FROM dual
  CONNECT BY LEVEL <= regexp_count((SELECT BU.ROLE_TARGET FROM BOC_USER BU WHERE BU.USER_NAME = :userName), ';')+1
)
select 
  brt.ROLE_TARGET_ID roleTargetId,
  brt.ROLE_TARGET_CODE roleTargetCode,
  brt.ROLE_TARGET_NAME roleTargetName,
  brt.STATUS status,
  brt.PARENT_ROLE_TARGET_ID parentRoleTargetId
from boc_role_target brt
inner join role_target_list rtl on rtl.roleTargetId = brt.role_target_id
where brt.status = 1