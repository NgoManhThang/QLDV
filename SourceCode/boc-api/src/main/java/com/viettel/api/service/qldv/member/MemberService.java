package com.viettel.api.service.qldv.member;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.MemberDto;

public interface MemberService {
    ResultDto saveData(MemberDto dto);

    Datatable searchMember(MemberDto dto);
}
