package com.viettel.api.repository.qldv.partner;

import com.viettel.api.domain.qldv.PartnerEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.PartnerDto;

public interface PartnerRepository {

    Datatable seachPartner(PartnerDto dto);

    ResultDto saveData(PartnerDto dto);

    PartnerEntity getDetail(PartnerDto dto);
}
