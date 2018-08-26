package com.viettel.api.repository.qldv.member;

import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.MemberDto;

public interface MemberRepository {
    ResultDto saveData(MemberDto dto);
}
