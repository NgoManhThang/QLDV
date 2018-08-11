package com.viettel.api.service.qldv;

import com.viettel.api.dto.qldv.CodeDecodeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommonQldvService {
    List<CodeDecodeDto> search (CodeDecodeDto dto);
}
