package com.viettel.api.service.qldv.member;

import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.MemberDto;
import com.viettel.api.repository.qldv.member.MemberRepository;
import com.viettel.api.utils.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class MemberServiceImpl implements MemberService {
    Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    MemberRepository memberRepository;

    @Override
    public ResultDto saveData(MemberDto dto, MultipartFile fileCMT, MultipartFile fileComputer) throws IOException {
        //Save file CMT
        if (fileCMT != null) {
            dto.setFileNameCMT(fileCMT.getOriginalFilename());
            String filePath = FilesUtils.saveUploadedFile(fileCMT);
            dto.setFilePathCMT(filePath);
            //Nếu có file thì không cần delete ở lúc update mà chỉ update thên file thôi
            dto.setFileIdDeleteCMT(null);
        } else {
            dto.setFilePathCMT(null);
            dto.setFileNameCMT(null);
        }
        //Save file CMT
        if (fileComputer != null) {
            dto.setFileNameComputer(fileComputer.getOriginalFilename());
            String filePath = FilesUtils.saveUploadedFile(fileComputer);
            dto.setFilePathComputer(filePath);
            //Nếu có file thì không cần delete ở lúc update mà chỉ update thên file thôi
            dto.setFileIdDeleteComputer(null);
        } else {
            dto.setFilePathComputer(null);
            dto.setFileNameComputer(null);
        }
        return memberRepository.saveData(dto);
    }

    @Override
    public Datatable searchMember(MemberDto dto) {
        return memberRepository.searchMember(dto);
    }
}
