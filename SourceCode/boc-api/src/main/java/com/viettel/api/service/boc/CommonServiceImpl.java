package com.viettel.api.service.boc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocConstantDto;
import com.viettel.api.dto.boc.BocFilesDto;
import com.viettel.api.dto.boc.BocRoleDto;
import com.viettel.api.dto.boc.BocRoleTargetDto;
import com.viettel.api.dto.boc.BocUnitDto;
import com.viettel.api.repository.boc.CommonRepository;

/**
 * Created by VTN-PTPM-NV04 on 2/7/2018.
 */
@Service
public class CommonServiceImpl implements CommonService {
    private final Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    protected CommonRepository commonRepository;

    @Override
    public ResultDto getDBSysDate() {
    	log.debug("Request to getDBSysDate : {}");
        return commonRepository.getDBSysDate();
    }
    
    @Override
    public BocFilesDto getFileById(Long fileId) {
    	return commonRepository.getFileById(fileId);
    }
    
    @Override
    public List<BocRoleDto> getListBocRole() {
    	return commonRepository.getListBocRole();
    }
    
    @Override
    public List<BocUnitDto> getListBocUnit() {
    	return commonRepository.getListBocUnit();
    }
    
    @Override
    public List<BocRoleTargetDto> getListBocRoleTarget() {
    	return commonRepository.getListBocRoleTarget();
    }
    
    @Override
    public List<BocConstantDto> getListBocConstant(String constantType) {
    	return commonRepository.getListBocConstant(constantType);
    }
}
