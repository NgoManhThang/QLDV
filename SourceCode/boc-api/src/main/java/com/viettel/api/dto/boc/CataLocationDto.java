package com.viettel.api.dto.boc;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CataLocationDto {
	private String kvId;
	private String provinceId;
	private String districtId;
	private String kvCode;
	private String provinceCode;
	private String districtCode;
	private String kvName;
	private String provinceName;
	private String districtName;
	private String parentId;
	
	private String code;
	private String decode;
	private List<String> lstProvinceCodes;
}
