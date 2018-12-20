package com.viettel.api.web.rest.qldv;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.UnionsEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.LookupDto;
import com.viettel.api.dto.qldv.MemberDto;
import com.viettel.api.dto.qldv.UnionsDto;
import com.viettel.api.service.qldv.lookup.LookupService;
import com.viettel.api.service.qldv.unions.UnionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "lookup")
@SuppressWarnings("all")
public class LookupResource {
    Logger logger = LoggerFactory.getLogger(LookupResource.class);

    @Autowired
    LookupService lookupService;

    @PostMapping("/search")
    @Timed
    public ResponseEntity<Datatable> search(@RequestBody LookupDto dto) {
        Datatable datatable = new Datatable();
        try {
            datatable = lookupService.search(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(datatable, HttpStatus.OK);
    }

    @PostMapping("/searchMember")
    @Timed
    public ResponseEntity<Datatable> searchMember(@RequestBody MemberDto dto) {
        Datatable datatable = new Datatable();
        try {
            datatable = lookupService.searchMember(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(datatable, HttpStatus.OK);
    }

    @PostMapping("/scanBarcode")
    @Timed
    public ResponseEntity<LookupDto> scanBarcode(@RequestBody LookupDto dto) {
        try {
            dto = lookupService.scanBarcode(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
