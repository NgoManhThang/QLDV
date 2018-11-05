package com.viettel.api.service.qldv.member;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.MemberDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MemberService {
    ResultDto saveData(MemberDto dto, MultipartFile fileCMT, MultipartFile fileComputer) throws IOException;

    Datatable searchMember(MemberDto dto);

    MemberDto getDetail(MemberDto dto);

    ResultDto delete(MemberDto dto);
}
