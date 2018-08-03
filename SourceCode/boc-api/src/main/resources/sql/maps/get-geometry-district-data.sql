WITH district_list AS
  (
    SELECT DISTINCT district_code, district_name, status
      FROM cata_location 
      WHERE status IN (1,2)
      AND province_code = :p_province_code
      AND :p_unit_code is null or district_code = :p_unit_code
  ),
  
boc_list as
(
  select  code, 
            decode name,
            direction,
            ref_data
    from boc_code
    where code_group='BOC_GROUP'
      and code = :p_boc_code
),
target_data as
  (
    select region_code,
           target_type,
           target_num target, 
           warning_1,
           warning_2
        from 
        (select d.district_code, boc.code, boc.name
          from district_list d,
            boc_list boc
          where case when ref_data != 3 and status = 1 then 1 
                  when ref_data = 3 then 1 end = 1
          ) rgn
        inner join boc_target tg on tg.region_code = rgn.district_code
                                and tg.target_type = rgn.code
                                and target_month = TO_CHAR(f_boc_date(tg.target_type), 'YYYYMM')
                                and month_year = 'T'    
  ),
  
  boc_data as
  (
  select  a.district_code,
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
        select distinct d.district_code, b.code, b.name, b.direction
        from district_list d, boc_list b
        where case when ref_data != 3 and status = 1 then 1 
                  when ref_data = 3 then 1 end = 1
      )a 
    left join boc_summary boc on a.district_code = boc.region_code 
                              and boc.day_id=f_boc_date(boc.boc_code)
                              and boc.boc_code = a.code
    left join target_data tg on tg.region_code = a.district_code                
                            and tg.target_type = a.code  
  )  
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
  da.target target,
  da.actual actual,
  da.levelAlarm levelAlarm
FROM boc_data da
  join BOC_REGION_MAP pd on pd.district_code = da.district_code
  ORDER BY NLSSORT(PD.DISTRICT_NAME, 'nls_sort = Vietnamese')