package com.viettel.api.dto.qldv;

import com.viettel.api.domain.qldv.UnionsEntity;
import com.viettel.api.dto.BaseDto;
import com.viettel.api.utils.DataUtil;
import com.viettel.api.utils.StringUtils;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UnionsDto extends BaseDto {
    private Long unionId;
    private String unionName;
    private Long vietnameseNumber;
    private Long foreignerNumber;
    private String fromDate;
    private String toDate;
    private Long partnerId;
    private String partnerName;

    private Long unitId;
    private String representName;
    private String representPhone;
    private Long employeeId;
    private String purpose;
    private String unionType;
    private String workContent;
    private String reasonNotStatement;
    private String apprStatus;
    private String apprUser;
    private Timestamp apprDate;
    private Long statementId;
    private String status;
    private String statusValue;
    private String createUser;
    private Timestamp createDate;
    private String updateUser;
    private Timestamp updateDate;
    private Long placeIds;
    private String reasonNotApp;
    private String representUnion;
    private String representCompany;

    private String fromDateFrom;
    private String fromDateTo;
    private String toDateFrom;
    private String toDateTo;
    private String phoneRepresentCompany;

    private List<String> lstPartnerId;
    private List<String> lstUnionType;
    private List<String> lstUnionStatus;

    public UnionsEntity toEntity() {
        Logger logger = LoggerFactory.getLogger(UnionsDto.class);
        try {
            UnionsEntity entity = new UnionsEntity(
                    unionId,
                    unionName,
                    vietnameseNumber,
                    foreignerNumber,
                    StringUtils.isNotNullOrEmpty(fromDate) ? DataUtil.ddMMyyyyToDate(fromDate) : null,
                    StringUtils.isNotNullOrEmpty(toDate) ? DataUtil.ddMMyyyyToDate(toDate) : null,
                    partnerId,
                    representName,
                    representPhone,
                    employeeId,
                    purpose,
                    unionType,
                    workContent,
                    apprStatus,
                    apprUser,
                    apprDate,
                    status,
                    createUser,
                    createDate,
                    updateUser,
                    updateDate,
                    placeIds,
                    reasonNotApp
            );
            return entity;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
