package com.viettel.api.config;

/**
 * Application constants.
 */
public final class Constants {
    public static final String API_PATH_PREFIX = "/api/boc/";
    
    public static final String LANGUAGE = "i18n/language";

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String LANGUAGE_CODE = "LanguageCode";

    public static final String RESPONSE_CODE = "responseCode";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
    public static final String DUPLICATE = "DUPLICATE";
    
    // define users messages
    public static final String EXISTED_USER = "user.msg.existUser";
    public static final String EXISTED_USER_CODE = "user.msg.existUserCode";
    public static final String EXISTED_EMAIL = "user.msg.existEmail";

    public interface ROLE {
        public static final Long ADMIN = 1L;
    }
    public static final String formatterDateText = "dd/MM/yyyy";
    public static final String formatterDateTimeText = "dd/MM/yyyy HH:mm:ss";
    
    public interface RESULT {
        String SUCCESS = "SUCCESS";
        String ERROR = "ERROR";
        String DUPLICATE = "DUPLICATE";
        String FILE_IS_NULL = "FILE_IS_NULL";
        String NODATA = "NODATA";
        String FILE_ERROR = "FILE_ERROR";
        String FILE_INVALID = "FILE_INVALID";
        String FILE_TYPE_INVALID = "FILE_TYPE_INVALID";
        String DATA_OVER = "DATA_OVER";
        String NOT_ACCESS = "NOT_ACCESS";
        String NO_CAN_DELETE = "NO_CAN_DELETE";

    }
    
    private Constants() {
    }
}
