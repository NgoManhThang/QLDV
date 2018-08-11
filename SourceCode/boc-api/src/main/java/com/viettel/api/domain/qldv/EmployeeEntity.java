package com.viettel.api.domain.qldv;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@Table(name = "QLDV_EMPLOYEE")
public class EmployeeEntity {
    @SequenceGenerator(name = "generator", sequenceName = "EMPLOYEE_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "EMPLOYEE_ID", unique = true)
    private Long employeeId;

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

    public EmployeeEntity() {
    }

    public EmployeeEntity(
            Long employeeId,
            String code,
            String fullName,
            String phoneNumber,
            String email,
            String position,
            String createUser,
            Timestamp createDate,
            String updateUser,
            Timestamp updateDate,
            String userName,
            String password,
            Long status
    ) {
        this.employeeId = employeeId;
        this.code = code;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.position = position;
        this.createUser = createUser;
        this.createDate = createDate;
        this.updateUser = updateUser;
        this.updateDate = updateDate;
        this.userName = userName;
        this.password = password;
        this.status = status;
    }
}
