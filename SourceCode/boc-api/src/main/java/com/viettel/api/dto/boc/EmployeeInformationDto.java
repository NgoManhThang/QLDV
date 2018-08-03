package com.viettel.api.dto.boc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeInformationDto {
	private String userName;
	private String fullName;
	private String phoneNumber;
	private String email;
	private String position;
	private String positionName;
	private String company;
	private String companyName;
	private String regionCode;
	private String regionName;
	private String workingMonth;
	private String failKpi;
	private String totalKpi;
	private String fileId;
}
