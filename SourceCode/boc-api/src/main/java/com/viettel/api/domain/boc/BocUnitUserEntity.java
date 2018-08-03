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
@Table(name = "BOC_UNIT_USER")
public class BocUnitUserEntity {
	@SequenceGenerator(name = "generator", sequenceName = "BOC_UNIT_USER_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "UNIT_USER_ID", unique = true)
    private Long unitUserId;

    @Column(name = "UNIT_ID")
    private Long unitId;
    
    @Column(name = "USER_ID")
    private Long userId;
    
    public BocUnitUserEntity(){
    	
    }

    public BocUnitUserEntity(
    		Long unitUserId
    		, Long unitId
    		, Long userId
    ){
        this.unitUserId = unitUserId;
        this.unitId = unitId;
        this.userId = userId;
    }
}
