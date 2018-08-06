package com.viettel.api.dto.qldv;

import com.viettel.api.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto extends BaseDto{
    private String employeeId;
    private String userName;
    private String fullName;
    private String codeEmployee;
    private String email;
    private String phone;
}
