package com.viettel.api.web.rest.qldv;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.PartnerEntity;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.qldv.PartnerDto;
import com.viettel.api.service.qldv.partner.PartnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "partner")
public class PartnerResource {
    Logger logger = LoggerFactory.getLogger(PartnerResource.class);

    @Autowired
    PartnerService partnerService;

    @PostMapping("/search")
    @Timed
    public ResponseEntity<Datatable> searchPartner(@RequestBody PartnerDto dto) {
        Datatable datatable = new Datatable();
        try {
            datatable = partnerService.seachPartner(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(datatable, HttpStatus.OK);
    }

    @PostMapping("/saveData")
    @Timed
    public ResponseEntity<ResultDto> saveData(@RequestParam("dataString") String dataString) {
        ResultDto resultDto = new ResultDto();
        try {
            ObjectMapper mapper = new ObjectMapper();
            PartnerDto dto = mapper.readValue(dataString, PartnerDto.class);
            resultDto = partnerService.saveData(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @PostMapping("/getDetail")
    @Timed
    public ResponseEntity<PartnerEntity> getDetail(@RequestBody PartnerDto dto) {
        PartnerEntity entity = partnerService.getDetail(dto);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }
}
