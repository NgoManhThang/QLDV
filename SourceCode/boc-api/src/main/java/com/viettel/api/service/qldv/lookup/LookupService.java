package com.viettel.api.service.qldv.lookup;

import com.viettel.api.domain.qldv.UnionsEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.LookupDto;
import com.viettel.api.dto.qldv.UnionsDto;

public interface LookupService {
    Datatable search(LookupDto dto);

    /*ResultDto saveData(UnionsDto dto);

    UnionsEntity getDetail(UnionsDto dto);

    ResultDto updateStatus(UnionsDto dto);*/
}
