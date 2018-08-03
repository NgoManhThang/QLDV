package com.viettel.api.utils;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;

import com.viettel.api.dto.DemoDiffDto;

/**
 * Created by VTN-PTPM-NV04 on 12/13/2017.
 */
public class ObjectUtils {
    public static void main(String[] args) {
    	DemoDiffDto demoDiffDto1 = new DemoDiffDto();
    	demoDiffDto1.setFieldLong(1L);
    	demoDiffDto1.setFieldString("A");
    	demoDiffDto1.setFieldFloat(1F);
    	demoDiffDto1.setFieldDouble(1D);
    	demoDiffDto1.setFieldDate(new Date());
    	demoDiffDto1.setFieldTimestamp(new Timestamp(0));
    	demoDiffDto1.setFieldBoolean(true);
    	
    	DemoDiffDto demoDiffDto2 = new DemoDiffDto();
    	demoDiffDto2.setFieldLong(1L);
    	demoDiffDto2.setFieldString("B");
    	demoDiffDto2.setFieldFloat(2F);
    	demoDiffDto2.setFieldDouble(1D);
    	demoDiffDto2.setFieldDate(new Date());
    	demoDiffDto2.setFieldTimestamp(new Timestamp(0));
    	demoDiffDto2.setFieldBoolean(false);
    	
    	DiffResult diff = demoDiffDto1.diff(demoDiffDto2);
    	System.out.println(getDiffTwoObject(diff));
	}
    
    public static String getDiffTwoObject(DiffResult diff) {
    	String result = "";
    	for(Diff<?> d: diff.getDiffs()) {
    		result = result + d.getFieldName() + " FROM [" + d.getLeft() + "] TO [" + d.getRight() + "]; ";
		}
    	return result;
    }
}
