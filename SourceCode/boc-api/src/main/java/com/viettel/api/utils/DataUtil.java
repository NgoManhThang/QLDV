package com.viettel.api.utils;

import com.google.common.base.Splitter;
import net.logstash.logback.encoder.org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by VTN-PTPM-NV30 on 12/18/2017.
 */
public class DataUtil {
    protected static final Logger logger = LoggerFactory.getLogger(DataUtil.class);
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd";

    public static List<String> splitDot(String input) {
        return Splitter.on(".").trimResults().omitEmptyStrings().splitToList(input);
    }
    
    public static boolean isNullOrEmpty(String obj1) {
        return (obj1 == null || "".equals(obj1.trim()));
    }

    public static String commonPaser(SimpleDateFormat format, String src) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date date = dateFormat.parse(src);
        return format.format(date);
    }

    public static Date convertStringToDate(String date1) throws Exception {
        String date = date1;
        String pattern = "dd/MM/yyyy HH:mm:ss";
        if (date.length() <= 10) {
            date = date + " 00:00:00";
        }
        return convertStringToTime(date, pattern);
    }

    public static Date convertStringToTime(String date, String pattern) throws ParseException {
        if (date == null || "".equals(date.trim())) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(date);

    }

    public static Timestamp date2Timestamp(Date value) {
        if (value != null) {
            return new Timestamp(value.getTime());
        }
        return null;
    }

    public static Timestamp string2Timestamp(String date) throws Exception {
        String strDate = DataUtil.commonPaser(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"), date);
        Date value = convertStringToDate(strDate);

        if (value != null) {
            return new Timestamp(value.getTime());
        }
        return null;
    }

    public static Date ddMMyyyyToDate(String str) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse(str);
        return date;
    }

    public static Boolean isNumber(String str) {
        return str != null && NumberUtils.isNumber(str);
    }

    public static String date2ddMMyyyyHHMMss(Date value) {
        if (value != null) {
            SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return dateTimeNoSlash.format(value);
        }
        return "";
    }

    public static Boolean forceAlphaNumericOnly(String str){
        return str.matches("[a-zA-Z0-9]*");
    }
}
