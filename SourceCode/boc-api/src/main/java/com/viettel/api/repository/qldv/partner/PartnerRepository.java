package com.viettel.api.repository.qldv.partner;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.PartnerDto;

public interface PartnerRepository {

    Datatable seachPartner(PartnerDto dto);
}
