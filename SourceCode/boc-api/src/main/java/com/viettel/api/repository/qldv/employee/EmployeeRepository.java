package com.viettel.api.repository.qldv.employee;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.EmployeeDto;

public interface EmployeeRepository {
    Datatable searchEmployee (EmployeeDto dto);
}
