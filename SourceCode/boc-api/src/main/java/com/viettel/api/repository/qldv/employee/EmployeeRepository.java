package com.viettel.api.repository.qldv.employee;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.dto.qldv.FilesDto;

public interface EmployeeRepository {
    Datatable searchEmployee (EmployeeDto dto);

    ResultDto saveData (EmployeeDto dto);

    EmployeeDto getDetail (EmployeeDto dto);

    ResultDto delete(EmployeeDto dto);
}
