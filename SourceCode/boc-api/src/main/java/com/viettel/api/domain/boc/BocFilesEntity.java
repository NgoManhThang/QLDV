package com.viettel.api.domain.boc;

import static javax.persistence.GenerationType.SEQUENCE;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VTN-PTPM-NV24 on 2/23/2018.
 */
@Getter
@Setter
@Entity
@Table(name = "BOC_FILES")
public class BocFilesEntity {
    @SequenceGenerator(name = "generator", sequenceName = "BOC_FILES_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "FILE_ID", unique = true)
    private Long fileId;
    
    @Column(name = "GROUP_ID")
    private Long groupId;
    
    @Column(name = "GROUP_FILE")
    private Long groupFile;

    @Column(name = "FILE_NAME")
    private String fileName;
    
    @Column(name = "FILE_PATH")
    private String filePath;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "CREATE_DATE")
    private Timestamp createDate;
    
    @Column(name = "FILE_SIZE")
    private Long fileSize;

    public BocFilesEntity(){

    }
    public BocFilesEntity(
            Long fileId,
            Long groupId,
            Long groupFile,
            String fileName,
            String filePath,
            String createUser,
            Timestamp createDate,
            Long fileSize
    ){
        this.fileId = fileId;
        this.groupId = groupId;
        this.groupFile = groupFile;
        this.fileName = fileName;
        this.filePath = filePath;
        this.createUser = createUser;
        this.createDate = createDate;
        this.fileSize = fileSize;
    }
            
}
