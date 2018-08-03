package com.viettel.api.dto;

import java.io.File;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VTN-PTPM-NV04 on 2/6/2018.
 */
@Getter
@Setter
public class ResultDto {
    String id;
    String key;
    String message;
    Timestamp systemDate;
    Object object;
    File file;
    String authToken;

    public ResultDto(){

    }
}
