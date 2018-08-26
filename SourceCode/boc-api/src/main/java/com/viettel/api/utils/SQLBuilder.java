package com.viettel.api.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.log4j.Logger;

import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author Nam, Nguyen Hoai
 *
 */
public class SQLBuilder {

    public static final Logger LOG = Logger.getLogger(SQLBuilder.class);

    public final static String SQL_MODULE_COMMON = "common";
    
    //Ngô Mạnh Thắng
    public final static String SQL_MODULE_MAPS = "maps";
    public final static String SQL_MODULE_BOC_TARGET = "boc_target";
    public final static String SQL_MODULE_BOC_USER = "boc_user";
    public final static String SQL_MODULE_STATISTICS = "statistics";
    public final static String SQL_MODULE_QLDV_EMPLOYEE = "employee";
    public final static String SQL_MODULE_QLDV_COMMON = "qldv_common";
    public final static String SQL_MODULE_QLDV_PARTNER = "partner";
    public final static String SQL_MODULE_QLDV_UNIONS = "unions";
    public final static String SQL_MODULE_QLDV_VALIDATE = "validate";
    public final static String SQL_MODULE_QLDV_MEMBER = "member";

    public static String getSqlQueryById(String module, String queryId) {
        File folder = null;
        try {
            folder = new ClassPathResource(
                    "sql" + File.separator + module + File.separator + queryId + ".sql").getFile();
            // Read file
            if (folder.isFile()) {
                return new String(Files.readAllBytes(Paths.get(folder.getAbsolutePath())));
            }
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
        return null;
    }

}
