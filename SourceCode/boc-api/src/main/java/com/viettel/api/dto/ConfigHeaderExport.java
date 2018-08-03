package com.viettel.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by VTN-PTPM-NV04 on 2/24/2018.
 */
@Getter
@Setter
public class ConfigHeaderExport {
    private String fieldName;
    private String align;
    private boolean hasMerge;
    private int mergeRow;
    private int mergeColumn;
    private String[] subHeader;
    private String[] replace;
    private String styleFormat;
    private String[] subHeaderName;
    private String headName;

    public ConfigHeaderExport(
            String fieldName
            , String align
            , boolean hasMerge
            , int mergeRow
            , int mergeColumn
            , String[] subHeader
            , String[] replace
            , String styleFormat
    ){
        this.fieldName = fieldName;
        this.align = align;
        this.hasMerge = hasMerge;
        this.mergeRow = mergeRow;
        this.mergeColumn = mergeColumn;
        this.subHeader = subHeader;
        this.replace = replace;
        this.styleFormat = styleFormat;
    }

    public ConfigHeaderExport(
            String fieldName
            , String align
            , boolean hasMerge
            , int mergeRow
            , int mergeColumn
            , String[] subHeader
            , String[] replace
            , String styleFormat
            , String headName
            , String[] subHeaderName
    ){
        this.fieldName = fieldName;
        this.align = align;
        this.hasMerge = hasMerge;
        this.mergeRow = mergeRow;
        this.mergeColumn = mergeColumn;
        this.subHeader = subHeader;
        this.replace = replace;
        this.styleFormat = styleFormat;
        this.headName = headName;
        this.subHeaderName = subHeaderName;
    }
}
