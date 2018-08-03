with region_info as
  (select * from (
    select distinct 
      case 
        when :p_region_code is null then province_code
        else district_code
      end regionCode, 
      case 
        when :p_region_code is null then province_name
        else district_name
      end regionName,
      status
    from cata_location
    where 1=1 AND status in(0, 1) -- b? qua c�c th?ng kh�ng ph?i t?nh (nh?ng th?ng n�y ch? qu?n l� 2 ch? ti�u c?a Ch?m s�c kh�ch h�ng)
      and (:p_region_code is null or (:p_region_code is not null and province_code=:p_region_code))
    )
    where regionCode is not null
    order by regionCode
    ) ,
kpi_list as (
    select code kpiCode, decode kpiName, note kpiOrder, direction
    from boc_code
    where code_group ='BOC_GROUP'
    and code_level = 2
    order by note
    ),
boc_list as (
    select  code, 
              decode name,
              direction,
              ref_data
      from boc_code
      where code_group='BOC_GROUP'
        and code = :p_boc_code
  ),
target_data as (
    select region_code,
           target_type,
           target_num target, 
           warning_1,
           warning_2
        from 
        (select d.regionCode, boc.code, boc.name
          from region_info d,
            boc_list boc
          where case when ref_data != 3 and status = 1 then 1 
                  when ref_data = 3 then 1 end = 1
          ) rgn
        inner join boc_target tg on tg.region_code = rgn.regionCode
                                and tg.target_type = rgn.code
                                and target_month = TO_CHAR(f_boc_date(tg.target_type), 'YYYYMM')
                                and month_year = 'T'    
  ),
  
boc_data as (
  select  a.regionCode,
          target,
          boc.actual actual,
         case 
            when actual is null or target is null then '-'
            when actual*direction >= target*direction then 'D'
            when actual*direction < warning_2*direction then 'M3'
            when actual*direction < warning_1*direction then 'M2'
          else  'M1'
        end  LEVELALARM
      
    from 
      (
        select distinct d.regionCode, b.code, b.name, b.direction
        from region_info d, boc_list b
        where case when ref_data != 3 and status = 1 then 1 
                  when ref_data = 3 then 1 end = 1
      )a 
    left join boc_summary boc on a.regionCode = boc.region_code 
                              and boc.day_id=f_boc_date(boc.boc_code)
                              and boc.boc_code = a.code
    left join target_data tg on tg.region_code = a.regionCode                
                            and tg.target_type = a.code  
  )  

select 
	dat_total targetD,
	(select count(*) from region_info) totalTargetD,
	m1_total targetM1,
	(select count(*) from region_info) totalTargetM1,
	m2_total targetM2,
	(select count(*) from region_info) totalTargetM2,
	m3_total targetM3,
	(select count(*) from region_info) totalTargetM3
from(
    select *
    from(
      select regionCode, levelAlarm
      from boc_data
      )
      pivot (count(regionCode) as total for (levelAlarm) in ('D' dat, 'M1' m1, 'M2' m2, 'M3' m3)
    )
  )
