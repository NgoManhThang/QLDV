package com.viettel.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VTN-PTPM-NV04 on 2/8/2018.
 */
@Getter
@Setter
public class BaseDto {
    private int page;
    private int pageSize;
    private String sortName;
    private String sortType;
    private int totalRow;
    private int indexRow;
    private String langKey;
}
