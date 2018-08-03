with region_info as
  (select distinct 
      case 
        when :p_province_code is null then province_code
        else district_code
      end regionCode, 
      case 
        when :p_province_code is null then province_name
        else district_name
      end regionName
    from cata_location
    where 1=1 AND status = 1 -- b? qua c�c th?ng kh�ng ph?i t?nh (nh?ng th?ng n�y ch? qu?n l� 2 ch? ti�u c?a Ch?m s�c kh�ch h�ng)
      and (:p_province_code is null or (:p_province_code is not null and province_code=:p_province_code))
    )
--    select * from region_info;
    ,
    kpi_list as (
    select code kpiCode, decode kpiName, note kpiOrder, direction
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
    , a.kpiCode, kpiOrder,
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
      , kpi.kpiCode, kpi.kpiOrder
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
--    select * from result_data;
    ,
    list_bad as
  ( 
   select 
      regionCode,
      sum(error_num_m1) failKpi0,
      nvl(sum(point_num_m1), 0) failPoint0,
      sum(error_num_m2) failKpi1,
      nvl(sum(point_num_m2), 0) failPoint1,
      sum(error_num_m3) failKpi2,
      nvl(sum(point_num_m3), 0) failPoint2,
      sum(error_num_m4) failKpi3,
      nvl(sum(point_num_m4), 0) failPoint3
   from (
    select bs.regionCode, 
    to_char(bs.day_id,'MM/YYYY') displayDate,
    case when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), 0),'MM/YYYY') and bs.result = 'D' then 0
        when to_char(bs.day_id,'MM/YYYY') != to_char(add_months(f_boc_date(bs.kpiCode), 0),'MM/YYYY') then 0 else 1 end error_num_m1,
    case when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), 0),'MM/YYYY') and result != 'D' then substr(bs.result,2,1) 
        when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), 0),'MM/YYYY') and result = 'D' then '0'
        when to_char(bs.day_id,'MM/YYYY') != to_char(add_months(f_boc_date(bs.kpiCode), 0),'MM/YYYY') then '0' end point_num_m1,
    case when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), -1),'MM/YYYY') and bs.result = 'D' then 0
        when to_char(bs.day_id,'MM/YYYY') != to_char(add_months(f_boc_date(bs.kpiCode), -1),'MM/YYYY') then 0 else 1 end error_num_m2,
    case when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), -1),'MM/YYYY') and result != 'D' then substr(bs.result,2,1) 
        when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), -1),'MM/YYYY') and result = 'D' then '0'
        when to_char(bs.day_id,'MM/YYYY') != to_char(add_months(f_boc_date(bs.kpiCode), -1),'MM/YYYY') then '0' end point_num_m2,
    case when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), -2),'MM/YYYY') and bs.result = 'D' then 0
        when to_char(bs.day_id,'MM/YYYY') != to_char(add_months(f_boc_date(bs.kpiCode), -2),'MM/YYYY') then 0 else 1 end error_num_m3,
    case when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), -2),'MM/YYYY') and result != 'D' then substr(bs.result,2,1) 
        when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), -2),'MM/YYYY') and result = 'D' then '0'
        when to_char(bs.day_id,'MM/YYYY') != to_char(add_months(f_boc_date(bs.kpiCode), -2),'MM/YYYY') then '0' end point_num_m3,
    case when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), -3),'MM/YYYY') and bs.result = 'D' then 0
        when to_char(bs.day_id,'MM/YYYY') != to_char(add_months(f_boc_date(bs.kpiCode), -3),'MM/YYYY') then 0 else 1 end error_num_m4,
    case when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), -3),'MM/YYYY') and result != 'D' then substr(bs.result,2,1) 
        when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), -3),'MM/YYYY') and result = 'D' then '0'
        when to_char(bs.day_id,'MM/YYYY') != to_char(add_months(f_boc_date(bs.kpiCode), -3),'MM/YYYY') then '0' end point_num_m4
    from result_data bs
    left join boc_unit bu
      on bs.regionCode = bu.unit_code
    where bu.status = 1
      and case 
        when :p_province_code is null and bu.parent_unit_id = 1 then 1
        when :p_province_code is not null and bu.parent_unit_id != 1 and bu.parent_unit_id = (select distinct unit_id from boc_unit where unit_code = :p_province_code) then 1
      end = 1
    )
    group by regionCode
  )
--  select * from list_bad;
  select bu.unit_code region_code,
      bu.unit_name region_name,
      (select count(distinct code) from boc_code, boc_unit where code_level = 2 and case when status = 2 and ref_data = 3 then 1 else 1 end = 1) totalKpi0,
      failKpi0,
      (select count(code) from boc_code where code_level = 2) totalKpi1,
      failKpi1,
      (select count(code) from boc_code where code_level = 2) totalKpi2,
      failKpi2,
      (select count(code) from boc_code where code_level = 2) totalKpi3,
      failKpi3,
      case
        when :p_region_level = 0 then 'true'
        when :p_region_level = 1 and (:p_unit_code = bu.unit_code or :p_unit_code = :p_province_code) then 'true'
        when :p_region_level = 1 and :p_unit_code <> bu.unit_code then 'false'
        when :p_region_level = 2 and :p_unit_code = bu.unit_code then 'true'
        when :p_region_level = 2 and :p_unit_code <> bu.unit_code then 'false'
        else 'false'
      end isClick
  from list_bad lb
  inner join boc_unit bu
  on bu.unit_code = lb.regionCode
  order by 
            failKpi0 DESC,
            failPoint0 DESC,
            failKpi1 DESC,
            failPoint1 DESC,
            failKpi2 DESC,
            failPoint2 DESC,
            failKpi3 DESC,
            failPoint3 DESC,
            regionCode
