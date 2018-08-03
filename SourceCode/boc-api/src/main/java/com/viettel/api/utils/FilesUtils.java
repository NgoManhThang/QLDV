package com.viettel.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.viettel.api.dto.boc.BocFilesDto;

import liquibase.util.file.FilenameUtils;

/**
 * Created by VTN-PTPM-NV30 on 12/14/2017.
 */
public class FilesUtils {

    public static String saveUploadedFile(MultipartFile file, String uploadFolder) throws IOException {
        String currentTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String originalName = file.getOriginalFilename();
        String fileName = FilenameUtils.getBaseName(originalName) + "_" + currentTime + "."
                + FilenameUtils.getExtension(originalName);
        byte[] bytes = file.getBytes();
        File folderUpload = new File(uploadFolder);
        if (!folderUpload.exists()) {
            folderUpload.mkdirs();
        }
        Path path = Paths.get(folderUpload.getPath() + File.separator + fileName);
        Files.write(path, bytes);
        return path.toString();
    }

    public static List<BocFilesDto> saveMultipleUploadedFile(List<MultipartFile> files, String uploadFolder) throws IOException {
        List<BocFilesDto> listBocFilesDto = new ArrayList<>();
        if (files != null && files.size() > 0) {
            int i = 1;
            for (MultipartFile file : files) {
                String currentTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                String originalName = file.getOriginalFilename();
                String fileName = FilenameUtils.getBaseName(originalName) + "_" + String.valueOf(i) + "_" + currentTime + "."
                        + FilenameUtils.getExtension(originalName);
                byte[] bytes = file.getBytes();
                File folderUpload = new File(uploadFolder + File.separator + createPathByDate());
                if (!folderUpload.exists()) {
                    folderUpload.mkdirs();
                }
                Path path = Paths.get(folderUpload.getPath() + File.separator + fileName);
                Files.write(path, bytes);
                BocFilesDto fileDto = new BocFilesDto();
                fileDto.setFileName(fileName);
                fileDto.setFilePath(createPathByDate() + File.separator + fileName);
                fileDto.setFileSize(file.getSize());
                listBocFilesDto.add(fileDto);
                i++;
            }
        }
        return listBocFilesDto;
    }

    public static String createPathByDate() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String pathDate = String.valueOf(year) + File.separator + String.valueOf(month + 1) + File.separator + String.valueOf(day);
        return pathDate;
    }

    public static File readFileByPath(String path, String uploadFolder) throws UnsupportedEncodingException {
        File file = new File(uploadFolder + File.separator + path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    
    public static void deleteFileByPath(String pathFile, String uploadFolder) throws UnsupportedEncodingException {
        File file = new File(uploadFolder + File.separator + pathFile);
        if (file.exists()) {
        	if(file.delete()){
        		System.out.println(uploadFolder + File.separator + pathFile + " has delete");
        	} else {
        		System.out.println(uploadFolder + File.separator + pathFile + " hasn't delete");
        	}
        }
    }

    public static void deleteFileByListFile(List<BocFilesDto> listFile, String uploadFolder) throws UnsupportedEncodingException {
        // Get list file
        for (int i = 0; i < listFile.size(); i++) {
            File file = new File(uploadFolder + File.separator + listFile.get(i).getFilePath());
            if (file.exists()) {
            	if(file.delete()){
            		System.out.println(uploadFolder + File.separator + listFile.get(i).getFilePath() + " has delete");
            	} else {
            		System.out.println(uploadFolder + File.separator + listFile.get(i).getFilePath() + " hasn't delete");
            	}
            }
        }
    }

    public static File readFileByListFile(List<BocFilesDto> listFile, String uploadFolder) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String fullPath = URLDecoder.decode(classLoader.getResource("").getPath(), "UTF-8");
        String pathArr[] = fullPath.split("/target/classes");
        String rootPath = pathArr[0].substring(0, pathArr[0].lastIndexOf("/"));
        rootPath += File.separator + "file_zip_out";
        String currentTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        File zipfile = new File(rootPath);
        if (!zipfile.exists()) {
            zipfile.mkdirs();
        }
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        ZipOutputStream out = null;
        try {
            // Get list file
            List<File> files = new ArrayList<>();
            for (int i = 0; i < listFile.size(); i++) {
                File file = new File(uploadFolder + File.separator + listFile.get(i).getFilePath());
                files.add(file);
            }
            // create the ZIP file
            out = new ZipOutputStream(new FileOutputStream(rootPath + File.separator + "file_" + currentTime + ".zip"));
            // compress the files
            for (int i = 0; i < files.size(); i++) {
                FileInputStream in = new FileInputStream(files.get(i).getCanonicalFile());
                try {
                    // add ZIP entry to output stream
                    out.putNextEntry(new ZipEntry(files.get(i).getName()));
                    // transfer bytes from the file to the ZIP file
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    // complete the entry
                    out.closeEntry();
                } catch (Exception e) {
                    throw e;
                } finally {
                    in.close();
                }
            }
            // complete the ZIP file
            out.close();
            return new File(rootPath + File.separator + "file_" + currentTime + ".zip");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static File zipFileByListFile(List<File> files) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String fullPath = URLDecoder.decode(classLoader.getResource("").getPath(), "UTF-8");
        String pathArr[] = fullPath.split("/target/classes");
        String rootPath = pathArr[0].substring(0, pathArr[0].lastIndexOf("/"));
        rootPath += File.separator + "file_zip_out";
        String currentTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        File zipfile = new File(rootPath);
        if (!zipfile.exists()) {
            zipfile.mkdirs();
        }
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        ZipOutputStream out = null;
        try {
            // create the ZIP file
            out = new ZipOutputStream(new FileOutputStream(rootPath + File.separator + "file_" + currentTime + ".zip"));
            // compress the files
            for (int i = 0; i < files.size(); i++) {
                FileInputStream in = new FileInputStream(files.get(i).getCanonicalFile());
                try {
                    // add ZIP entry to output stream
                    out.putNextEntry(new ZipEntry(files.get(i).getName()));
                    // transfer bytes from the file to the ZIP file
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    // complete the entry
                    out.closeEntry();
                } catch (Exception e) {
                    throw e;
                } finally {
                    in.close();
                }
            }
            // complete the ZIP file
            out.close();
            return new File(rootPath + File.separator + "file_" + currentTime + ".zip");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
