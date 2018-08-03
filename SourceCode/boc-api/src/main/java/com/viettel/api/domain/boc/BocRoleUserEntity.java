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
@Table(name = "BOC_ROLE_USER")
public class BocRoleUserEntity {
	@SequenceGenerator(name = "generator", sequenceName = "BOC_ROLE_USER_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "ROLE_USER_ID", unique = true)
    private Long roleUserId;

    @Column(name = "ROLE_ID")
    private Long roleId;
    
    @Column(name = "USER_ID")
    private Long userId;
    
    public BocRoleUserEntity(){
    	
    }

    public BocRoleUserEntity(
    		Long roleUserId
    		, Long roleId
    		, Long userId
    ){
        this.roleUserId = roleUserId;
        this.roleId = roleId;
        this.userId = userId;
    }
}
