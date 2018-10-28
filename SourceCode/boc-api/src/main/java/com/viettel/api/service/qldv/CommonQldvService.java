package com.viettel.api.service.qldv;

import com.viettel.api.domain.qldv.FilesEntity;
import com.viettel.api.dto.qldv.CodeDecodeDto;
import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.dto.qldv.PlaceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommonQldvService {
    List<CodeDecodeDto> search (CodeDecodeDto dto);

    List<PlaceDto> getPlaceById(PlaceDto dto);

    List<EmployeeDto> getEmployeeByIdOrUserName(EmployeeDto dto);

    FilesEntity getFileById(Long id);
}
