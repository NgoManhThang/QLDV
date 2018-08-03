package com.viettel.api.dto.boc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viettel.api.domain.boc.BocRoleTargetEntity;
import com.viettel.api.dto.TreeDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BocRoleTargetDto extends TreeDto{
	private Long roleTargetId;
	private String roleTargetName;
	private String roleTargetCode;
	private Long status;
	private Long parentRoleTargetId;
	
	public BocRoleTargetDto() {
    }
	
    public BocRoleTargetEntity toEntity(){
        Logger log = LoggerFactory.getLogger(BocRoleTargetDto.class);
        try {
        	BocRoleTargetEntity entity = new BocRoleTargetEntity(
        			roleTargetId
            		, roleTargetName
            		, roleTargetCode
            		, status
            		, parentRoleTargetId
            );

            return entity;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }
}
