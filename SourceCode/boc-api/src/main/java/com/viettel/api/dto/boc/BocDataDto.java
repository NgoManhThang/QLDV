package com.viettel.api.dto.boc;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BocDataDto {
	private String regionCode;
	private String regionName;
	private String targetList;
	private String actualList;
	private String resultList;
	
	private List<String> target;
	private List<String> actual;
	private List<String> result;
}
