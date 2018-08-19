package com.viettel.api.repository.qldv.unions;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.UnionsDto;

public interface UnionsRepository {
    Datatable search(UnionsDto dto);
}
