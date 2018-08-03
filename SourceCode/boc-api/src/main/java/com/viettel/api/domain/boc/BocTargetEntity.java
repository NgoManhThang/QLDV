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
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@Entity
@Getter
@Setter
@Table(name = "BOC_TARGET")
public class BocTargetEntity {
	@SequenceGenerator(name = "generator", sequenceName = "BOC_TARGET_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "TARGET_ID", unique = true)
    private Long targetId;

    @Column(name = "TARGET_MONTH")
    private String targetMonth;

    @Column(name = "TARGET_TYPE")
    private String targetType;

    @Column(name = "REGION_CODE")
    private String regionCode;

    @Column(name = "TARGET_NUM")
    private Float targetNum;

    @Column(name = "WARNING_1")
    private Float warning1;
    
    @Column(name = "WARNING_2")
    private Float warning2;
    
    @Column(name = "CREATED_USER_NAME")
    private String createdUserName;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    
    @Column(name = "MONTH_YEAR")
    private String monthYear;

    public BocTargetEntity(){
    	
    }

    public BocTargetEntity(
    		Long targetId
    		, String targetMonth
    		, String targetType
    		, String regionCode
    		, Float targetNum
    		, Float warning1
    		, Float warning2
    		, String createdUserName
    		, Timestamp createdDate
    		, String monthYear
    ){
    	this.targetId = targetId;
        this.targetMonth = targetMonth;
        this.targetType = targetType;
        this.regionCode = regionCode;
        this.targetNum = targetNum;
        this.warning1 = warning1;
        this.warning2 = warning2;
        this.createdUserName = createdUserName;
        this.createdDate = createdDate;
        this.monthYear = monthYear;
    }
}
