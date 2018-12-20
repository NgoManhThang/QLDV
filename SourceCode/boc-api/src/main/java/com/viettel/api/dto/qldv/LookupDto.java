package com.viettel.api.dto.qldv;

import com.viettel.api.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LookupDto extends BaseDto {
    private Long unionId;
    private String unionName;
    private Long vietnameseNumber;
    private Long foreignerNumber;
    private String fromDate;
    private String toDate;
    private Long partnerId;
    private String partnerName;
    private String nationalName;
    private Long unionMemberId;
    private String fullName;
    private String memberId;
    private String laptopId;
    private String fileIdCMT;
    private String fileIdComputer;
    private String barCodeUser;
    private String barCodeComputer;

    // property for form search
    private String fromDateFrom;
    private String fromDateTo;
    private String toDateFrom;
    private String toDateTo;
    private List<String> lstPartnerId;

    //Scan barcode
    private String barCode;
    private String typeScan;
}
