package com.viettel.api.repository.boc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.api.config.Constants;
import com.viettel.api.domain.boc.BocTargetEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocCodeDto;
import com.viettel.api.dto.boc.BocTargetDto;
import com.viettel.api.dto.boc.BocUserDto;
import com.viettel.api.dto.boc.CataLocationDto;
import com.viettel.api.dto.boc.KVDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.SQLBuilder;
import com.viettel.api.utils.StringUtils;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@SuppressWarnings("rawtypes")
@Repository
@Transactional
public class BocTargetRepositoryImpl extends BaseRepository implements BocTargetRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(BocTargetRepositoryImpl.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
	@SuppressWarnings("unchecked")
	@Override
    public Datatable search(BocTargetDto bocTargetDto) {
        Datatable dataReturn = new Datatable();
        try {
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
    		String unitCode = null;
        	if(!bocUserDto.getListUnit().isEmpty()) {
    			unitCode = bocUserDto.getListUnit().get(0);
        	} else {
        		return dataReturn;
        	}
        	String sqlQuery = "";
        	if (bocTargetDto.getListProvinceCodes() != null && bocTargetDto.getListProvinceCodes().size() > 0) {
        		sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_TARGET, "get-data-table-boc-target");
            } else {
            	sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_TARGET, "get-all-data-table-boc-target");
            }
        	
        	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getTargetType())){
        		sqlQuery += " AND BT.TARGET_TYPE = :targetType ";
            }
        	if ("T".equals(bocTargetDto.getMonthYear())){
        		sqlQuery += " AND BT.MONTH_YEAR = 'T' ";
        		if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && !StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYYMM') >= TO_DATE(:p_from_date, 'YYYYMM') ";
                }
            	if (!StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYYMM') <= TO_DATE(:p_to_date, 'YYYYMM') ";
                }
            	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYYMM') >= TO_DATE(:p_from_date, 'YYYYMM') ";
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYYMM') <= TO_DATE(:p_to_date, 'YYYYMM') ";
                }
        	}
        	if ("N".equals(bocTargetDto.getMonthYear())){
        		sqlQuery += " AND BT.MONTH_YEAR = 'N' ";
        		if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && !StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYY') >= TO_DATE(:p_from_date, 'YYYY') ";
                }
            	if (!StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYY') <= TO_DATE(:p_to_date, 'YYYY') ";
                }
            	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYY') >= TO_DATE(:p_from_date, 'YYYY') ";
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYY') <= TO_DATE(:p_to_date, 'YYYY') ";
                }
        	}
        	Map<String, Object> parameters = new HashMap<>();
        	
        	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getTargetType())){
        		parameters.put("targetType", bocTargetDto.getTargetType());
            }
        	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && !StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
        		parameters.put("p_from_date", bocTargetDto.getFromDateString());
            }
        	if (!StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
            	parameters.put("p_to_date", bocTargetDto.getToDateString());
            }
        	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
            	parameters.put("p_from_date", bocTargetDto.getFromDateString());
            	parameters.put("p_to_date", bocTargetDto.getToDateString());
            }
        	if (bocTargetDto.getListProvinceCodes() != null && bocTargetDto.getListProvinceCodes().size() > 0) {
        		if(bocUserDto.getRegionLevel() == 1L) {
        			parameters.put("p_province_code_list", unitCode);
        		} else {
        			parameters.put("p_province_code_list", String.join(",", bocTargetDto.getListProvinceCodes()));
        		}
            } else {
            	if(bocUserDto.getRegionLevel() == 1L) {
            		parameters.put("p_province_code_list", unitCode);
            	} else {
            		parameters.put("p_province_code_list", null);
            	}
            }
        	if (bocTargetDto.getListDistrictCodes() != null && bocTargetDto.getListDistrictCodes().size() > 0) {
        		if(bocUserDto.getRegionLevel() == 2L) {
        			parameters.put("p_district_code_list", unitCode);
        		} else {
        			parameters.put("p_district_code_list", String.join(",", bocTargetDto.getListDistrictCodes()));
        		}
            } else {
            	if(bocUserDto.getRegionLevel() == 2L) {
            		parameters.put("p_district_code_list", unitCode);
        		} else {
        			parameters.put("p_district_code_list", null);
        		}
            }
        	if (bocTargetDto.getListProvinceCodes() != null && bocTargetDto.getListProvinceCodes().size() > 0) {
        		
        	} else {
        		parameters.put("p_region_level", bocUserDto.getRegionLevel());
                parameters.put("p_unit_code", unitCode);
        	}
	        List<BocTargetDto> list = getListDataBySqlQuery(sqlQuery, parameters,
				bocTargetDto.getPage(), bocTargetDto.getPageSize(),
				BocTargetDto.class, true,
				bocTargetDto.getSortName(), bocTargetDto.getSortType());
	        
            int count = 0;
            if(list.isEmpty()) {
            	dataReturn.setRecordsTotal(count);
            } else {
            	count = list.get(0).getTotalRow();
            	dataReturn.setRecordsTotal(list.get(0).getTotalRow());
            }
            if (bocTargetDto.getPageSize() > 0){
                if (count % bocTargetDto.getPageSize() == 0){
                    dataReturn.setDraw(count / bocTargetDto.getPageSize());
                }
                else {
                    dataReturn.setDraw((count / bocTargetDto.getPageSize()) + 1);
                }
            }

            dataReturn.setData(list);
        } catch (Exception ex){
        	LOGGER.error(ex.getMessage(), ex);
        }

        return dataReturn;
    }

	@Override
	public List<BocCodeDto> getTypeTarget() {
		List<BocCodeDto> lstResult = new ArrayList<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
    		List<String> listBocCode = new ArrayList<>();
        	if(!bocUserDto.getListRoleTarget().isEmpty()) {
        		listBocCode = bocUserDto.getListRoleTarget();
        	} else {
        		return lstResult;
        	}
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_TARGET, "get-data-type-target");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_list_boc_code", listBocCode);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(BocCodeDto.class));
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
	}
	
	@Override
	public List<CataLocationDto> getListProvince() {
		List<CataLocationDto> lstResult = new ArrayList<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
    		String unitCode = null;
        	if(!bocUserDto.getListUnit().isEmpty()) {
    			unitCode = bocUserDto.getListUnit().get(0);
        	} else {
        		return lstResult;
        	}
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_TARGET, "get-data-province");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_region_level", bocUserDto.getRegionLevel());
            parameters.put("p_unit_code", unitCode);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(CataLocationDto.class));
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
	}
	
	@Override
	public List<CataLocationDto> getListDistrictByProvinceCode(CataLocationDto cataLocationDto) {
		List<CataLocationDto> lstResult = new ArrayList<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
    		String unitCode = null;
        	if(!bocUserDto.getListUnit().isEmpty()) {
    			unitCode = bocUserDto.getListUnit().get(0);
        	} else {
        		return lstResult;
        	}
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_TARGET, "get-data-district");
            Map<String, Object> parameters = new HashMap<String, Object>();
            if (cataLocationDto.getLstProvinceCodes() != null && cataLocationDto.getLstProvinceCodes().size() > 0) {
                parameters.put("p_province_code_list", String.join(",", cataLocationDto.getLstProvinceCodes()));
            } else {
            	parameters.put("p_province_code_list", "");
            }
            parameters.put("p_region_level", bocUserDto.getRegionLevel());
            parameters.put("p_unit_code", unitCode);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(CataLocationDto.class));
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
	}
	
	@Override
    public ResultDto delete(BocTargetDto bocTargetDto) {
        ResultDto resultDTO = new ResultDto();
        entityManager = getEntityManager();
        try {
        	for (int i = 0; i < bocTargetDto.getListIdDelete().size(); i++) {
        		BocTargetEntity e = entityManager.find(BocTargetEntity.class, Long.valueOf(bocTargetDto.getListIdDelete().get(i)));
                if (e != null){
                	entityManager.remove(e);
                }
			}
        	resultDTO.setKey(Constants.RESULT.SUCCESS);
        } catch (Exception ex) {
            resultDTO.setKey(Constants.RESULT.ERROR);
            resultDTO.setMessage(ex.getMessage());
            LOGGER.error(ex.getMessage(), ex);
        }
        return resultDTO;
    }
	
	@Override
	public List<BocTargetDto> getListForExport(BocTargetDto bocTargetDto) {
		List<BocTargetDto> lstResult = new ArrayList<>();
		try {
			String sqlQuery = "";
        	if (bocTargetDto.getListProvinceCodes() != null && bocTargetDto.getListProvinceCodes().size() > 0) {
        		sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_TARGET, "get-data-table-boc-target");
            } else {
            	sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_TARGET, "get-all-data-table-boc-target");
            }
        	
        	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getTargetType())){
        		sqlQuery += " AND BT.TARGET_TYPE = :targetType ";
            }
        	if ("T".equals(bocTargetDto.getMonthYear())){
        		sqlQuery += " AND BT.MONTH_YEAR = 'T' ";
        		if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && !StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYYMM') >= TO_DATE(:p_from_date, 'YYYYMM') ";
                }
            	if (!StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYYMM') <= TO_DATE(:p_to_date, 'YYYYMM') ";
                }
            	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYYMM') >= TO_DATE(:p_from_date, 'YYYYMM') ";
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYYMM') <= TO_DATE(:p_to_date, 'YYYYMM') ";
                }
        	}
        	if ("N".equals(bocTargetDto.getMonthYear())){
        		sqlQuery += " AND BT.MONTH_YEAR = 'N' ";
        		if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && !StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYY') >= TO_DATE(:p_from_date, 'YYYY') ";
                }
            	if (!StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYY') <= TO_DATE(:p_to_date, 'YYYY') ";
                }
            	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYY') >= TO_DATE(:p_from_date, 'YYYY') ";
                	sqlQuery += " AND TO_DATE(BT.TARGET_MONTH, 'YYYY') <= TO_DATE(:p_to_date, 'YYYY') ";
                }
        	}
        	Map<String, Object> parameters = new HashMap<>();
        	
        	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getTargetType())){
        		parameters.put("targetType", bocTargetDto.getTargetType());
            }
        	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && !StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
        		parameters.put("p_from_date", bocTargetDto.getFromDateString());
            }
        	if (!StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
            	parameters.put("p_to_date", bocTargetDto.getToDateString());
            }
        	if (StringUtils.isNotNullOrEmpty(bocTargetDto.getFromDateString()) && StringUtils.isNotNullOrEmpty(bocTargetDto.getToDateString())){
            	parameters.put("p_from_date", bocTargetDto.getFromDateString());
            	parameters.put("p_to_date", bocTargetDto.getToDateString());
            }
        	if (bocTargetDto.getListProvinceCodes() != null && bocTargetDto.getListProvinceCodes().size() > 0) {
                parameters.put("p_province_code_list", String.join(",", bocTargetDto.getListProvinceCodes()));
            } else {
            	parameters.put("p_province_code_list", "");
            }
        	if (bocTargetDto.getListDistrictCodes() != null && bocTargetDto.getListDistrictCodes().size() > 0) {
                parameters.put("p_district_code_list", String.join(",", bocTargetDto.getListDistrictCodes()));
            } else {
            	parameters.put("p_district_code_list", "");
            }
            lstResult = getNamedParameterJdbcTemplate().query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(BocTargetDto.class));
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public ResultDto insertList(List<BocTargetDto> list) {
        ResultDto resultDTO = new ResultDto();
        resultDTO.setKey(Constants.RESULT.SUCCESS);
        Session session = getSession();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        if (list != null && list.size() > 0) {
            try {
            	List<BocTargetDto> lsData = removeDuplicates(list);
                for (int i = 0; i < lsData.size(); i++) {
                	if("INSERT".equals(lsData.get(i).getTypeInsertOrUpdate())) {
                		BocTargetDto dto = lsData.get(i);
                    	BocTargetEntity entity = new BocTargetEntity();
                    	entity.setTargetMonth(dto.getTargetMonth());
                    	entity.setTargetType(dto.getTargetType());
                    	entity.setRegionCode(dto.getRegionCode());
                    	entity.setTargetNum(dto.getTargetNum());
                    	entity.setWarning1(dto.getWarning1());
                    	entity.setWarning2(dto.getWarning2());
                        entity.setCreatedUserName(userName);
                        entity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                        entity.setMonthYear(dto.getMonthYear());
                        session.save(entity);
                	} else if("UPDATE".equals(lsData.get(i).getTypeInsertOrUpdate())) {
                		entityManager = getEntityManager();
                		BocTargetDto dto = lsData.get(i);
                		List<BocTargetEntity> listEntity = findByMultilParam(BocTargetEntity.class,
                				"targetMonth", dto.getTargetMonth(),
                				"regionCode", dto.getRegionCode(),
                				"targetType", dto.getTargetType());
                		if(!listEntity.isEmpty()) {
                			listEntity.get(0).setTargetMonth(dto.getTargetMonth());
                			listEntity.get(0).setTargetType(dto.getTargetType());
                			listEntity.get(0).setRegionCode(dto.getRegionCode());
                			listEntity.get(0).setTargetNum(dto.getTargetNum());
                			listEntity.get(0).setWarning1(dto.getWarning1());
                			listEntity.get(0).setWarning2(dto.getWarning2());
                			listEntity.get(0).setCreatedUserName(userName);
                			listEntity.get(0).setCreatedDate(new Timestamp(System.currentTimeMillis()));
                			listEntity.get(0).setMonthYear(dto.getMonthYear());
                			entityManager.merge(listEntity.get(0));
                		}
                	}
                }
            } catch (Exception ex) {
                resultDTO.setKey(Constants.RESULT.ERROR);
                resultDTO.setMessage(ex.getMessage());
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        return resultDTO;
    }
	
	@SuppressWarnings("unchecked")
	public List<BocTargetDto> removeDuplicates(List<BocTargetDto> list) {
		Collections.reverse(list);
	    Set set = new TreeSet(new Comparator() {
	        @Override
	        public int compare(Object o1, Object o2) {
	            if(((BocTargetDto)o1).getTargetMonth().equals(((BocTargetDto)o2).getTargetMonth()) 
	            		&& ((BocTargetDto)o1).getRegionCode().equals(((BocTargetDto)o2).getRegionCode())
	            		&& ((BocTargetDto)o1).getTargetType().equals(((BocTargetDto)o2).getTargetType())) {
	                return 0;
	            }
	            return 1;
	        }
	    });
	    set.addAll(list);
	    final List newList = new ArrayList(set);
	    return newList;
	}
	
	@Override
	public List<KVDto> getListProvinceDistrictForExport() {
		List<KVDto> lstResult = new ArrayList<>();
		try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_TARGET, "get-list-province-district-for-export");
            lstResult = getNamedParameterJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(KVDto.class));
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
	}
	
	@Override
	public Boolean checkDuplicateImport(String regionCode, String targetMonth, String targetType) {
		Boolean check = false;
		try {
			List<BocTargetDto> list = new ArrayList<>();
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BOC_TARGET, "get-list-check-duplicate-boc-target");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("regionCode", regionCode);
            parameters.put("targetMonth", targetMonth);
            parameters.put("targetType", targetType);
            list = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(BocTargetDto.class));
            if(list.isEmpty()) {
            	check = true;
            }
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return check;
	}
}
