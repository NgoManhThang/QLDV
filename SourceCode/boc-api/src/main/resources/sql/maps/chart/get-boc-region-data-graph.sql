with region_info as
  (select distinct 
      case 
        when :p_region_level=0 then 'SPC'
        when :p_region_level=1 then province_code
        else district_code
      end regionCode, 
      case 
        when :p_region_level=0 then 'SPC'
        when :p_region_level=1 then province_name
        else district_name
      end regionName
    from cata_location
    where 1=1
      and (:p_province_code is null or (:p_province_code is not null and province_code=:p_province_code))
    ),

code_type as (select ref_data from boc_code
                where code =:p_boc_code),

list_region as (
  select rc.regionCode, ct.ref_data 
  from ( select distinct pd.unit_code regionCode, status
          from boc_unit pd
          where pd.status IN (1, 2)
            and case when :p_province_code is null and pd.parent_unit_id = 1 then 1
                when :p_province_code is not null and pd.parent_unit_id != 1 
                      and pd.parent_unit_id = (select distinct unit_id from boc_unit where unit_code = :p_province_code) then 1
            end = 1
  ) rc, code_type ct
  where case when ct.ref_data != 3 and status = 1 then 1 
            when ct.ref_data = 3 then 1 end = 1
)

--select * from list_region;
  
 select LISTAGG(lr.regionCode, '; ') WITHIN GROUP (ORDER BY pd.unit_name) regionCode
      , LISTAGG(pd.unit_name, '; ') WITHIN GROUP (ORDER BY pd.unit_name) regionName
      , LISTAGG(nvl(bs.actual, -9999), '; ') WITHIN GROUP (ORDER BY pd.unit_name) actual
 from list_region lr
 inner join boc_unit pd
 on pd.unit_code = lr.regionCode
 left join (select bs1.day_id,
                                  bs1.boc_code,
                                  bs1.region_code,
                                  bs1.actual
                          from boc_summary bs1, (select max(day_id) day_id,
                                                        boc_code,
                                                        region_code
                                                  from boc_summary
                                                  group by to_char(day_id, 'YYYYMM'),
                                                            boc_code,
                                                            region_code) bs2
where bs1.day_id = bs2.day_id and bs1.boc_code = bs2.boc_code and bs1.region_code = bs2.region_code) bs
on lr.regionCode = bs.region_code
and day_id = f_boc_date(boc_code)
and boc_code = :p_boc_code