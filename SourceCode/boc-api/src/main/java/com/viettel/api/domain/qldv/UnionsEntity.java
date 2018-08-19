package com.viettel.api.domain.qldv;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.Date;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@Table(name = "QLDV_UNIONS")
public class UnionsEntity {
    @SequenceGenerator(name = "generator", sequenceName = "UNIONS_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "UNION_ID", unique = true)
    private Long unionId;
    @Column(name = "UNION_NAME")
    private String unionName;
    @Column(name = "VIETNAMESE_NUMBER")
    private Long vietnameeseNumber;
    @Column(name = "FOREIGNER_NUMBER")
    private Long foreignerNumber;
    @Column(name = "FROM_DATE")
    private Date fromDate;
    @Column(name = "TO_DATE")
    private Date toDate;
    @Column(name = "PARTNER_ID")
    private Long partnerId;
    @Column(name = "UNIT_ID")
    private Long unitId;
    @Column(name = "REPRESENT_NAME")
    private String representName;
    @Column(name = "REPRESENT_PHONE")
    private String representPhone;
    @Column(name = "EMPLOYEE_ID")
    private String employeeId;
    @Column(name = "PURPOSE")
    private String purpose;
    @Column(name = "UNION_TYPE")
    private String unionType;
    @Column(name = "WORK_CONTENT")
    private String workContent;
    @Column(name = "REASON_NOT_STATEMENT")
    private String reasonNotStatement;
    @Column(name = "APPR_STATUS_1")
    private String apprStatus1;
    @Column(name = "APPR_USER_1")
    private String apprUser1;
    @Column(name = "APPR_DATE_1")
    private Timestamp apprDate1;
    @Column(name = "APPR_STATUS_2")
    private String apprStatus2;
    @Column(name = "APPR_USER_2")
    private String apprUser2;
    @Column(name = "APPR_DATE_2")
    private Timestamp apprDate2;
    @Column(name = "STATEMENT_ID")
    private Long statementId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATE")
    private Timestamp createDate;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;
    @Column(name = "PLACE_IDS")
    private String placeIds;
    @Column(name = "REASON_NOT_APP")
    private String reasonNotApp;

    public UnionsEntity() {
    }

    public UnionsEntity(
            Long unionId,
            String unionName,
            Long vietnameeseNumber,
            Long foreignerNumber,
            Date fromDate,
            Date toDate,
            Long partnerId,
            Long unitId,
            String representName,
            String representPhone,
            String employeeId,
            String purpose,
            String unionType,
            String workContent,
            String reasonNotStatement,
            String apprStatus1,
            String apprUser1,
            Timestamp apprDate1,
            String apprStatus2,
            String apprUser2,
            Timestamp apprDate2,
            Long statementId,
            String status,
            String createUser,
            Timestamp createDate,
            String updateUser,
            Timestamp updateDate,
            String placeIds,
            String reasonNotApp
    ) {
        this.unionId = unionId;
        this.unionName = unionName;
        this.vietnameeseNumber = vietnameeseNumber;
        this.foreignerNumber = foreignerNumber;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.partnerId = partnerId;
        this.unitId = unitId;
        this.representName = representName;
        this.representPhone = representPhone;
        this.employeeId = employeeId;
        this.purpose = purpose;
        this.unionType = unionType;
        this.workContent = workContent;
        this.reasonNotStatement = reasonNotStatement;
        this.apprStatus1 = apprStatus1;
        this.apprUser1 = apprUser1;
        this.apprDate1 = apprDate1;
        this.apprStatus2 = apprStatus2;
        this.apprUser2 = apprUser2;
        this.apprDate2 = apprDate2;
        this.statementId = statementId;
        this.status = status;
        this.createUser = createUser;
        this.createDate = createDate;
        this.updateUser = updateUser;
        this.updateDate = updateDate;
        this.placeIds = placeIds;
        this.reasonNotApp = reasonNotApp;
    }
}
