package com.viettel.api.dto.boc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BocCodeDto {
	private String codeGroup;
	private String code;
	private String decode;
	private String parentCode;
	private Long codeLevel;
	private String note;
	private String refData;
	
	private String decodeGroup;
}
