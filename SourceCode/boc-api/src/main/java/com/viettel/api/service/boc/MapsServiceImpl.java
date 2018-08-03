/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.api.service.boc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.viettel.api.repository.boc.MapsRepository;

/**
 * Created by TungPV on 04/01/2018.
 */
@Service
public class MapsServiceImpl implements MapsService {
	
	@Autowired
	MapsRepository mapsRepository;
    
    @Override
    public List<ProvinceDistrictDto> getGeometryProvince(String bocCode) {
        return mapsRepository.getGeometryProvince(bocCode);
    }
    
    @Override
    public List<ProvinceDistrictDto> getGeometryDistrict(String provinceCode, String bocCode) {
        return mapsRepository.getGeometryDistrict(provinceCode, bocCode);
    }
    
    @Override
    public List<KVDto> getKv() {
        return mapsRepository.getKv();
    }
    
    @Override
    public List<ProvinceDistrictDto> getProvince() {
        return mapsRepository.getProvince();
    }
    
    @Override
    public List<BocCodeDto> getBocCode() {
        return mapsRepository.getBocCode();
    }
    
    @Override
    public List<MonthTitleDto> getMonthTitle() {
        return mapsRepository.getMonthTitle();
    }
    
    @Override
    public List<BadBocRegionDto> getTopTenWorstBOC(String regionCode) {
        return mapsRepository.getTopTenWorstBOC(regionCode);
    }
    
    @Override
    public List<EmployeeInformationDto> getBadEmployeeWorstBOC(String regionCode) {
    	return mapsRepository.getBadEmployeeWorstBOC(regionCode);
    }
    
    @Override
    public List<BocModuleDto> getListBocModule() {
    	return mapsRepository.getListBocModule();
    }
    
    @Override
    public List<TargetDataDto> getListTargetsDistrict(String regionCode) {
    	return mapsRepository.getListTargetsDistrict(regionCode);
    }
    
    @Override
    public List<BocCodeDto> getBocTitle(String bocCodeGroup) {
    	return mapsRepository.getBocTitle(bocCodeGroup);
    }
    
    @Override
    public List<BocDataDto> getBocData(String regionCode, String bocCodeGroup) {
    	return mapsRepository.getBocData(regionCode, bocCodeGroup);
    }
    
    @Override
    public List<BocDataChartDto> getBocDataChart(BocDataChartDto bocDataChartDto) {
    	return mapsRepository.getBocDataChart(bocDataChartDto);
    }
}
