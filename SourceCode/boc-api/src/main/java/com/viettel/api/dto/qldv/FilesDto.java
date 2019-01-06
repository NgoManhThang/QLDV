package com.viettel.api.dto.qldv;

import com.viettel.api.domain.boc.BocFilesEntity;
import com.viettel.api.domain.qldv.FilesEntity;
import com.viettel.api.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

/**
 * Created by ManhThang on 10/28/2018.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilesDto extends CodeDecodeDto {
	private Long fileId;
    private Long groupId;
    private Long groupFile;
    private String fileName;
    private String filePath;
    private String createUser;
    private Timestamp createDate;
    private Long fileSize;

    public FilesEntity toEntity(){
        Logger log = LoggerFactory.getLogger(FilesDto.class);
        try {
            FilesEntity entity = new FilesEntity(
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
