package com.viettel.api.domain.boc;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@Entity
@Getter
@Setter
@Table(name = "BOC_ROLE_TARGET")
public class BocRoleTargetEntity {
	@SequenceGenerator(name = "generator", sequenceName = "BOC_ROLE_TARGET_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "ROLE_TARGET_ID", unique = true)
    private Long roleTargetId;

    @Column(name = "ROLE_TARGET_NAME")
    private String roleTargetName;

    @Column(name = "ROLE_TARGET_CODE")
    private String roleTargetCode;

    @Column(name = "STATUS")
    private Long status;
    
    @Column(name = "PARENT_ROLE_TARGET_ID")
    private Long parentRoleTargetId;
    
    public BocRoleTargetEntity(){
    	
    }

    public BocRoleTargetEntity(
    		Long roleTargetId
    		, String roleTargetName
    		, String roleTargetCode
    		, Long status
    		, Long parentRoleTargetId
    ){
        this.roleTargetId = roleTargetId;
        this.roleTargetName = roleTargetName;
        this.roleTargetCode = roleTargetCode;
        this.status = status;
        this.parentRoleTargetId = parentRoleTargetId;
    }
}
