with region_info as
  (select distinct 
      unit_code regionCode,
      unit_name regionName
    from boc_unit
    where status = 1 
      and unit_code = :p_region_code
    )
--    select * from region_info;
    ,
    kpi_list as (
    select code kpiCode, decode kpiName, note kpiOrder, direction, ref_data
    from boc_code
    where code_group ='BOC_GROUP'
    order by note
    )
--    select * from kpi_list;
    ,
    target_data as (
    select t.region_code, t.target_type, target_num, warning_1, warning_2, target_month
    from   
      (select rgn.regionCode, rgn.regionName
      ,kpi.kpiCode, kpi.kpiOrder, kpi.ref_data
      from region_info rgn
        ,kpi_list kpi
      ) rgn
      inner join boc_target t on 
        t.region_code=rgn.regionCode 
        and t.target_type=rgn.kpiCode
        and month_year='T'
    )
--    select * from target_data;
    ,
  result_data as (
    select day_id,a.regionCode, a.regionName
    , a.kpiCode, kpiOrder,
      actual,
      target_num,
      warning_1,
      warning_2,
      ref_data,
      case 
        when actual is null or target_num is null then '-'
        when actual*direction >= target_num*direction then 'D'
        when actual*direction < warning_2*direction then 'M3'
        when actual*direction < warning_1*direction then 'M2'
        else 'M1'
      end result
    from
      (select rgn.regionCode, rgn.regionName
      , kpi.kpiCode, kpi.kpiOrder, kpi.ref_data
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
  list_manager as (
  select us.user_name,
      us.full_name,
      us.phone_number,
      us.email,
      us.position,
      un.unit_code regionCode,
      us.company,
      trunc(months_between(f_working_date(),us.working_date)) working_month,
      bf.file_id,
      (select bct.decode from boc_code bct where bct.code_group = 'POSITION' and bct.code = us.position) positionName,
      (select bct.decode from boc_code bct where bct.code_group = 'COMPANY' and bct.code = us.company) companyName
  from boc_user us
  inner join boc_unit_user unu
  on unu.user_id = us.user_id
  inner join boc_unit un
  on un.unit_id = unu.unit_id
  left join boc_files bf
  on bf.group_id = us.user_id
  where us.position = 'GD'
  and un.unit_code = :p_region_code
  )   

select 
  userName,
  fullName,
  phoneNumber,
  email,
  position,
  company,
  regionCode,
  workingMonth,
  fileId,
  positionName,
  companyName,
  regionName,
  failKpi,
  case when company = 'CTDL' then total_CTDL when company = 'CCTLDCT' then total_CCTLDCT when company = 'CNTT' then total_CNTT end totalKpi
from (select lm.user_name userName,
      lm.full_name fullName,
      lm.phone_number phoneNumber,
      lm.email email,
      lm.position position,
      lm.company company,
      lm.regionCode regionCode,
      lm.working_month workingMonth,
      lm.file_id fileId,
      lm.positionName positionName,
      lm.companyName companyName,
      a.regionName regionName,
      nvl(sum(a.point_num_m1), 0) failKpi,
      (select count(code) from boc_code where ref_data in (1, 3, 4)) total_CTDL,
      (select count(code) from boc_code where ref_data in (2, 3, 4)) total_CCTLDCT,
      (select count(code) from boc_code where ref_data = 3) total_CNTT
   from list_manager lm
   join
   (
    select bs.regionCode,
      bs.regionName,
      bs.ref_data,
    case when to_char(bs.day_id,'MM/YYYY') = to_char(add_months(f_boc_date(bs.kpiCode), 0),'MM/YYYY') and result != 'D' then '1'
        else '0' end point_num_m1
    from result_data bs
    ) a
    on lm.regionCode = a.regionCode
    where case when lm.company = 'CTDL' and a.ref_data in (1, 3, 4) then 1
        when lm.company = 'CCTLDCT' and a.ref_data in (2, 3, 4) then 1
        when lm.company = 'CNTT' and a.ref_data = 3 then 1 end = 1
    group by lm.user_name,
      lm.full_name,
      lm.phone_number,
      lm.email,
      lm.position,
      lm.company,
      lm.regionCode,
      lm.working_month,
      lm.file_id,
      lm.positionName,
      lm.companyName,
      a.regionName)