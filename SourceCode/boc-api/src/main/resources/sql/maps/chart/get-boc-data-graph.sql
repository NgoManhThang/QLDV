with date_range as (
	select 
		case 
			when :p_date_group_type='D' then to_date(:p_from_date, 'YYYYMMDD')
			when :p_date_group_type='W' then TRUNC(to_date(:p_from_date, 'YYYYMMDD'),'iw')
			when :p_date_group_type='M' then TRUNC(to_date(:p_from_date, 'YYYYMMDD'),'mm')
			when :p_date_group_type='Q' then TRUNC(to_date(:p_from_date, 'YYYYMMDD'),'q')
			when :p_date_group_type='Y' then TRUNC(to_date(:p_from_date, 'YYYYMMDD'),'y')
			else to_date(:p_from_date, 'YYYYMMDD')
		end start_date,
		case 
			when :p_date_group_type='D' then to_date(:p_to_date, 'YYYYMMDD')
			when :p_date_group_type='W' then TRUNC(to_date(:p_to_date, 'YYYYMMDD'),'iw') + 7 - 1/86400
			when :p_date_group_type='M' then LAST_DAY (TRUNC(to_date(:p_to_date, 'YYYYMMDD'), 'mm')) + 1 - 1/86400
			when :p_date_group_type='Q' then TRUNC(TRUNC(to_date(:p_to_date, 'YYYYMMDD'), 'q')+93,'mm')- 1/86400
			when :p_date_group_type='Y' then trunc(TRUNC(to_date(:p_to_date, 'YYYYMMDD'),'y')+370, 'y')- 1/86400
			else to_date(:p_to_date, 'YYYYMMDD')
		end end_date,
		case
			when :p_date_group_type='D' then 1
			when :p_date_group_type='W' then 7
			when :p_date_group_type='M' then 30
			when :p_date_group_type='Q' then 90
			when :p_date_group_type='Y' then 365
			else 1
		end day_step
	from dual
  ),
  
 date_control_temp AS (
		SELECT
			CASE
			WHEN :p_date_group_type IN ('D', 'W', 'Default')
			  THEN start_date + (level - 1) * day_step
			WHEN :p_date_group_type = 'M'
			  THEN add_months(start_date, level - 1)
			WHEN :p_date_group_type = 'Q'
			  THEN add_months(start_date, (level - 1) * 3)
			WHEN :p_date_group_type = 'Y'
			  THEN add_months(start_date, (level - 1) * 12)
      when :p_date_group_type='T' then start_date
			END range_start_date,
			CASE
			WHEN :p_date_group_type IN ('D', 'W', 'Default')
			  THEN start_date + (level) * day_step - 1
			WHEN :p_date_group_type = 'M'
			  THEN LAST_DAY(add_months(start_date, level - 1))
			WHEN :p_date_group_type = 'Q'
			  THEN add_months(start_date, (level) * 3) - 1 / 86400
			WHEN :p_date_group_type = 'Y'
			  THEN add_months(start_date, (level) * 12) - 1 / 86400
      when :p_date_group_type='T' then end_date
			END range_end_date,
			CASE
			WHEN :p_date_group_type IN ('D', 'W', 'Default')
			  THEN end_date - day_step + 1
			WHEN :p_date_group_type = 'M'
			  THEN add_months(end_date, -1) + 1
			WHEN :p_date_group_type = 'Q'
			  THEN add_months(end_date, -3) + 1
			WHEN :p_date_group_type = 'Y'
			  THEN add_months(end_date, -12) + 1
      when :p_date_group_type='T' then start_date
			END last_range_start_date,
			start_date,
			end_date
		FROM date_range
		CONNECT BY
			CASE
			WHEN :p_date_group_type IN ('D', 'W', 'Default')
			  THEN start_date + (level) * day_step - 1
			WHEN :p_date_group_type = 'M'
			  THEN LAST_DAY(add_months(start_date, level - 1))
			WHEN :p_date_group_type = 'Q'
			  THEN add_months(start_date, (level) * 3) - 1 / 86400
			WHEN :p_date_group_type = 'Y'
			  THEN add_months(start_date, (level) * 12) - 1 / 86400
      when :p_date_group_type='T' then end_date+level
			END <= end_date
	),
  
date_control AS (
		SELECT
			range_start_date,
			range_end_date,
			last_range_start_date,
			start_date,
			end_date,
			CASE
			WHEN :p_date_group_type = 'D' OR :p_date_group_type = 'Default'
			  THEN to_char(range_start_date, 'DD/MM/YYYY')
			WHEN :p_date_group_type = 'W'
			  THEN 'W' || to_char(range_start_date, 'IW') || '/' || TO_CHAR(range_start_date, 'YY') || '<br/><small>(' ||
				   TO_CHAR(range_start_date, 'DD/MM/YYYY') || '=>' || TO_CHAR(range_end_date, 'DD/MM/YYYY') || ')</small>'
			WHEN :p_date_group_type = 'M'
			  THEN to_char(range_start_date, 'MM/YYYY')
			WHEN :p_date_group_type = 'Q'
			  THEN 'Q' || TO_CHAR(range_start_date, 'Q/YY') || '<br/><small>(' || TO_CHAR(range_start_date, 'DD/MM/YYYY') ||
				   '=>' || TO_CHAR(range_end_date, 'DD/MM/YYYY') || ')</small>'
			WHEN :p_date_group_type = 'Y'
			  THEN to_char(range_start_date, 'YYYY')
			ELSE to_char(range_start_date, 'DD/MM/YYYY')
			END                                                    displayDate,
			trunc(range_end_date) - trunc(range_start_date) + 1 AS numberOfDay
		FROM date_control_temp
		ORDER BY range_start_date
	),
  
region_info as
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

 graph_data as (select a.regionCode, a.regionName,
    case when :p_region_level = 0 then listagg(nvl(spc.actual, -9999), '; ') WITHIN GROUP (ORDER BY a.range_start_date)
        when :p_region_level = 1 then listagg(nvl(pro.actual, -9999), '; ') WITHIN GROUP (ORDER BY a.range_start_date)
        else listagg(nvl(dis.actual, -9999), '; ') WITHIN GROUP (ORDER BY a.range_start_date) end actual,
    case when :p_region_level = 0 then listagg(nvl(spc.target, -9999), '; ') WITHIN GROUP (ORDER BY a.range_start_date)
        when :p_region_level = 1 then listagg(nvl(pro.target, -9999), '; ') WITHIN GROUP (ORDER BY a.range_start_date)
        else listagg(nvl(dis.target, -9999), '; ') WITHIN GROUP (ORDER BY a.range_start_date) end target,
    listagg(a.displayDate, '; ') WITHIN GROUP (ORDER BY a.range_start_date) displayDate
  from
    (select * from date_control, region_info) a
    left join (select dc.range_start_date, bs.boc_code
        , bs.region_code region_code
        , bs.actual actual
        , nvl(ta.target_num, -9999) target
  from date_control dc
          inner join (select bs1.day_id, 
                        bs1.boc_code,
                        bs1.region_code,
                        bs1.actual 
                      from boc_summary bs1, (select max(day_id) day_id,
                                                    boc_code,
                                                    region_code
                                              from boc_summary 
                                              where region_code = 'SPC' 
                                              group by to_char(day_id, 'YYYYMM'),
                                                        boc_code,
                                                        region_code) bs2
                      where bs1.day_id = bs2.day_id 
                      and bs1.boc_code = bs2.boc_code
                      and bs1.region_code = bs2.region_code) bs 
            on trunc(bs.day_id) BETWEEN trunc(dc.range_start_date) AND trunc(dc.range_end_date)
            and bs.boc_code = :p_boc_code
            left join boc_target ta
            on ta.region_code = bs.region_code
            and ta.target_type = :p_boc_code
            and to_char(bs.day_id, 'YYYYMM') = ta.target_month
        where :p_region_level = 0
    ) spc on a.range_start_date=spc.range_start_date
    left join (select dc.range_start_date, bs.boc_code
                    , bs.region_code region_code
                    , bs.actual actual
                    , nvl(ta.target_num, -9999) target
              from date_control dc
              inner join (select bs1.day_id,
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
            on trunc(bs.day_id) BETWEEN trunc(dc.range_start_date) AND trunc(dc.range_end_date)
            and bs.boc_code = :p_boc_code
          inner join (select district_code from BOC_REGION_MAP
                      where province_code = district_code
                      and status = 1) pd
            on bs.region_code = pd.district_code
          left join boc_target ta
            on ta.region_code = bs.region_code
            and ta.target_type = :p_boc_code
            and to_char(bs.day_id, 'YYYYMM') = ta.target_month
        where :p_region_level = 1
    ) pro on a.range_start_date=pro.range_start_date and a.regionCode = pro.region_code
    left join (select dc.range_start_date, bs.boc_code
                , bs.region_code region_code
                , bs.actual actual
                , nvl(ta.target_num, -9999) target
              from date_control dc
              inner join (select bs1.day_id,
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
                on trunc(bs.day_id) BETWEEN trunc(dc.range_start_date) AND trunc(dc.range_end_date)
                and bs.boc_code = :p_boc_code
              inner join (select district_code
                          from BOC_REGION_MAP
                          where province_code != district_code
                          and status = 1) pd
                on bs.region_code = pd.district_code
              left join boc_target ta
                on ta.region_code = bs.region_code
                and ta.target_type = :p_boc_code
                and to_char(bs.day_id, 'YYYYMM') = ta.target_month
              where :p_region_level is null or :p_region_level NOT IN(0, 1)
    ) dis on a.range_start_date=dis.range_start_date and a.regionCode = dis.region_code
  group by a.regionCode, a.regionName)
 
  select * 
  from graph_data gd
  where :p_boc_code is not null
    and ((:p_region_level = 0 and gd.regionCode = 'SPC')
        or (:p_region_level = 1 and gd.regionCode = :p_province_code)
        or ((:p_region_level is null or :p_region_level NOT IN (0, 1)) and gd.regionCode = :p_district_code))