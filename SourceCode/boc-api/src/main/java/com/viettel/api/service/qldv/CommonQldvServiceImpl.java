package com.viettel.api.service.qldv;

import com.viettel.api.dto.qldv.CodeDecodeDto;
import com.viettel.api.repository.qldv.CommonQldvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonQldvServiceImpl implements CommonQldvService {

    @Autowired
    CommonQldvRepository commonRepository;

    @Override
    public List<CodeDecodeDto> search(CodeDecodeDto dto) {
        return commonRepository.search(dto);
    }
}
