package com.viettel.api.repository.qldv.validate;

import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.dto.qldv.UnionsDto;

import java.util.List;

public interface ValidateRepository {
    List<UnionsDto> getEmployeeExistInUnions(UnionsDto dto);

    List<UnionsDto> getPartnerExistInUnions(UnionsDto dto);


}
