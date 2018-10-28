package com.viettel.api.web.rest.qldv;

import com.codahale.metrics.annotation.Timed;
import com.viettel.api.config.Constants;
import com.viettel.api.domain.qldv.FilesEntity;
import com.viettel.api.dto.boc.BocFilesDto;
import com.viettel.api.dto.qldv.CodeDecodeDto;
import com.viettel.api.dto.qldv.EmployeeDto;
import com.viettel.api.dto.qldv.PlaceDto;
import com.viettel.api.service.qldv.CommonQldvService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "qldv-common")
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

    @PostMapping("/getPlaceById")
    @Timed
    public ResponseEntity<List<PlaceDto>> getPlaceById(@RequestBody PlaceDto dto) {
        List<PlaceDto> lst = new ArrayList<>();
        try {
            lst = commonQldvService.getPlaceById(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @PostMapping("/getEmployeeByIdOrUserName")
    @Timed
    public ResponseEntity<List<EmployeeDto>> getEmployeeByIdOrUserName(@RequestBody EmployeeDto dto) {
        List<EmployeeDto> lst = new ArrayList<>();
        try {
            lst = commonQldvService.getEmployeeByIdOrUserName(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @GetMapping("/getFileById")
    @Timed
    public ResponseEntity<byte[]> getFileById(@RequestParam("fileId") Long fileId) {
        try {
            FilesEntity filesEntity = commonQldvService.getFileById(fileId);

//            ResourceBundle resource = ResourceBundle.getBundle("config/globalConfig");
//            String folderUpload = resource.getString("FOLDER_UPLOAD");
//            InputStream is = new FileInputStream(folderUpload + File.separator + filesEntity.getFilePath());
            InputStream is = new FileInputStream(filesEntity.getFilePath());

            byte[] contents = IOUtils.toByteArray(is);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
            String filename = filesEntity.getFileName();
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
