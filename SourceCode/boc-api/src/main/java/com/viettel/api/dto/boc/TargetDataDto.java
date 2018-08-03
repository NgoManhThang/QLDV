package com.viettel.api.dto.boc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetDataDto {
	private String code;
	private String decode;
	private Float target;
	private Float actual;
	private String evaluate;
}
