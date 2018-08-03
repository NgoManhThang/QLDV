WITH province_list AS
  (
    SELECT DISTINCT province_code, province_name, status
      FROM cata_location 
      WHERE status IN (1,2)
      AND (:p_unit_code is null or :p_unit_code = 'PB') or province_code = :p_unit_code
  ) 
--  select * from province_list;
  ,
  boc_list as
  (
    select  code kpiCode, 
            decode name,
            direction,
            ref_data,
            note kpiOrder
    from boc_code
    where code_group='BOC_GROUP'
    and code = :p_boc_code
    and code_level = 2
  )
--  select * from boc_list;
  ,
  target_data as
 (
    select region_code,
           target_type,
           target_num, 
           warning_1,
           warning_2
        from 
        (select p.province_code, p.province_name, boc.kpiCode, boc.name
          from province_list p,
            boc_list boc
          where case when ref_data != 3 and status = 1 then 1 
                  when ref_data = 3 then 1 end = 1
          ) rgn
        inner join boc_target tg on tg.region_code = rgn.province_code
                                and tg.target_type = rgn.kpiCode
                                and target_month = TO_CHAR(f_boc_date(tg.target_type), 'YYYYMM')
                                and month_year = 'T'     
  )
--  select * from target_data;
,
  boc_data as (
    select a.province_code, a.province_name, a.kpiCode, kpiOrder,
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
      end levelAlarm
    from
      (select rgn.province_code, rgn.province_name, kpi.kpiCode, kpi.kpiOrder, direction, kpi.ref_data
      from province_list rgn,
        boc_list kpi
      where case when kpi.ref_data != 3 and rgn.status = 1 then 1
            when kpi.ref_data = 3 then 1 end = 1
      ) a
      left join boc_summary kpiData on 
        a.province_code = kpiData.region_code
        and a.kpiCode=kpiData.boc_code
        and kpiData.day_id=f_boc_date(kpiData.boc_code)
      left join target_data t on
        t.region_code=a.province_code
        and t.target_type=a.kpiCode
    )
--  select * from boc_data;
  
SELECT PD.PROVINCE_ID provinceId, 
  PD.PROVINCE_CODE provinceCode,
  PD.PROVINCE_NAME provinceName, 
  PD.PROVINCE_VNAME provinceVname, 
  PD.DISTRICT_ID districtId,
  PD.DISTRICT_CODE districtCode, 
  PD.DISTRICT_NAME districtName, 
  PD.IMPORT_DATA importData, 
  PD.GROUP_PROV groupProv, 
  PD.VTMAPS_CODE vtmapsCode, 
  PD.CENTER_LOC centerLoc, 
  PD.NORTH_LOC northLoc, 
  PD.SOUTH_LOC southLoc, 
  PD.IMPORT_DATE importDate, 
  PD.GEOMETRY geometry, 
  --PD.GEOMETRY_MINI geometryMini,
  PD.NORTH_POLE northPole,
  da.target_num target,
  da.actual actual,
  da.levelAlarm levelAlarm
FROM boc_data  da
join BOC_REGION_MAP pd on pd.province_code = da.province_code
WHERE PD.PROVINCE_CODE = PD.DISTRICT_CODE
ORDER BY NLSSORT(PD.PROVINCE_VNAME, 'nls_sort = Vietnamese')