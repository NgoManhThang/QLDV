package com.viettel.api.dto.boc;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viettel.api.domain.boc.BocTargetEntity;
import com.viettel.api.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@Getter
@Setter
public class BocTargetDto extends BaseDto {
	private Long targetId;
    private String targetMonth;
    private String targetType;
    private String regionCode;
    private Float targetNum;
    private Float warning1;
    private Float warning2;
    private String createdUserName;
    private Timestamp createdDate;
    private String monthYear;
    
    private String createdFullName;
    private List<String> listProvinceCodes;
    private List<String> listDistrictCodes;
    private Timestamp fromDate;
    private Timestamp toDate;
    private String regionName;
    private String targetName;
    private String createdDateString;
    private List<Long> listIdDelete;
    private String fromDateString;
    private String toDateString;
    private String targetNumString;
    private String warning1String;
    private String warning2String;
    
    private String typeInsertOrUpdate;
    
    public BocTargetDto() {
    }

    public BocTargetEntity toEntity(){
        Logger log = LoggerFactory.getLogger(BocTargetDto.class);
        try {
        	BocTargetEntity entity = new BocTargetEntity(
        			targetId
            		, targetMonth
            		, targetType
            		, regionCode
            		, targetNum
            		, warning1
            		, warning2
            		, createdUserName
            		, createdDate
            		, monthYear
            );

            return entity;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }
}
