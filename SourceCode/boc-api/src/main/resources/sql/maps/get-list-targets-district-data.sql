/*
select 
  a.code code, 
  a.decode decode,
  '20' target,
  '25' actual,
  'D' evaluate
from boc_code a
  inner join
  (select *
  from boc_code
  where code_group='BOC_GROUP') b on a.parent_code=b.code
where :p_region_code = :p_region_code
order by NLSSORT(b.decode, 'nls_sort = Vietnamese')
*/


with 
 region_info as
 ( select distinct district_code, district_name
	from cata_location
	where status = '1'
    and district_code = :p_district_code
 ),

  boc_list as
  (
    select  distinct code, 
            decode decode,
            direction
    from boc_code
    where code_group='BOC_GROUP'
      and code_level = '2'
  ),
  
 target_data as
 (
    select region_code,
           target_type,
           target_num target, 
           warning_1,
           warning_2
        from 
        (
          select re.district_code, re.district_name, b.code, b.decode, b.direction
          from region_info re, boc_list b
        )a
        inner join boc_target tg on tg.region_code = a.district_code
                                and target_month = TO_CHAR(f_working_date(), 'YYYYMM')
                                and month_year = 'T'
                                and tg.target_type = a.code
  )
select 
		code, 
		decode, target, actual,
--    warning_1,warning_2,
	case 
        when actual is null or target is null then '-'
        when actual*direction >= target*direction then 'D'
        when actual*direction < warning_2*direction then 'M3'
        when actual*direction < warning_1*direction then 'M2'
        else 'M1'
      end evaluate
from 
	(
		select r.district_code, r.district_name, b.code, b.decode, b.direction
		from region_info r, boc_list b
	) a
    left join target_data tg on a.district_code = tg.region_code
                            and a.code = tg.target_type
	left join boc_summary boc on boc.region_code = a.district_code
							 and boc.boc_code = a.code
							 and boc.day_id = f_working_date()
order by decode asc