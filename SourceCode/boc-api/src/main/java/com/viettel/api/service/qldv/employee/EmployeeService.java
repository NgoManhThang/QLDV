package com.viettel.api.service.qldv.employee;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.EmployeeDto;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    Datatable searchEmployee (EmployeeDto dto);

    ResultDto saveData (EmployeeDto dto);

    EmployeeDto getDetail (EmployeeDto dto);

    ResultDto validateRight(EmployeeDto dto, String action);
}
