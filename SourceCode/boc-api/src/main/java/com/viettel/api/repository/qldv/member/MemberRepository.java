package com.viettel.api.repository.qldv.member;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.MemberDto;

public interface MemberRepository {

    Datatable searchMember(MemberDto dto);

    ResultDto saveData(MemberDto dto);
}