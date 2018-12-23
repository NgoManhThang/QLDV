package com.viettel.api.domain.qldv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QLDV_MEMBER_INOUT")
public class MemberInOutEntity implements Serializable{
    @SequenceGenerator(name = "generator", sequenceName = "MEM_IN_OUT_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "UNION_MEMBER_INOUT_ID", unique = true)
    private Long unionMemberInOutId;

    @Column(name = "UNION_MEMBER_ID")
    private Long unionMemberId;

    @Column(name = "UNION_ID")
    private Long unionId;

    @CreationTimestamp
    @Column(name = "TIME_IN", insertable = true, updatable = false)
    private Timestamp timeIn;

    @Column(name = "USER_IN")
    private String userIn;

    @UpdateTimestamp
    @Column(name = "TIME_OUT", insertable = false, updatable = true)
    private Timestamp timeOut;

    @Column(name = "USER_OUT")
    private String userOut;

    @Column(name = "BAR_CODE")
    private String barCode;
}
