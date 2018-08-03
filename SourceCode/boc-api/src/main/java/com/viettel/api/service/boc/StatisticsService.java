/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.api.service.boc;

import java.util.List;

import com.viettel.api.dto.boc.BocUserDto;
import com.viettel.api.dto.boc.StatisticsTargetDto;
import com.viettel.api.dto.boc.TargetDataDto;

/**
 * Created by TungPV on 04/01/2018.
 */
public interface StatisticsService {
	
	List<TargetDataDto> getListTargetsStatistics(String regionCode, String bocCode, Long regionLevel);
	List<StatisticsTargetDto> getStatisticsTargets(String regionCode, String bocCode, Long regionLevel);
	List<BocUserDto> getListStatisticsInfoEmployee();
	List<StatisticsTargetDto> getStatisticsTargetsByBocCode(String regionCode, String bocCode, Long regionLevel);
}
