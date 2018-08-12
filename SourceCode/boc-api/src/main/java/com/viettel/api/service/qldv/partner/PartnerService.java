package com.viettel.api.service.qldv.partner;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.PartnerDto;

public interface PartnerService {
    Datatable seachPartner(PartnerDto dto);
}
