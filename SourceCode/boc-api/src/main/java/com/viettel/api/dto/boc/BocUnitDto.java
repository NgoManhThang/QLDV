package com.viettel.api.dto.boc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viettel.api.domain.boc.BocUnitEntity;
import com.viettel.api.dto.TreeDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BocUnitDto extends TreeDto{
	private Long unitId;
    private String unitName;
    private String unitCode;
    private Long parentUnitId;
    private Long mapUnitId;
    private String description;
    private Long status;
    
    private Long regionLevel;
	
	public BocUnitDto() {
    }
	
    public BocUnitEntity toEntity(){
        Logger log = LoggerFactory.getLogger(BocUnitDto.class);
        try {
        	BocUnitEntity entity = new BocUnitEntity(
        			unitId
            		, unitName
            		, unitCode
            		, parentUnitId
            		, mapUnitId
            		, description
            		, status
            );

            return entity;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }
}
