package com.viettel.api.service.qldv.employee;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.dto.qldv.FilesDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface EmployeeService {
    Datatable searchEmployee (EmployeeDto dto);

    ResultDto saveData (EmployeeDto dto, MultipartFile file) throws IOException;

    EmployeeDto getDetail (EmployeeDto dto);

    ResultDto delete(EmployeeDto dto);

    ResultDto validateRight(EmployeeDto dto, String action);
}
