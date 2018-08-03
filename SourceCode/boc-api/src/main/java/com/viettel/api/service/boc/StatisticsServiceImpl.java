/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.api.service.boc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettel.api.dto.boc.BocUserDto;
import com.viettel.api.dto.boc.StatisticsTargetDto;
import com.viettel.api.dto.boc.TargetDataDto;
import com.viettel.api.repository.boc.StatisticsRepository;

/**
 * Created by TungPV on 04/01/2018.
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
	
	@Autowired
	StatisticsRepository statisticsRepository;
    
	@Override
    public List<TargetDataDto> getListTargetsStatistics(String regionCode, String bocCode, Long regionLevel) {
        return statisticsRepository.getListTargetsStatistics(regionCode, bocCode, regionLevel);
    }
	
	@Override
    public List<StatisticsTargetDto> getStatisticsTargets(String regionCode, String bocCode, Long regionLevel) {
        return statisticsRepository.getStatisticsTargets(regionCode, bocCode, regionLevel);
    }
	
	@Override
    public List<BocUserDto> getListStatisticsInfoEmployee() {
        return statisticsRepository.getListStatisticsInfoEmployee();
    }
	
	@Override
    public List<StatisticsTargetDto> getStatisticsTargetsByBocCode(String regionCode, String bocCode, Long regionLevel) {
        return statisticsRepository.getStatisticsTargetsByBocCode(regionCode, bocCode, regionLevel);
    }
}
