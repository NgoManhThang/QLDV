package com.viettel.api.service.qldv;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.EmployeeDto;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    Datatable searchEmployee (EmployeeDto dto);
}