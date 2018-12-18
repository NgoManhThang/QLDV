package com.viettel.api.service.qldv.lookup;

import com.viettel.api.domain.qldv.UnionsEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.LookupDto;
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

    /*@Override
    public ResultDto saveData(UnionsDto dto) {
        return unionsRepository.saveData(dto);
    }

    @Override
    public UnionsEntity getDetail(UnionsDto dto) {
        return unionsRepository.getDetail(dto);
    }

    @Override
    public ResultDto updateStatus(UnionsDto dto) {
        return unionsRepository.updateStatus(dto);
    }*/
}
