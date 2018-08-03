/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.api.repository.boc;

import java.util.List;

import org.springframework.stereotype.Repository;

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

/**
 * Created by TungPV on 04/01/2018.
 */
@Repository
public interface MapsRepository {
    
    List<ProvinceDistrictDto> getGeometryProvince(String bocCode);
    List<ProvinceDistrictDto> getGeometryDistrict(String provinceCode, String bocCode);
    List<KVDto> getKv();
    List<ProvinceDistrictDto> getProvince();
    List<BocCodeDto> getBocCode();
    List<MonthTitleDto> getMonthTitle();
    List<BadBocRegionDto> getTopTenWorstBOC(String regionCode);
    List<EmployeeInformationDto> getBadEmployeeWorstBOC(String regionCode);
    List<BocModuleDto> getListBocModule();
    List<TargetDataDto> getListTargetsDistrict(String regionCode);
    List<BocCodeDto> getBocTitle(String bocCodeGroup);
    List<BocDataDto> getBocData(String regionCode, String bocCodeGroup);
    List<BocDataChartDto> getBocDataChart(BocDataChartDto bocDataChartDto);
}
