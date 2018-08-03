with region_info as
  (select distinct 
      case 
        when :p_province_code is null then province_code
        else district_code
      end regionCode, 
      case 
        when :p_province_code is null then province_name
        else district_name
      end regionName,
      status
    from cata_location
    where 1=1 AND status IN (1 ,2)
      and (:p_province_code is null or (:p_province_code is not null and province_code=:p_province_code and district_code is not null))
    ),
    kpi_list as (
    select code kpiCode, decode kpiName, note kpiOrder, direction, ref_data
    from boc_code
    where code_group ='BOC_GROUP'
      and parent_code=:p_boc_code_group
    order by note
    ),
    target_data as (
    select t.region_code, t.target_type, target_num, warning_1, warning_2
    from   
      (select rgn.regionCode, rgn.regionName, kpi.kpiCode, kpi.kpiOrder
      from region_info rgn,
        kpi_list kpi
      ) rgn
      inner join boc_target t on 
        t.region_code=rgn.regionCode 
        and t.target_type=rgn.kpiCode
        and case when rgn.kpiCode = 'LOSS_ELECTRIC_POWER_ACCUMULATED' 
                      and month_year = 'N' 
                      and target_month = TO_CHAR(f_boc_date(t.target_type), 'YYYY')then 1
                when rgn.kpiCode != 'LOSS_ELECTRIC_POWER_ACCUMULATED'
                      and month_year = 'T'
                      and target_month = TO_CHAR(f_boc_date(t.target_type), 'YYYYMM') then 1 end = 1
    ),
    result_data as (
    select a.regionCode, a.regionName, a.kpiCode, kpiOrder,
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
      (select rgn.regionCode, rgn.regionName, kpi.kpiCode, kpi.kpiOrder, direction, kpi.ref_data
      from region_info rgn,
        kpi_list kpi
      where case when kpi.ref_data != 3 and rgn.status = 1 then 1
            when kpi.ref_data = 3 then 1 end = 1
      ) a
      left join boc_summary kpiData on 
        a.regionCode=kpiData.region_code
        and a.kpiCode=kpiData.boc_code
        and kpiData.day_id = f_boc_date(kpiData.boc_code)
      left join target_data t on
        t.region_code=a.regionCode
        and t.target_type=a.kpiCode
    )
    
select a.regionCode, a.regionName,
  LISTAGG(nvl(round(target_num,2),-9999), ';') WITHIN GROUP (ORDER BY a.kpiOrder) targetList,
  LISTAGG(nvl(round(actual,2),-9999), ';') WITHIN GROUP (ORDER BY a.kpiOrder) actualList,
  LISTAGG(nvl(result,'-'), ';') WITHIN GROUP (ORDER BY a.kpiOrder) resultList
from 
  result_data a
where :p_region_level = 0 
or (:p_region_level = 1 and (:p_unit_code = a.regionCode or :p_unit_code = :p_province_code)) 
or (:p_region_level = 2 and :p_unit_code = a.regionCode)
group by a.regionCode, a.regionName
order by NLSSORT(a.regionName, 'nls_sort = Vietnamese')
