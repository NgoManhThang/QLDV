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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.viettel.api.config.Constants;
import com.viettel.api.dto.boc.BadBocRegionDto;
import com.viettel.api.dto.boc.BocCodeDto;
import com.viettel.api.dto.boc.BocDataChartDto;
import com.viettel.api.dto.boc.BocDataDto;
import com.viettel.api.dto.boc.BocModuleDto;
import com.viettel.api.dto.boc.TargetDataDto;
import com.viettel.api.dto.boc.EmployeeInformationDto;
import com.viettel.api.dto.boc.KVDto;
import com.viettel.api.dto.boc.MonthTitleDto;
import com.viettel.api.dto.boc.ProvinceDistrictDto;
import com.viettel.api.service.boc.MapsService;
import com.viettel.api.web.rest.BaseController;

/**
 * Created by TungPV on 04/01/2018.
 */
@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "maps")
public class MapsResource {
	
	@Autowired
	MapsService mapsService;

    @GetMapping("/getGeometryProvince")
    @Timed
    public ResponseEntity<List<ProvinceDistrictDto>> getGeometryProvince(String bocCode)
    		throws URISyntaxException {
    	List<ProvinceDistrictDto> data = mapsService.getGeometryProvince(bocCode);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getGeometryDistrict")
    @Timed
    public ResponseEntity<List<ProvinceDistrictDto>> getGeometryDistrict(String provinceCode, 
    		String bocCode) throws URISyntaxException {
    	List<ProvinceDistrictDto> data = mapsService.getGeometryDistrict(provinceCode, bocCode);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getKv")
    @Timed
    public ResponseEntity<List<KVDto>> getKv() throws URISyntaxException {
    	List<KVDto> data = mapsService.getKv();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getProvince")
    @Timed
    public ResponseEntity<List<ProvinceDistrictDto>> getProvince() throws URISyntaxException {
    	List<ProvinceDistrictDto> data = mapsService.getProvince();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getBocCode")
    @Timed
    public ResponseEntity<List<BocCodeDto>> getBocCode() throws URISyntaxException {
    	List<BocCodeDto> data = mapsService.getBocCode();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getMonthTitle")
    @Timed
    public ResponseEntity<List<MonthTitleDto>> getMonthTitle() throws URISyntaxException {
    	List<MonthTitleDto> data = mapsService.getMonthTitle();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getTopTenWorstBOC")
    @Timed
    public ResponseEntity<List<BadBocRegionDto>> getTopTenWorstBOC(String regionCode) throws URISyntaxException {
    	List<BadBocRegionDto> data = mapsService.getTopTenWorstBOC(regionCode);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getBadEmployeeWorstBOC")
    @Timed
    public ResponseEntity<List<EmployeeInformationDto>> getBadEmployeeWorstBOC(String regionCode) throws URISyntaxException {
    	List<EmployeeInformationDto> data = mapsService.getBadEmployeeWorstBOC(regionCode);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getListBocModule")
    @Timed
    public ResponseEntity<List<BocModuleDto>> getListBocModule() throws URISyntaxException {
    	List<BocModuleDto> data = mapsService.getListBocModule();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getListTargetsDistrict")
    @Timed
    public ResponseEntity<List<TargetDataDto>> getListTargetsDistrict(String regionCode) throws URISyntaxException {
    	List<TargetDataDto> data = mapsService.getListTargetsDistrict(regionCode);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getBocTitle")
    @Timed
    public ResponseEntity<List<BocCodeDto>> getBocTitle(String bocCodeGroup) throws URISyntaxException {
    	List<BocCodeDto> data = mapsService.getBocTitle(bocCodeGroup);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getBocData")
    @Timed
    public ResponseEntity<List<BocDataDto>> getBocData(String regionCode, String bocCodeGroup) throws URISyntaxException {
    	List<BocDataDto> data = mapsService.getBocData(regionCode, bocCodeGroup);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @PostMapping("/getBocDataChart")
    @Timed
    public ResponseEntity<List<BocDataChartDto>> getBocDataChart(@RequestBody BocDataChartDto bocDataChartDto) throws URISyntaxException {
    	List<BocDataChartDto> data = mapsService.getBocDataChart(bocDataChartDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
