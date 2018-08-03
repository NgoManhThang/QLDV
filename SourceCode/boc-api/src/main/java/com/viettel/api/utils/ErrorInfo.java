package com.viettel.api.utils;

/**
 * Created by VTN-PTPM-NV30 on 12/20/2017.
 */
public class ErrorInfo {
    private int row;
    private String msg;

    public ErrorInfo(int row, String msg) {
        this.row = row;
        this.msg = msg;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
