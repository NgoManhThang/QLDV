package com.viettel.api.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viettel.api.config.Constants;

/**
 * Created by maint on 12/16/2017.
 */
public class BundleUtils {

    protected static final Logger logger = LoggerFactory.getLogger(BundleUtils.class);

    protected static final String[] ALIGNS = new String[]{"LEFT", "RIGHT", "CENTER"};

    private static volatile ResourceBundle rsConfig = null;

    public static String getBundleValue(String key, Locale... locale) {
        ResourceBundle resource = ResourceBundle.getBundle("config/globalConfig");
        return resource.getString(key);
    }

    public static String getLangString(String key, Locale... locale) {
        Locale vi = new Locale("vi");
        Locale mlocale = vi;

        try {
            if (locale != null) {
                if (locale.length == 0) {
                    rsConfig = ResourceBundle.getBundle(Constants.LANGUAGE, mlocale);
                } else {
                    rsConfig = ResourceBundle.getBundle(Constants.LANGUAGE, locale[0]);
                }
            } else {
                rsConfig = ResourceBundle.getBundle(Constants.LANGUAGE, mlocale);
            }
            return rsConfig.getString(key);
//            return new String(rsConfig.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return key;
        }
    }

    public static String getMessage(String key, Locale locale, Object... params) {
        try {
            String ms = getLangString(key, locale);
            return MessageFormat.format(ms, params);
        } catch (MissingResourceException e) {
            logger.error(e.getMessage(), e);
            return '!' + key + '!';
        }
    }
}
