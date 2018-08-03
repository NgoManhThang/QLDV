package com.viettel.api.dto.boc;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viettel.api.domain.boc.BocUserEntity;
import com.viettel.api.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@Getter
@Setter
public class BocUserDto extends BaseDto {
    private Long userId;
    private String code;
    private String userName;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String position;
    private String createUser;
    private Timestamp createDate;
    private String updateUser;
    private Timestamp updateDate;
    private Long status;
    private Timestamp workingDate;
    private String company;
    private String roleTarget;
    
    private String rePassword;
    private Long fileId;
    private List<String> listRole;
    private String listRoleString;
    private List<String> listUnit;
    private String listUnitString;
    private List<String> listRoleTarget;
    private String listRoleTargetString;
    private Long regionLevel;
    private String positionName;
    private String companyName;
    private String workingDateString;
    
    private Long targetNotAchieved;
    private Long totalTarget;
    
    public BocUserDto() {
    }

    public BocUserEntity toEntity(){
        Logger log = LoggerFactory.getLogger(BocUserDto.class);
        try {
            BocUserEntity entity = new BocUserEntity(
            		userId
            		, code
            		, userName
            		, password
            		, fullName
            		, phoneNumber
            		, email
            		, position
            		, createUser
            		, createDate
            		, updateUser
            		, updateDate
            		, status
            		, workingDate
            		, company
            		, roleTarget
            );

            return entity;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }
}
