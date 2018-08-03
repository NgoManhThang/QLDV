with date_input as (
    select to_char(trunc(F_WORKING_DATE()),'YYYYMMDD') p_to_date, to_char(add_months(F_WORKING_DATE(), -3), 'YYYYMMDD') as p_from_date from dual
  ),
  date_range as (
  select 
    case 
      when :p_date_group_type='D' then to_date(p_from_date, 'YYYYMMDD')
      when :p_date_group_type='W' then TRUNC(to_date(p_from_date, 'YYYYMMDD'),'iw')
      when :p_date_group_type='M' then TRUNC(to_date(p_from_date, 'YYYYMMDD'),'mm')
      when :p_date_group_type='Q' then TRUNC(to_date(p_from_date, 'YYYYMMDD'),'q')
      when :p_date_group_type='Y' then TRUNC(to_date(p_from_date, 'YYYYMMDD'),'y')
      else to_date(p_from_date, 'YYYYMMDD')
    end start_date,
    case 
      when :p_date_group_type='D' then to_date(p_to_date, 'YYYYMMDD')
      when :p_date_group_type='W' then TRUNC(to_date(p_to_date, 'YYYYMMDD'),'iw') + 7 - 1/86400
      when :p_date_group_type='M' then LAST_DAY (TRUNC(to_date(p_to_date, 'YYYYMMDD'), 'mm')) + 1 - 1/86400
      when :p_date_group_type='Q' then TRUNC(TRUNC(to_date(p_to_date, 'YYYYMMDD'), 'q')+93,'mm')- 1/86400
      when :p_date_group_type='Y' then trunc(TRUNC(to_date(p_to_date, 'YYYYMMDD'),'y')+370, 'y')- 1/86400
      else to_date(p_to_date, 'YYYYMMDD')
    end end_date,
    case
      when :p_date_group_type='D' then 1
      when :p_date_group_type='W' then 7
      when :p_date_group_type='M' then 30
      when :p_date_group_type='Q' then 90
      when :p_date_group_type='Y' then 365
      else 1
    end day_step
  from date_input
  ),
  date_control as (
  select
    case 
      when :p_date_group_type in ('D', 'W') then start_date + (level - 1) * day_step
      when :p_date_group_type='M' then add_months(start_date, level-1)
      when :p_date_group_type='Q' then add_months(start_date, (level-1)*3)
      when :p_date_group_type='Y' then add_months(start_date, (level-1)*12)
    end range_start_date,
    case 
      when :p_date_group_type in ('D', 'W') then start_date + (level) * day_step - 1
      when :p_date_group_type='M' then LAST_DAY(add_months(start_date, level-1))
      when :p_date_group_type='Q' then add_months(start_date, (level)*3)- 1/86400
      when :p_date_group_type='Y' then add_months(start_date, (level)*12)- 1/86400
    end range_end_date,
    case 
      when :p_date_group_type in ('D', 'W') then end_date - day_step + 1
      when :p_date_group_type='M' then add_months(end_date, -1)+1
      when :p_date_group_type='Q' then add_months(end_date, -3)+1
      when :p_date_group_type='Y' then add_months(end_date, -12)+1
    end last_range_start_date,
    start_date,
    end_date,
    4-level month_index
  from date_range
  connect by
    case 
      when :p_date_group_type in ('D', 'W') then start_date + (level) * day_step - 1
      when :p_date_group_type='M' then LAST_DAY(add_months(start_date, level-1))
      when :p_date_group_type='Q' then add_months(start_date, (level)*3)- 1/86400
      when :p_date_group_type='Y' then add_months(start_date, (level)*12)- 1/86400
    end <= end_date
  )
select TO_CHAR(range_start_date, 'MM/YYYY') monthName
from date_control
order by range_start_date desc