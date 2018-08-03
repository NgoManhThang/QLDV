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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.api.dto.boc.BocUserDto;
import com.viettel.api.dto.boc.StatisticsTargetDto;
import com.viettel.api.dto.boc.TargetDataDto;
import com.viettel.api.repository.BaseRepository;
import com.viettel.api.utils.SQLBuilder;

/**
 * Created by TungPV on 04/01/2018.
 */
@Repository
@Transactional
@SuppressWarnings("rawtypes")
public class StatisticsRepositoryImpl extends BaseRepository implements StatisticsRepository {

	private static final Logger LOGGER = Logger.getLogger(StatisticsRepositoryImpl.class);

	@Override
    public List<TargetDataDto> getListTargetsStatistics(String regionCode, String bocCode, Long regionLevel) {
        List<TargetDataDto> lstResult = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_STATISTICS, "get-list-targets-statistics");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_region_code", regionCode);
            //parameters.put("p_boc_code", bocCode);
            parameters.put("p_region_level", regionLevel);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(TargetDataDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
	
	@Override
    public List<StatisticsTargetDto> getStatisticsTargets(String regionCode, String bocCode, Long regionLevel) {
        List<StatisticsTargetDto> lstResult = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_STATISTICS, "get-statistics-targets");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_region_code", regionCode);
            //parameters.put("p_boc_code", bocCode);
            parameters.put("p_region_level", regionLevel);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(StatisticsTargetDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
	
	@Override
    public List<BocUserDto> getListStatisticsInfoEmployee() {
        List<BocUserDto> lstResult = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_STATISTICS, "get-statistics-info-employees");
            lstResult = getNamedParameterJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(BocUserDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
	
	@Override
    public List<StatisticsTargetDto> getStatisticsTargetsByBocCode(String regionCode, String bocCode, Long regionLevel) {
        List<StatisticsTargetDto> lstResult = new ArrayList<>();
        try {
            String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_STATISTICS, "get-statistics-targets-by-boc-code");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("p_region_code", regionCode);
            parameters.put("p_boc_code", bocCode);
            //parameters.put("p_region_level", regionLevel);
            lstResult = getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(StatisticsTargetDto.class));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
}
