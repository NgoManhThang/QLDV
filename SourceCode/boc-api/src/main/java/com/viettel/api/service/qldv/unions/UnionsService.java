package com.viettel.api.service.qldv.unions;

import com.viettel.api.domain.qldv.UnionsEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.UnionsDto;

public interface UnionsService {
    Datatable search(UnionsDto dto);

    ResultDto saveData(UnionsDto dto);

    UnionsEntity getDetail(UnionsDto dto);

    ResultDto updateStatus(UnionsDto dto);
}
