package com.viettel.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by VTN-PTPM-NV04 on 2/6/2018.
 */
@Getter
@Setter
public class Datatable {
    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private List<?> data;
}
