package com.viettel.api.dto.boc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BocDataChartDto {
	private String regionCode;
	private String regionName;
	private String actual;
	private String target;
	private String displayDate;
	
	private String fromDate;
	private String toDate;
	private String provinceCode;
	private String districtCode;
	private String bocCode;
	private String service;
}
