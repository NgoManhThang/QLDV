package com.viettel.api.dto.boc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BocModuleDto {
	private String bocModuleId;
	private String moduleName;
	private String moduleGroup;
	private String moduleGroupName;
	private String jsonParam;
	private String service;
}
