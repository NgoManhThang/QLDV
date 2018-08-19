package com.viettel.api.service.qldv.unions;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.UnionsDto;

public interface UnionsService {
    Datatable search(UnionsDto dto);
}
