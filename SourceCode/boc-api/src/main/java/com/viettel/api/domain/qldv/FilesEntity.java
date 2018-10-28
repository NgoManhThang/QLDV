package com.viettel.api.domain.qldv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by VTN-PTPM-NV24 on 2/23/2018.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QLDV_FILES")
public class FilesEntity {
    @SequenceGenerator(name = "generator", sequenceName = "FILES_SEQ", allocationSize = 1)
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

    @Column(name = "CREATE_USER", insertable = true, updatable = false)
    private String createUser;

    @CreationTimestamp
    @Column(name = "CREATE_DATE", insertable = true, updatable = false)
    private Timestamp createDate;

    @Column(name = "FILE_SIZE")
    private Long fileSize;
}

