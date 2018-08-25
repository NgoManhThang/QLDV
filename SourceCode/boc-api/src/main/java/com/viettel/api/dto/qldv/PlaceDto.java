package com.viettel.api.dto.qldv;

import com.viettel.api.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDto extends BaseDto{
    private Long placeId;
    private String placeCode;
    private String placeName;
    private String area;
    private String description;
    private Long status;
}
