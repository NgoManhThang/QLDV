package com.viettel.api.dto.boc;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viettel.api.domain.boc.BocFilesEntity;
import com.viettel.api.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VTN-PTPM-NV24 on 2/23/2018.
 */
@Getter
@Setter
public class BocFilesDto extends BaseDto {
	private Long fileId;
    private Long groupId;
    private Long groupFile;
    private String fileName;
    private String filePath;
    private String createUser;
    private Timestamp createDate;
    private Long fileSize;
    
    public BocFilesDto(){
    	
    }
    
    public BocFilesEntity toEntity(){
        Logger log = LoggerFactory.getLogger(BocFilesDto.class);
        try {
        	BocFilesEntity entity = new BocFilesEntity(
        			fileId
            		, groupId
            		, groupFile
            		, fileName
            		, filePath
            		, createUser
            		, createDate
            		, fileSize
            );

            return entity;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }
}
