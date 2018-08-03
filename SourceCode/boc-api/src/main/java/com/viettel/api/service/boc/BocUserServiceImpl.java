package com.viettel.api.service.boc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.viettel.api.config.Constants;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocRoleDto;
import com.viettel.api.dto.boc.BocRoleTargetDto;
import com.viettel.api.dto.boc.BocUnitDto;
import com.viettel.api.dto.boc.BocUserDto;
import com.viettel.api.repository.boc.BocUserRepository;
import com.viettel.api.repository.boc.CommonRepository;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@Service
public class BocUserServiceImpl implements BocUserService{
	private final Logger log = LoggerFactory.getLogger(BocUserServiceImpl.class);

    @Autowired
    protected BocUserRepository bocUserRepository;
    
    @Autowired
    protected CommonRepository commonRepository;
    
    @Override
    public BocUserDto getUserByUserName(String userName) {
        log.debug("Request to getUserByUserName : {}", userName);
        
        return bocUserRepository.getUserByUserName(userName);
    }
    
    @Override
    public List<BocRoleDto> getListRoleByUserName(String userName) {
        log.debug("Request to getListRoleByUserName : {}", userName);
        
        return bocUserRepository.getListRoleByUserName(userName);
    }
    
    @Override
    public List<BocUnitDto> getListUnitByUserName(String userName) {
        log.debug("Request to getListUnitByUserName : {}", userName);
        
        return bocUserRepository.getListUnitByUserName(userName);
    }
    
    @Override
    public List<BocRoleTargetDto> getListRoleTargetByUserName(String userName) {
        log.debug("Request to getListRoleTargetByUserName : {}", userName);
        
        return bocUserRepository.getListRoleTargetByUserName(userName);
    }
    
    @Override
    public Datatable search(BocUserDto bocUserDto) {
        log.debug("Request to search : {}", bocUserDto);
        return bocUserRepository.search(bocUserDto);
    }
    
    @Override
    public ResultDto delete(Long userId) {
        log.debug("Request to delete : {}", userId);

        return bocUserRepository.delete(userId);
    }

    @Override
    public BocUserDto getDetail(Long userId) {
        log.debug("Request to getDetail : {}", userId);

        return bocUserRepository.getDetail(userId);
    }
    
    @Override
    public ResultDto save(List<MultipartFile> files, BocUserDto bocUserDto) {
        ResultDto resultDto = new ResultDto();
        resultDto.setKey(Constants.RESULT.SUCCESS);

        ResultDto sysDate = commonRepository.getDBSysDate();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // Them moi
            if (bocUserDto.getUserId() == null){
            	bocUserDto.setUserId(null);
            	bocUserDto.setCreateUser(auth.getName());
            	bocUserDto.setCreateDate(sysDate.getSystemDate());
            	bocUserDto.setUpdateUser(null);
            	bocUserDto.setUpdateDate(null);
            	bocUserDto.setStatus(1L);
                resultDto = bocUserRepository.save(files, bocUserDto);
            }
            // Cap nhat
            else{
            	bocUserDto.setUpdateUser(auth.getName());
                bocUserDto.setUpdateDate(sysDate.getSystemDate());
                resultDto = bocUserRepository.save(files, bocUserDto);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            resultDto.setKey(Constants.RESULT.ERROR);
        }
        return resultDto;
    }
}
