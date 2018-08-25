package com.viettel.api.repository.qldv;

import com.viettel.api.dto.qldv.CodeDecodeDto;
import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.dto.qldv.PlaceDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonQldvRepository {
    List<CodeDecodeDto> search (CodeDecodeDto dto);

    List<PlaceDto> getPlaceById(PlaceDto dto);

    List<EmployeeDto> getEmployeeByIdOrUserName(EmployeeDto dto);
}
