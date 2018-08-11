package com.viettel.api.domain.qldv;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.sql.Timestamp;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@Table(name = "QLDV_PARTNER")
public class PartnerEntity {
    @SequenceGenerator(name = "generator", sequenceName = "PARTNER_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "PARTNER_ID", unique = true)
    private Long partnerId;

    @Column(name = "PARTNER_CODE")
    private String partnerCode;

    @Column(name = "PARTNER_NAME")
    private String partnerName;

    @Column(name = "PARTNER_TYPE")
    private String partnerType;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

    @Column(name = "UPDATE_USER")
    private String updateUser;

    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;

    @Column(name = "STATUS")
    private String status;

    public PartnerEntity() {
    }

    public PartnerEntity(
            Long partnerId,
            String partnerCode,
            String partnerName,
            String partnerType,
            String createUser,
            Timestamp createDate,
            String updateUser,
            Timestamp updateDate,
            String status
    ) {
        this.partnerId = partnerId;
        this.partnerCode = partnerCode;
        this.partnerName = partnerName;
        this.partnerType = partnerType;
        this.createUser = createUser;
        this.createDate = createDate;
        this.updateUser = updateUser;
        this.updateDate = updateDate;
        this.status = status;
    }
}
