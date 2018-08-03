package com.viettel.api.dto.boc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viettel.api.domain.boc.BocRoleEntity;
import com.viettel.api.dto.TreeDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BocRoleDto extends TreeDto{
	private Long roleId;
	private String roleName;
	private String roleCode;
	private Long status;
	private Long parentRoleId;
	
	public BocRoleDto() {
    }
	
    public BocRoleEntity toEntity(){
        Logger log = LoggerFactory.getLogger(BocRoleDto.class);
        try {
        	BocRoleEntity entity = new BocRoleEntity(
        			roleId
            		, roleName
            		, roleCode
            		, status
            		, parentRoleId
            );

            return entity;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }
}
