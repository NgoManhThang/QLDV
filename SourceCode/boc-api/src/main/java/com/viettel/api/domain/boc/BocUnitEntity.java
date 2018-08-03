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
@Table(name = "BOC_UNIT")
public class BocUnitEntity {
	@SequenceGenerator(name = "generator", sequenceName = "BOC_UNIT_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "UNIT_ID", unique = true)
    private Long unitId;

    @Column(name = "UNIT_NAME")
    private String unitName;

    @Column(name = "UNIT_CODE")
    private String unitCode;

    @Column(name = "PARENT_UNIT_ID")
    private Long parentUnitId;

    @Column(name = "MAP_UNIT_ID")
    private Long mapUnitId;

    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "STATUS")
    private Long status;
    
    public BocUnitEntity(){
    	
    }

    public BocUnitEntity(
    		Long unitId
    		, String unitName
    		, String unitCode
    		, Long parentUnitId
    		, Long mapUnitId
    		, String description
    		, Long status
    ){
        this.unitId = unitId;
        this.unitName = unitName;
        this.unitCode = unitCode;
        this.parentUnitId = parentUnitId;
        this.mapUnitId = mapUnitId;
        this.description = description;
        this.status = status;
    }
}
