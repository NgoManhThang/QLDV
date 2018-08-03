/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.api.web.rest.boc;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.viettel.api.config.Constants;
import com.viettel.api.dto.boc.BocUserDto;
import com.viettel.api.dto.boc.StatisticsTargetDto;
import com.viettel.api.dto.boc.TargetDataDto;
import com.viettel.api.service.boc.StatisticsService;

/**
 * Created by TungPV on 04/01/2018.
 */
@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "statistics")
public class StatisticsResource {
	
	@Autowired
	StatisticsService statisticsService;

	@GetMapping("/getListTargetsStatistics")
    @Timed
    public ResponseEntity<List<TargetDataDto>> getListTargetsStatistics(String regionCode, String bocCode, Long regionLevel) throws URISyntaxException {
    	List<TargetDataDto> data = statisticsService.getListTargetsStatistics(regionCode, bocCode, regionLevel);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
	
	@GetMapping("/getStatisticsTargets")
    @Timed
    public ResponseEntity<List<StatisticsTargetDto>> getStatisticsTargets(String regionCode, String bocCode, Long regionLevel) throws URISyntaxException {
    	List<StatisticsTargetDto> data = statisticsService.getStatisticsTargets(regionCode, bocCode, regionLevel);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
	
	@GetMapping("/getListStatisticsInfoEmployee")
    @Timed
    public ResponseEntity<List<BocUserDto>> getListStatisticsInfoEmployee() throws URISyntaxException {
    	List<BocUserDto> data = statisticsService.getListStatisticsInfoEmployee();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
	
	@GetMapping("/getStatisticsTargetsByBocCode")
    @Timed
    public ResponseEntity<List<StatisticsTargetDto>> getStatisticsTargetsByBocCode(String regionCode, String bocCode, Long regionLevel) throws URISyntaxException {
    	List<StatisticsTargetDto> data = statisticsService.getStatisticsTargetsByBocCode(regionCode, bocCode, regionLevel);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
