package com.viettel.api.web.rest.qldv;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.api.config.Constants;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.MemberDto;
import com.viettel.api.service.qldv.member.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "member")
public class MemberResource {
    Logger logger = LoggerFactory.getLogger(MemberResource.class);

    @Autowired
    MemberService memberService;

    @PostMapping("/saveData")
    @Timed
    public ResponseEntity<ResultDto> saveData(@RequestParam("dataString") String data) {
        ResultDto resultDto = new ResultDto();
        try {
            ObjectMapper mapper = new ObjectMapper();
            MemberDto dto = mapper.readValue(data, MemberDto.class);
            resultDto = memberService.saveData(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @PostMapping("/searchMember")
    @Timed
    public ResponseEntity<Datatable> searchMember(@RequestBody MemberDto dto){
        Datatable datatable = new Datatable();
        try {
            datatable = memberService.searchMember(dto);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(datatable, HttpStatus.OK);
    }
}