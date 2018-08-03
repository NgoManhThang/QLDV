select 
  a.code code, 
  a.decode decode
from boc_code a
  inner join
  (select *
  from boc_code
  where code_group='BOC_GROUP') b on a.parent_code=b.code
where b.code = :p_boc_code_group
order by a.note