package com.viettel.api.dto.boc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viettel.api.domain.boc.BocRoleUserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BocRoleUserDto {
	private Long roleUserId;
	private Long roleId;
	private Long userId;
	
	public BocRoleUserDto() {
    }
	
    public BocRoleUserEntity toEntity(){
        Logger log = LoggerFactory.getLogger(BocRoleUserDto.class);
        try {
        	BocRoleUserEntity entity = new BocRoleUserEntity(
        			roleUserId
            		, roleId
            		, userId
            );

            return entity;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }
}
