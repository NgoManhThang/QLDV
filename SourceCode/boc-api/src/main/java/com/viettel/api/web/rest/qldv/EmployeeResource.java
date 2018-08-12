package com.viettel.api.web.rest.qldv;

import com.codahale.metrics.annotation.Timed;
import com.viettel.api.config.Constants;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.service.qldv.employee.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "employee")
public class EmployeeResource {
    Logger logger = LoggerFactory.getLogger(EmployeeResource.class);

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/search")
    @Timed
    public ResponseEntity<Datatable> search(@RequestBody EmployeeDto dto) throws URISyntaxException {
        Datatable data = employeeService.searchEmployee(dto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/saveData")
    @Timed
    public ResponseEntity<ResultDto> saveData(@RequestBody EmployeeDto dto){
        ResultDto resultDto = employeeService.saveData(dto);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @PostMapping("/getDetail")
    @Timed
    public ResponseEntity<EmployeeDto> getDetail(@RequestBody EmployeeDto dto){
        EmployeeDto employeeDto = employeeService.getDetail(dto);
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }
}
