package com.viettel.api.repository.boc;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocRoleDto;
import com.viettel.api.dto.boc.BocRoleTargetDto;
import com.viettel.api.dto.boc.BocUnitDto;
import com.viettel.api.dto.boc.BocUserDto;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@Repository
public interface BocUserRepository {
	BocUserDto getUserByUserName(String userName);
	List<BocRoleDto> getListRoleByUserName(String userName);
	List<BocUnitDto> getListUnitByUserName(String userName);
	List<BocRoleTargetDto> getListRoleTargetByUserName(String userName);
    Datatable search(BocUserDto bocUserDto);
    ResultDto delete(Long userId);
    BocUserDto getDetail(Long userId);
    ResultDto save(List<MultipartFile> files, BocUserDto bocUserDto);
}
