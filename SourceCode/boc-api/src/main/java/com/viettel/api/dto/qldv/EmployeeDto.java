package com.viettel.api.dto.qldv;

import com.viettel.api.domain.qldv.EmployeeEntity;
import com.viettel.api.dto.BaseDto;
import com.viettel.api.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

@Getter
@Setter
public class EmployeeDto extends BaseDto {
    private String employeeId;
    private String code;
    private Long status;
    private String userName;
    private String password;
    private String fullName;
    private String codeEmployee;
    private String email;
    private String phone;
    private String position;
    private String createUser;
    private String updateUser;
    private Timestamp createDate;
    private Timestamp updateDate;


    public EmployeeEntity toEntity() {
        Logger logger = LoggerFactory.getLogger(EmployeeDto.class);
        try {
            EmployeeEntity entity = new EmployeeEntity(
                    StringUtils.isNotNullOrEmpty(employeeId) ? Long.valueOf(employeeId) : null,
                    code,
                    fullName,
                    phone,
                    email,
                    position,
                    createUser,
                    createDate,
                    updateUser,
                    updateDate,
                    userName,
                    password,
                    status
            );
            return entity;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
