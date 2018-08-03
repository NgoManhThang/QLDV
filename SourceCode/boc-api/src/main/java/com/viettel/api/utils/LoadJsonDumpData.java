/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.api.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author VTN-PTPM-NV19
 */
public class LoadJsonDumpData {
    public static final Logger LOG = Logger.getLogger(LoadJsonDumpData.class);
    
    public static final  String MODULE_LOGIN = "login";
    public static final  String MODULE_WO = "wo";
    public static final  String MODULE_SEARCH_KPI = "search_kpi";
    public static final  String MODULE_OTHER = "other";
    public static final  String MODULE_RESOURCE = "resource";
    public static final  String MODULE_UTILITY = "utility";
    
    public static String getGetJsonData(String module,
            String fileName) throws IOException {
        BufferedReader in = null;
        try {
        	Resource resource = new ClassPathResource(
                    "dump_data" + File.separatorChar + module + File.separatorChar + fileName + ".json");
        	LOG.info("dump_data" + File.separatorChar + module + File.separatorChar + fileName + ".json");
        	LOG.info(resource.exists());
            // Read file
            if (resource.exists()) {
                in = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));
                String str;
                String data = "";
                while((str = in.readLine()) != null) {
                    data += str;
                }
                //String sql = new String(Files.readAllBytes(Paths.get(folder.getAbsolutePath())));
                in.close();
                return data;
            }
        } catch (IOException e) {
            LOG.error(e);
            return null;
        } finally {
            if(in != null){
                in.close();
            }
        }
        return null;
    }
}
