package com.viettel.api.service.qldv.employee;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.repository.qldv.employee.EmployeeRepository;
import com.viettel.api.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    MailService mailService;

    @Override
    public Datatable searchEmployee(EmployeeDto dto) {
        return employeeRepository.searchEmployee(dto);
    }

    @Override
    public ResultDto saveData(EmployeeDto dto) {
        return employeeRepository.saveData(dto);
    }

    @Override
    public EmployeeDto getDetail(EmployeeDto dto) {
        //Gửi mail
//        mailService.sendEmail("manhthangngo1994@gmail.com", "ABC", "XXX", false, true);
        return employeeRepository.getDetail(dto);
    }
}
