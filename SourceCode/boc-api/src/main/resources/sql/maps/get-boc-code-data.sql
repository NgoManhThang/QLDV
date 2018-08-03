select 
  a.code code, 
  a.decode decode,
  b.code codeGroup,
  b.decode decodeGroup
from boc_code a
  inner join
  (select *
  from boc_code
  where code_group='BOC_GROUP') b on a.parent_code=b.code
where a.code in (:p_list_boc_code)
order by NLSSORT(b.decode, 'nls_sort = Vietnamese')