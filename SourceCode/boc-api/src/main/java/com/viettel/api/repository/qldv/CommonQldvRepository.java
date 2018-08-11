package com.viettel.api.repository.qldv;

import com.viettel.api.dto.qldv.CodeDecodeDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonQldvRepository {
    List<CodeDecodeDto> search (CodeDecodeDto dto);
}
