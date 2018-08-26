package com.viettel.api.service.qldv.employee;

import com.viettel.api.config.Constants;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.dto.qldv.UnionsDto;
import com.viettel.api.repository.qldv.CommonQldvRepository;
import com.viettel.api.repository.qldv.employee.EmployeeRepository;
import com.viettel.api.repository.qldv.validate.ValidateRepository;
import com.viettel.api.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ValidateRepository validateRepository;

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

    @Override
    public ResultDto delete(EmployeeDto dto) {
        ResultDto resultDto = new ResultDto();
        resultDto = validateRight(dto, "DELETE");
        if (Constants.RESULT.SUCCESS.equals(resultDto.getKey())) {
            resultDto = employeeRepository.delete(dto);
        }
        return resultDto;
    }

    @Override
    public ResultDto validateRight(EmployeeDto dto, String action) {
        ResultDto resultDto = new ResultDto();
        resultDto.setKey(Constants.RESULT.SUCCESS);
        if (Constants.VALIDATE.DELETE.equals(action)) {
            //If employee exist in unions then no can delete
            UnionsDto unionsDto = new UnionsDto();
            unionsDto.setEmployeeId(dto.getEmployeeId());
            List<UnionsDto> lst = validateRepository.getEmployeeExistInUnions(unionsDto);
            if (lst != null && lst.size() > 0) {
                resultDto.setKey(Constants.RESULT.NO_CAN_DELETE);
                return resultDto;
            }
        }
        return resultDto;
    }
}
