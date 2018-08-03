/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.api.repository.boc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.api.dto.boc.BadBocRegionDto;
import com.viettel.api.dto.boc.BocCodeDto;
import com.viettel.api.dto.boc.BocDataChartDto;
import com.viettel.api.dto.boc.BocDataDto;
import com.viettel.api.dto.boc.BocModuleDto;
import com.viettel.api.dto.boc.BocUserDto;
import com.viettel.api.dto.boc.TargetDataDto;
import com.viettel.api.dto.boc.EmployeeInformationDto;
import com.viettel.api.dto.boc.KVDto;
import com.viettel.api.dto.boc.MonthTitleDto;
import com.viettel.api.dto.boc.ProvinceDistrictDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.SQLBuilder;
import com.viettel.api.utils.StringUtils;

/**
 * Created by TungPV on 04/01/2018.
 */
@Repository
@Transactional
@SuppressWarnings("rawtypes")
public class MapsRepositoryImpl extends BaseRepository implements MapsRepository {

	private static final Logger LOGGER = Logger.getLogger(MapsRepositoryImpl.class);

	@Override
	public List<ProvinceDistrictDto> getGeometryProvince(String bocCode) {
		List<ProvinceDistrictDto> lstResult = new ArrayList<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
    		String unitCode = null;
        	if(!bocUserDto.getListUnit().isEmpty()) {
    			unitCode = bocUserDto.getListUnit().get(0);
        	} else {
        		return lstResult;
        	}
        	
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "get-geometry-province-data");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_boc_code", bocCode);
            parameters.put("p_unit_code", unitCode);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(ProvinceDistrictDto.class));
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
	}
	
	@Override
	public List<ProvinceDistrictDto> getGeometryDistrict(String provinceCode, String bocCode) {
		List<ProvinceDistrictDto> lstResult = new ArrayList<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
    		String unitCode = null;
        	if(!bocUserDto.getListUnit().isEmpty()) {
    			unitCode = bocUserDto.getListUnit().get(0);
        	} else {
        		return lstResult;
        	}
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "get-geometry-district-data");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_province_code", provinceCode);
            parameters.put("p_boc_code", bocCode);
            if(bocUserDto.getRegionLevel() == 2L) {
            	parameters.put("p_unit_code", unitCode);
        	} else {
        		parameters.put("p_unit_code", null);
        	}
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(ProvinceDistrictDto.class));
        } catch (Exception ex) {
        	LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
	}
	
	@Override
	public List<KVDto> getKv() {
		List<KVDto> lstResult = new ArrayList<>();
		try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "get-kv-data");
            lstResult = getNamedParameterJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(KVDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
	}
	
	@Override
	public List<ProvinceDistrictDto> getProvince() {
		List<ProvinceDistrictDto> lstResult = new ArrayList<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
    		String unitCode = null;
        	if(!bocUserDto.getListUnit().isEmpty()) {
    			unitCode = bocUserDto.getListUnit().get(0);
        	} else {
        		return lstResult;
        	}
        	if(bocUserDto.getRegionLevel() == 2L) {
        		String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "get-province-from-district-data");
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("p_unit_code", unitCode);
                lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(ProvinceDistrictDto.class));
        	} else {
        		String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "get-province-data");
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("p_unit_code", unitCode);
                lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(ProvinceDistrictDto.class));
        	}
        	for (int i = 0; i < lstResult.size(); i++) {
        		lstResult.get(0).setRegionLevel(bocUserDto.getRegionLevel());
			}
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
	}
	
	@Override
	public List<BocCodeDto> getBocCode() {
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
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "get-boc-code-data");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_list_boc_code", listBocCode);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(BocCodeDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
	}
	
	@Override
    public List<MonthTitleDto> getMonthTitle() {
        List<MonthTitleDto> lstResult = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "frame1/top-ten-worst-boc-heading");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_date_group_type", "M");
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(MonthTitleDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
	
	@Override
    public List<BadBocRegionDto> getTopTenWorstBOC(String regionCode) {
        List<BadBocRegionDto> lstResult = new ArrayList<>();
        try {
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
    		String unitCode = null;
        	if(!bocUserDto.getListUnit().isEmpty()) {
    			unitCode = bocUserDto.getListUnit().get(0);
        	} else {
        		return lstResult;
        	}
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "frame1/top-ten-worst-boc-data");
            Map<String, Object> parameters = new HashMap<String, Object>();
            if ("ALL".equals(regionCode)) {
                parameters.put("p_province_code", null);
            } else {
                parameters.put("p_province_code", regionCode);
            }
            parameters.put("p_region_level", bocUserDto.getRegionLevel());
            parameters.put("p_unit_code", unitCode);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(BadBocRegionDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
	
	@Override
    public List<EmployeeInformationDto> getBadEmployeeWorstBOC(String regionCode) {
        List<EmployeeInformationDto> lstResult = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "get-bad-employee");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_region_code", regionCode);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(EmployeeInformationDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
	
	@Override
	public List<BocModuleDto> getListBocModule() {
		List<BocModuleDto> lstResult = new ArrayList<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
    		List<String> listBocCode = new ArrayList<>();
        	if(!bocUserDto.getListRoleTarget().isEmpty()) {
        		listBocCode = bocUserDto.getListRoleTarget();
        	} else {
        		return lstResult;
        	}
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "get-list-boc-module-data");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_list_boc_code", listBocCode);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(BocModuleDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
	}
	
	@Override
    public List<TargetDataDto> getListTargetsDistrict(String regionCode) {
        List<TargetDataDto> lstResult = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "get-list-targets-district-data");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_district_code", regionCode);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(TargetDataDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
	
	@Override
    public List<BocCodeDto> getBocTitle(String bocCodeGroup) {
        List<BocCodeDto> lstResult = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "frame2/get-boc-heading");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_boc_code_group", bocCodeGroup);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(BocCodeDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
	
	@Override
    public List<BocDataDto> getBocData(String regionCode, String bocCodeGroup) {
		List<BocDataDto> lstResult = new ArrayList<>();
        try {
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		BocUserDto bocUserDto = (BocUserDto) auth.getDetails();
    		String unitCode = null;
        	if(!bocUserDto.getListUnit().isEmpty()) {
    			unitCode = bocUserDto.getListUnit().get(0);
        	} else {
        		return lstResult;
        	}
        	
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "frame2/get-boc-data");

            Map<String, Object> parameters = new HashMap<String, Object>();
            if("ALL".equals(regionCode)) {
            	parameters.put("p_province_code", null);
            } else {
            	parameters.put("p_province_code", regionCode);
            }
            parameters.put("p_boc_code_group", bocCodeGroup);
            parameters.put("p_region_level", bocUserDto.getRegionLevel());
            parameters.put("p_unit_code", unitCode);
            List<BocDataDto> list = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(BocDataDto.class));
            for (BocDataDto bocDataDto : list) {
            	bocDataDto.setActual(StringUtils.convertStringToArray(bocDataDto.getActualList()));
            	bocDataDto.setResult(StringUtils.convertStringToArray(bocDataDto.getResultList()));
            	bocDataDto.setTarget(StringUtils.convertStringToArray(bocDataDto.getTargetList()));
                lstResult.add(bocDataDto);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
	
	@Override
    public List<BocDataChartDto> getBocDataChart(BocDataChartDto bocDataChartDto) {
		List<BocDataChartDto> lstResult = new ArrayList<>();
        try {
            String sql = "";
            Map<String, Object> parameters = new HashMap<String, Object>();
            if("CHART_XT".equals(bocDataChartDto.getService())) {
            	sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "chart/get-boc-data-graph");
                parameters.put("p_province_code", bocDataChartDto.getProvinceCode());
                parameters.put("p_district_code", bocDataChartDto.getDistrictCode());
                if(bocDataChartDto.getProvinceCode() == null && bocDataChartDto.getDistrictCode() == null) {
                	parameters.put("p_region_level", "0");
                } else if(bocDataChartDto.getProvinceCode() != null && bocDataChartDto.getDistrictCode() == null) {
                	parameters.put("p_region_level", "1");
                } else {
                	parameters.put("p_region_level", null);
                }
                parameters.put("p_from_date", bocDataChartDto.getFromDate());
                parameters.put("p_to_date", bocDataChartDto.getToDate());
                parameters.put("p_boc_code", bocDataChartDto.getBocCode());
                parameters.put("p_date_group_type", "M");
            }
            if("CHART_LK".equals(bocDataChartDto.getService())) {
            	sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "chart/get-boc-accumulated-data-graph");
                parameters.put("p_province_code", bocDataChartDto.getProvinceCode());
                parameters.put("p_district_code", bocDataChartDto.getDistrictCode());
                if(bocDataChartDto.getProvinceCode() == null && bocDataChartDto.getDistrictCode() == null) {
                	parameters.put("p_region_level", "0");
                } else if(bocDataChartDto.getProvinceCode() != null && bocDataChartDto.getDistrictCode() == null) {
                	parameters.put("p_region_level", "1");
                } else {
                	parameters.put("p_region_level", null);
                }
                parameters.put("p_from_date", bocDataChartDto.getFromDate());
                parameters.put("p_to_date", bocDataChartDto.getToDate());
                parameters.put("p_boc_code", bocDataChartDto.getBocCode());
                parameters.put("p_date_group_type", "M");
            }
            if("CHART_REGION".equals(bocDataChartDto.getService())) {
            	sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MAPS, "chart/get-boc-region-data-graph");
                parameters.put("p_province_code", bocDataChartDto.getProvinceCode());
                if(bocDataChartDto.getProvinceCode() == null) {
                	parameters.put("p_region_level", "0");
                } else {
                	parameters.put("p_region_level", "1");
                }
                parameters.put("p_boc_code", bocDataChartDto.getBocCode());
            }
            
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(BocDataChartDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
}
