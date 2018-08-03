package com.viettel.api.service.boc;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocCodeDto;
import com.viettel.api.dto.boc.BocTargetDto;
import com.viettel.api.dto.boc.CataLocationDto;
import com.viettel.api.dto.boc.KVDto;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@Service
public interface BocTargetService {
    Datatable search(BocTargetDto bocTargetDto);
    List<BocCodeDto> getTypeTarget();
    List<CataLocationDto> getListProvince();
    List<CataLocationDto> getListDistrictByProvinceCode(CataLocationDto cataLocationDto);
    ResultDto delete(BocTargetDto bocTargetDto);
    File exportTarget(BocTargetDto bocTargetDto) throws Exception;
    ResultDto importData(MultipartFile uploadfile);
    List<KVDto> getListProvinceDistrictForExport();
}
