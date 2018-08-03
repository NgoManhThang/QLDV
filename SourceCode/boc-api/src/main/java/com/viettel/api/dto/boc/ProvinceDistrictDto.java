package com.viettel.api.dto.boc;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProvinceDistrictDto {
	private Long provinceId;
	private String provinceCode;
	private String provinceName;
	private String provinceVname;
	private Long districtId;
	private String districtCode;
	private String districtName;
	private String importData;
	private String groupProv;
	private String vtmapsCode;
	private String centerLoc;
	private String northLoc;
	private String southLoc;
	private Date importDate;
	private String geometry;
	private String geometryMini;
	private String northPole;
	private String levelAlarm;
	private String target;
	private String actual;
	private Long regionLevel;
}
