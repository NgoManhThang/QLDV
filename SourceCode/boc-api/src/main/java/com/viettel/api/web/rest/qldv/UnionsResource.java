package com.viettel.api.web.rest.qldv;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.UnionsEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.UnionsDto;
import com.viettel.api.service.qldv.unions.UnionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/saveData")
    @Timed
    public ResponseEntity<ResultDto> saveData(@RequestParam("dataString") String data) {
        ResultDto resultDto = new ResultDto();
        try {
            ObjectMapper mapper = new ObjectMapper();
            UnionsDto dto = mapper.readValue(data, UnionsDto.class);
            resultDto = unionsService.saveData(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @PostMapping("/getDetail")
    @Timed
    public ResponseEntity<UnionsEntity> getDetail(@RequestBody UnionsDto dto) {
        UnionsEntity unionsEntity = new UnionsEntity();
        try {
            unionsEntity = unionsService.getDetail(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(unionsEntity, HttpStatus.OK);
    }

    @PostMapping("/updateStatus")
    @Timed
    public ResponseEntity<ResultDto> updateStatus(@RequestBody UnionsDto dto) {
        ResultDto resultDto = new ResultDto();
        try {
            resultDto = unionsService.updateStatus(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }
}
