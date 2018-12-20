package com.viettel.api.service.qldv.lookup;

import com.viettel.api.domain.qldv.UnionsEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.LookupDto;
import com.viettel.api.dto.qldv.MemberDto;
import com.viettel.api.dto.qldv.UnionsDto;
import com.viettel.api.repository.qldv.lookup.LookupRepository;
import com.viettel.api.repository.qldv.unions.UnionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LookupServiceImpl implements LookupService {
    Logger logger = LoggerFactory.getLogger(LookupServiceImpl.class);

    @Autowired
    LookupRepository lookupRepository;

    @Override
    public Datatable search(LookupDto dto) {
        return lookupRepository.search(dto);
    }

    @Override
    public Datatable searchMember(MemberDto dto) {
        return lookupRepository.searchMember(dto);
    }

    @Override
    public LookupDto scanBarcode(LookupDto dto) {
        LookupDto lookupDto = lookupRepository.scanBarcode(dto);
        if (lookupDto.getUnionMemberId() != null) {
            if ("IN".equals(dto.getTypeScan())) {
                lookupRepository.save(lookupDto);
            } else {
                lookupRepository.update(lookupDto);
            }
        }
        return lookupDto;
    }

}
