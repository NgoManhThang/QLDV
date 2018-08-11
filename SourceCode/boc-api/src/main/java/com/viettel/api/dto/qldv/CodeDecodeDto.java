package com.viettel.api.dto.qldv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CodeDecodeDto {
    private String codeGroup;
    private String code;
    private String decode;
    private String codeLevel;
    private String status;
}
