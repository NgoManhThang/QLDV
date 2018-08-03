package com.viettel.api.dto.boc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadBocRegionDto {
	private String regionCode;
	private String regionName;
	private Long failKPI0;
	private Long totalKPI0;
	private Long failKPI1;
	private Long totalKPI1;
	private Long failKPI2;
	private Long totalKPI2;
	private Long failKPI3;
	private Long totalKPI3;
	private String isClick;
}
