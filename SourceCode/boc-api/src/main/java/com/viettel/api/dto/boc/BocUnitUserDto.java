package com.viettel.api.dto.boc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viettel.api.domain.boc.BocUnitUserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BocUnitUserDto {
	private Long unitUserId;
	private Long unitId;
	private Long userId;
	
	public BocUnitUserDto() {
    }
	
    public BocUnitUserEntity toEntity(){
        Logger log = LoggerFactory.getLogger(BocUnitUserDto.class);
        try {
        	BocUnitUserEntity entity = new BocUnitUserEntity(
        			unitUserId
            		, unitId
            		, userId
            );

            return entity;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }
}
