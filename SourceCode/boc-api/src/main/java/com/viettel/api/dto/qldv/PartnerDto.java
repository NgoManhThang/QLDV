package com.viettel.api.dto.qldv;

import com.viettel.api.domain.qldv.PartnerEntity;
import com.viettel.api.dto.BaseDto;
import com.viettel.api.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class PartnerDto extends BaseDto{
    private Long partnerId;
    private String partnerCode;
    private String partnerName;
    private String partnerType;
    private String partnerTypeName;
    private String createUser;
    private Timestamp createDate;
    private String updateUser;
    private Timestamp updateDate;
    private String status;
    private String statusName;
    private String representName;
    private String phoneRepresent;

    public PartnerDto() {
    }

    public PartnerEntity toEntity() {
        Logger logger = LoggerFactory.getLogger(PartnerDto.class);
        try {
            PartnerEntity entity = new PartnerEntity(
                    partnerId,
                    partnerCode,
                    partnerName,
                    partnerType,
                    createUser,
                    createDate,
                    updateUser,
                    updateDate,
                    StringUtils.isNotNullOrEmpty(status) ? Long.valueOf(status) : null,
                    representName,
                    phoneRepresent
            );
            return entity;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
