package com.viettel.api.dto.qldv;

import com.viettel.api.domain.qldv.MemberEntity;
import com.viettel.api.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto extends BaseDto {
    private Long unionMemberId;
    private Long unionId;
    private String fullName;
    private String nationalId;
    private String memberType;
    private String memberId;
    private String laptopId;
    private Long barCodePrint;
    private String apprStatus;
    private String apprUser;
    private Timestamp apprDate;
    private String createUser;
    private Timestamp createDate;
    private String updateUser;
    private Timestamp updateDate;
    private String barCodeUser;
    private String barCodeComputer;
    private String unionName;

    private String filePathCMT;
    private String fileNameCMT;
    private String fileIdDeleteCMT;
    private String fileIdCMT;

    private String filePathComputer;
    private String fileNameComputer;
    private String fileIdDeleteComputer;
    private String fileIdComputer;

    private String numPersonVN;
    private String numPersonNN;

    public MemberEntity toEntity() {
        Logger logger = LoggerFactory.getLogger(MemberDto.class);
        try {
            MemberEntity entity = new MemberEntity(
                    unionMemberId,
                    unionId,
                    fullName,
                    nationalId,
                    memberType,
                    memberId,
                    laptopId,
                    barCodePrint,
                    apprUser,
                    apprDate,
                    createUser,
                    createDate,
                    updateUser,
                    updateDate,
                    barCodeUser,
                    barCodeComputer
            );
            return entity;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
