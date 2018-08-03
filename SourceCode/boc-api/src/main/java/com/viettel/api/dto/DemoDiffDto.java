package com.viettel.api.dto;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemoDiffDto implements Diffable<DemoDiffDto> {
	private Long fieldLong;
	private String fieldString;
	private Double fieldDouble;
	private Float fieldFloat;
	private Date fieldDate;
	private Timestamp fieldTimestamp;
	private Boolean fieldBoolean;
	
	@Override
	public DiffResult diff(DemoDiffDto obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
			 .append("Long value: ", this.fieldLong, obj.fieldLong)
			 .append("String value: ", this.fieldString, obj.fieldString)
			 .append("Double value: ", this.fieldDouble, obj.fieldDouble)
			 .append("Float value: ", this.fieldFloat, obj.fieldFloat)
			 .append("Date value: ", this.fieldDate, obj.fieldDate)
			 .append("Timestamp value: ", this.fieldTimestamp, obj.fieldTimestamp)
			 .append("Boolean value: ", this.fieldBoolean, obj.fieldBoolean)
			 .build();
	}
}
