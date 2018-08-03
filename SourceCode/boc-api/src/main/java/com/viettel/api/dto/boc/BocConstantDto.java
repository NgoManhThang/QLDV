package com.viettel.api.dto.boc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BocConstantDto {
	private Long constantId;
	private String constantCode;
	private String constantName;
	private String description;
	private Long status;
	private String constantType;
}
