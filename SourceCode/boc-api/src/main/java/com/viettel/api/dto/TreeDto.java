package com.viettel.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeDto {
	private String label;
    private String code;
    private String parent;
    private Boolean selected;
}
