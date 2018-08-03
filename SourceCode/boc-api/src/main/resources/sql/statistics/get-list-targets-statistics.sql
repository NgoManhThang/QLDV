with region_info as
  (select distinct 
      case 
        when :p_region_level = 0 then kv_code
        when :p_region_level = 1 then province_code
        when :p_region_level = 2 then district_code
      end regionCode, 
      case 
        when :p_region_level = 0 then kv_name
        when :p_region_level = 1 then province_name
        when :p_region_level = 2 then district_name
      end regionName
    from cata_location
    where 1=1 AND status in(0, 1) -- b? qua c�c th?ng kh�ng ph?i t?nh (nh?ng th?ng n�y ch? qu?n l� 2 ch? ti�u c?a Ch?m s�c kh�ch h�ng)
      and (:p_region_level = 0 or (:p_region_level = 1 and province_code=:p_region_code) or (:p_region_level = 2 and district_code=:p_region_code))
    ),
    kpi_list as (
    select code kpiCode, decode kpiName, note kpiOrder, direction, decode
    from boc_code
    where code_group ='BOC_GROUP'
    and code_level = 2
    order by note
    ),
    target_data as (
    select t.region_code, t.target_type, target_num, warning_1, warning_2, target_month
    from   
      (select rgn.regionCode, rgn.regionName
      ,kpi.kpiCode, kpi.kpiOrder
      from region_info rgn
        ,kpi_list kpi
      ) rgn
      inner join boc_target t on 
        t.region_code=rgn.regionCode 
        and t.target_type=rgn.kpiCode
        and month_year='T'
    ),
  result_data as (
    select day_id,a.regionCode, a.regionName
    , a.kpiCode, a.decode, kpiOrder,
      actual,
      target_num,
      warning_1,
      warning_2,
      case 
        when actual is null or target_num is null then '-'
        when actual*direction >= target_num*direction then 'D'
        when actual*direction < warning_2*direction then 'M3'
        when actual*direction < warning_1*direction then 'M2'
        else 'M1'
      end result
    from
      (select rgn.regionCode, rgn.regionName
      , kpi.kpiCode, kpi.kpiOrder, kpi.decode
        , direction
      from region_info rgn
       , kpi_list kpi
      ) a
      left join boc_summary kpiData on 
        a.regionCode=kpiData.region_code
        and a.kpiCode=kpiData.boc_code
      left join target_data t on
        t.region_code=a.regionCode
        and t.target_type=a.kpiCode
        and to_char(kpiData.day_id, 'YYYYMM') = t.target_month 
    )
select kpicode code,
    decode,
    target_num target,
    actual,
    result evaluate
from result_data
where to_char(day_id,'MM/YYYY') = to_char(add_months(f_boc_date(kpiCode), 0),'MM/YYYY')