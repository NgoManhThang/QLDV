package com.viettel.api.web.rest.qldv;

import com.codahale.metrics.annotation.Timed;
import com.viettel.api.config.Constants;
import com.viettel.api.dto.qldv.CodeDecodeDto;
import com.viettel.api.service.qldv.CommonQldvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "common")
public class CommonQldvResource {
    Logger logger = LoggerFactory.getLogger(CommonQldvResource.class);

    @Autowired
    CommonQldvService commonQldvService;


    @PostMapping("/search")
    @Timed
    public ResponseEntity<List<CodeDecodeDto>> seacrch(@RequestBody CodeDecodeDto dto) {
        List<CodeDecodeDto> lst = new ArrayList<>();
        try {
            lst = commonQldvService.search(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }
}
