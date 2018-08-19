package com.viettel.api.web.rest.qldv;

import com.codahale.metrics.annotation.Timed;
import com.viettel.api.config.Constants;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.qldv.UnionsDto;
import com.viettel.api.service.qldv.unions.UnionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "unions")
public class UnionsResource {
    Logger logger = LoggerFactory.getLogger(UnionsResource.class);

    @Autowired
    UnionsService unionsService;

    @PostMapping("/search")
    @Timed
    public ResponseEntity<Datatable> search(@RequestBody UnionsDto dto) {
        Datatable datatable = new Datatable();
        try {
            datatable = unionsService.search(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(datatable, HttpStatus.OK);
    }
}
