package com.viettel.api.web.rest.boc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.api.config.Constants;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocUserDto;
import com.viettel.api.service.boc.BocUserService;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "bocUser")
public class BocUserResource {

    protected Logger logger = LoggerFactory.getLogger(BocUserResource.class);

    @Autowired
    BocUserService bocUserService;
    
    @PreAuthorize("@roleChecker.hasValidRole('MANAGER_USER')")
    @PostMapping("/search")
    @Timed
    public ResponseEntity<Datatable> search(@RequestBody BocUserDto bocUserDto) throws URISyntaxException {
        Datatable data = bocUserService.search(bocUserDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @PreAuthorize("@roleChecker.hasValidRole('MANAGER_USER_DELETE')")
    @PostMapping("/delete")
    @Timed
    public ResponseEntity<ResultDto> delete(Long userId) throws URISyntaxException {
        ResultDto result = bocUserService.delete(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("@roleChecker.hasValidRole('MANAGER_USER')")
    @PostMapping("/getDetail")
    @Timed
    public ResponseEntity<BocUserDto> getDetail(Long userId) throws URISyntaxException {
    	BocUserDto data = bocUserService.getDetail(userId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @PreAuthorize("@roleChecker.hasValidRole('MANAGER_USER_ADD')")
    @PostMapping("/add")
    @Timed
    public ResponseEntity<ResultDto> add(@RequestParam("files") List<MultipartFile> files, @RequestParam("formDataJson") String formDataJson) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
    	ObjectMapper mapper = new ObjectMapper();
    	BocUserDto bocUserDto = mapper.readValue(formDataJson, BocUserDto.class);
    	ResultDto data = bocUserService.save(files, bocUserDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @PreAuthorize("@roleChecker.hasValidRole('MANAGER_USER_EDIT')")
    @PostMapping("/edit")
    @Timed
    public ResponseEntity<ResultDto> edit(@RequestParam("files") List<MultipartFile> files, @RequestParam("formDataJson") String formDataJson) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
    	ObjectMapper mapper = new ObjectMapper();
    	BocUserDto bocUserDto = mapper.readValue(formDataJson, BocUserDto.class);
    	ResultDto data = bocUserService.save(files, bocUserDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
