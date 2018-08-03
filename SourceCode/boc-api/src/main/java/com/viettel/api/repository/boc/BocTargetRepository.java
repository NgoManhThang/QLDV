package com.viettel.api.repository.boc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocCodeDto;
import com.viettel.api.dto.boc.BocTargetDto;
import com.viettel.api.dto.boc.CataLocationDto;
import com.viettel.api.dto.boc.KVDto;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@Repository
public interface BocTargetRepository {
    Datatable search(BocTargetDto bocTargetDto);
    List<BocCodeDto> getTypeTarget();
    List<CataLocationDto> getListProvince();
    List<CataLocationDto> getListDistrictByProvinceCode(CataLocationDto cataLocationDto);
    ResultDto delete(BocTargetDto bocTargetDto);
    List<BocTargetDto> getListForExport(BocTargetDto bocTargetDto);
    ResultDto insertList(List<BocTargetDto> lsData);
    List<KVDto> getListProvinceDistrictForExport();
    Boolean checkDuplicateImport(String regionCode, String targetMonth, String targetType);
}
