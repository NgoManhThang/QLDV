package com.viettel.api.domain.qldv;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.sql.Timestamp;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@Table(name = "QLDV_UNIONS_MEMBER")
public class MemberEntity {
    @SequenceGenerator(name = "generator", sequenceName = "MEMBER_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "UNION_MEMBER_ID", unique = true)
    private Long unionMemberId;

    @Column(name = "UNION_ID")
    private Long unionId;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "NATIONAL_ID")
    private String nationalId;

    @Column(name = "MEMBER_TYPE")
    private String memberType;

    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "LAPTOP_ID")
    private String laptopId;

    @Column(name = "BAR_CODE_PRINT")
    private Long barCodePrint;

    @Column(name = "APPR_STATUS")
    private String apprStatus;

    @Column(name = "APPR_USER")
    private String apprUser;

    @Column(name = "APPR_DATE")
    private Timestamp apprDate;

    @Column(name = "CREATE_USER")
    private String createUser;

    @CreationTimestamp
    @Column(name = "CREATE_DATE", updatable = false, insertable = true)
    private Timestamp createDate;

    @Column(name = "UPDATE_USER")
    private String updateUser;

    @UpdateTimestamp
    @Column(name = "UPDATE_DATE", updatable = true, insertable = false)
    private Timestamp updateDate;

//    @Column(name = "REASON_NOT_APP")
//    private String reasonNotApp;

    @Column(name = "BAR_CODE_USER")
    private String barCodeUser;
    @Column(name = "BAR_CODE_COMPUTER")
    private String barCodeComputer;


    public MemberEntity() {
    }

    public MemberEntity(
            Long unionMemberId,
            Long unionId,
            String fullName,
            String nationalId,
            String memberType,
            String memberId,
            String laptopId,
            Long barCodePrint,
            String apprStatus,
            String apprUser,
            Timestamp apprDate,
            String createUser,
            Timestamp createDate,
            String updateUser,
            Timestamp updateDate,
            String barCodeUser,
            String barCodeComputer
    ) {
        this.unionMemberId = unionMemberId;
        this.unionId = unionId;
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.memberType = memberType;
        this.memberId = memberId;
        this.laptopId = laptopId;
        this.barCodePrint = barCodePrint;
        this.apprStatus = apprStatus;
        this.apprUser = apprUser;
        this.apprDate = apprDate;
        this.createUser = createUser;
        this.createDate = createDate;
        this.updateUser = updateUser;
        this.updateDate = updateDate;
        this.barCodeUser = barCodeUser;
        this.barCodeComputer = barCodeComputer;
    }
}
