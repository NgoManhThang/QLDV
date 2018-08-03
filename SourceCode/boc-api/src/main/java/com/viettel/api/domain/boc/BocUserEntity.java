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
@Table(name = "BOC_USER")
public class BocUserEntity {
	@SequenceGenerator(name = "generator", sequenceName = "BOC_USER_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "USER_ID", unique = true)
    private Long userId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "POSITION")
    private String position;
    
    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

    @Column(name = "UPDATE_USER")
    private String updateUser;

    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;
    
    @Column(name = "USER_NAME")
    private String userName;
    
    @Column(name = "PASSWORD")
    private String password;
    
    @Column(name = "STATUS")
    private Long status;
    
    @Column(name = "WORKING_DATE")
    private Timestamp workingDate;
    
    @Column(name = "COMPANY")
    private String company;
    
    @Column(name = "ROLE_TARGET")
    private String roleTarget;
    
    public BocUserEntity(){
    	
    }

    public BocUserEntity(
    		Long userId
    		, String code
    		, String userName
    		, String password
    		, String fullName
    		, String phoneNumber
    		, String email
    		, String position
    		, String createUser
    		, Timestamp createDate
    		, String updateUser
    		, Timestamp updateDate
    		, Long status
    		, Timestamp workingDate
    		, String company
    		, String roleTarget
    ){
    	this.userId = userId;
        this.code = code;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.position = position;
        this.createUser = createUser;
        this.createDate = createDate;
        this.updateUser = updateUser;
        this.updateDate = updateDate;
        this.status = status;
        this.workingDate = workingDate;
        this.company = company;
        this.roleTarget = roleTarget;
    }
}
