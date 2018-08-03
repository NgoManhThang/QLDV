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
@Table(name = "BOC_ROLE")
public class BocRoleEntity {
	@SequenceGenerator(name = "generator", sequenceName = "BOC_ROLE_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "ROLE_ID", unique = true)
    private Long roleId;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "ROLE_CODE")
    private String roleCode;

    @Column(name = "STATUS")
    private Long status;
    
    @Column(name = "PARENT_ROLE_ID")
    private Long parentRoleId;
    
    public BocRoleEntity(){
    	
    }

    public BocRoleEntity(
    		Long roleId
    		, String roleName
    		, String roleCode
    		, Long status
    		, Long parentRoleId
    ){
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleCode = roleCode;
        this.status = status;
        this.parentRoleId = parentRoleId;
    }
}
