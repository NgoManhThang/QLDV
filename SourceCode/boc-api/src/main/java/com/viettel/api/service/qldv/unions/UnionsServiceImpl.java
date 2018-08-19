package com.viettel.api.service.qldv.unions;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.UnionsDto;
import com.viettel.api.repository.qldv.unions.UnionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnionsServiceImpl implements UnionsService {
    Logger logger = LoggerFactory.getLogger(UnionsServiceImpl.class);

    @Autowired
    UnionsRepository unionsRepository;

    @Override
    public Datatable search(UnionsDto dto) {
        return unionsRepository.search(dto);
    }
}
