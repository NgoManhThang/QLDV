package com.viettel.api.service.qldv.member;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.MemberDto;
import com.viettel.api.repository.qldv.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    MemberRepository memberRepository;

    @Override
    public ResultDto saveData(MemberDto dto) {
        return memberRepository.saveData(dto);
    }

    @Override
    public Datatable searchMember(MemberDto dto) {
        return memberRepository.searchMember(dto);
    }
}
