package com.viettel.api.service.qldv.partner;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.PartnerDto;
import com.viettel.api.repository.qldv.partner.PartnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerServiceImpl implements PartnerService {

    Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Autowired
    PartnerRepository partnerRepository;

    @Override
    public Datatable seachPartner(PartnerDto dto) {
        return partnerRepository.seachPartner(dto);
    }
}
