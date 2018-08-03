package com.viettel.api.repository.boc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocConstantDto;
import com.viettel.api.dto.boc.BocFilesDto;
import com.viettel.api.dto.boc.BocRoleDto;
import com.viettel.api.dto.boc.BocRoleTargetDto;
import com.viettel.api.dto.boc.BocUnitDto;

/**
 * Created by VTN-PTPM-NV04 on 2/7/2018.
 */
@Repository
public interface CommonRepository {

    ResultDto getDBSysDate();
    BocFilesDto getFileById(Long fileId);
    List<BocRoleDto> getListBocRole();
    List<BocUnitDto> getListBocUnit();
    List<BocRoleTargetDto> getListBocRoleTarget();
    List<BocConstantDto> getListBocConstant(String constantType);
    
    void keepAlive();
}
