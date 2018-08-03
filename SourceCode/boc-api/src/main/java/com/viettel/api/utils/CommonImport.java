package com.viettel.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.viettel.api.dto.ResultDto;

import liquibase.util.file.FilenameUtils;

/**
 * Created by VTN-PTPM-NV04 on 2/6/2018.
 */
public class CommonImport {
    protected static final Logger logger = LoggerFactory.getLogger(CommonImport.class);
    private static String currentTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    public static List getDataFromExcelFile(
            File file
            , int iSheet
            , int iBeginRow
            , int iFromCol
            , int iToCol
            , int rowBack
    ) throws IOException {
        List result = new ArrayList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        FileInputStream fileInput = new FileInputStream(file);
        try {
            String extension = FilenameUtils.getExtension(file.getName());
            Workbook workbook;
            if ("xls".equals(extension)) {
                workbook = new HSSFWorkbook(fileInput);
            } else {
                workbook = new XSSFWorkbook(fileInput);
            }
            Sheet worksheet = workbook.getSheetAt(iSheet);
            int irowBack = 0;

            for (int i = iBeginRow; i <= worksheet.getLastRowNum(); i++) {
                Object[] obj = new Object[iToCol - iFromCol + 1];
                Row row = worksheet.getRow(i);

                if (row != null) {
                    int iCount = 0;
                    int check = 0;
                    for (int j = iFromCol; j <= iToCol; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_STRING:
                                    obj[iCount] = cell.getStringCellValue().trim();
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    Double doubleValue = (Double) cell.getNumericCellValue();
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        Date date = HSSFDateUtil.getJavaDate(doubleValue);
                                        obj[iCount] = simpleDateFormat.format(date);
                                        break;
                                    }
                                    List<String> lstValue = DataUtil.splitDot(String.valueOf(doubleValue));
                                    if (lstValue.get(1).matches("[0]+")) {
                                        obj[iCount] = lstValue.get(0);
                                    } else {
                                        DecimalFormat df = new DecimalFormat("##.##");
                                        obj[iCount] = df.format(doubleValue);
                                    }
                                    break;
                                case Cell.CELL_TYPE_BLANK:
                                    check++;
                                    break;
                                default:
                                    obj[iCount] = cell.getStringCellValue().trim();
                                    break;
                            }
                        } else {
                            obj[iCount] = null;
                        }
                        iCount += 1;
                    }
                    if (check != (iToCol - iFromCol + 1)) {
                        boolean isOK = false;
                        for (int k = 0; k < obj.length; k++) {
                            if (!StringUtils.isStringNullOrEmpty(obj[k])) {
                                isOK = true;
                            }
                        }
                        if (isOK) {
                            result.add(obj);
                        }
                    }
                } else {
                    irowBack += 1;
                }
                if (irowBack == rowBack) {
                    break;
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            result = null;
        } finally {
            if (fileInput != null) {
                fileInput.close();
            }
        }
        return result;
    }

    public static boolean validateFileTmp(
            String fileImportPathOut
            , String templatePathOut
            , int rowStart
    ) throws UnsupportedEncodingException {
        ExcelWriterUtils ewu = new ExcelWriterUtils();

        Workbook workBook = ewu.readFileExcel(templatePathOut);
        Sheet sheetTemplate = workBook.getSheetAt(0);

        Workbook workBook2 = ewu.readFileExcel(fileImportPathOut);
        Sheet sheetFileImport = workBook2.getSheetAt(0);

        Row rowFileImport = sheetFileImport.getRow(rowStart);

        if (rowFileImport == null) {
            return false;
        }

        Row rowTemplate = sheetTemplate.getRow(rowStart);
        String arrHeaderTemplate = "";
        String arrHeaderFileImport = "";
        int k = rowTemplate.getLastCellNum();
        for (int i = 0; i < k; i++) {
            if (rowTemplate.getCell(i).getStringCellValue() != null) {
                arrHeaderTemplate += "," + rowTemplate.getCell(i).getStringCellValue();
            } else {
                break;
            }
        }
        int l = rowFileImport.getLastCellNum();
        for (int j = 0; j < l; j++) {
            Cell cell = rowFileImport.getCell(j);
            if (cell == null) {
                break;
            }
            if (rowFileImport.getCell(j).getStringCellValue() != null) {
                arrHeaderFileImport += "," + rowFileImport.getCell(j).getStringCellValue();
            } else {
                break;
            }
        }
        if (!arrHeaderTemplate.equals(arrHeaderFileImport)) {
            return false;
        }

        return true;
    }

    public static String saveUploadedFile(MultipartFile file) throws IOException {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String currentTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String originalName = file.getOriginalFilename();
        String fileName = FilenameUtils.getBaseName(originalName) + "_" + currentTime + "."
                + FilenameUtils.getExtension(originalName);
        byte[] bytes = file.getBytes();
        ResourceBundle resource = ResourceBundle.getBundle("config/globalConfig");
        String UPLOADED_FOLDER = resource.getString("FOLDER_UPLOAD") + currentDate + "/";
        File folderUpload = new File(UPLOADED_FOLDER);
        if (!folderUpload.exists()) {
            folderUpload.mkdir();
        }
        Path path = Paths.get(UPLOADED_FOLDER + fileName);
        Files.write(path, bytes);
        return path.toString();
    }

    public static HashMap<String, String> decompressZipFile(MultipartFile file, String dateFolder) throws IOException {
        HashMap<String, String> mapFileName = new HashMap<>();
        ResourceBundle resource = ResourceBundle.getBundle("config/globalConfig");
        String uploadFolder = resource.getString("FOLDER_UPLOAD");
        File folderUpload = new File(uploadFolder + File.separator + dateFolder);
        if (!folderUpload.exists()) {
            folderUpload.mkdirs();
        }

        // Luu file zip
        byte[] bytes = file.getBytes();
        String originalName = file.getOriginalFilename();
        String fileName = FilenameUtils.getBaseName(originalName) + "."
                + FilenameUtils.getExtension(originalName);
        Path path = Paths.get(folderUpload.getPath() + File.separator + fileName);
        Files.write(path, bytes);

        // Decompress
        FileInputStream is = new FileInputStream(path.toFile());
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(is);
            ZipEntry ze = zis.getNextEntry();
            byte[] buffer = new byte[1024];

            String currentTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            while (ze != null) {
                String _originalName = ze.getName();
                String _fileName = FilenameUtils.getBaseName(_originalName) + "_" + currentTime + "."
                        + FilenameUtils.getExtension(_originalName);

                mapFileName.put(_originalName, _fileName);

                File newFile = new File(folderUpload.getPath() + File.separator + _fileName);
                FileOutputStream fos = new FileOutputStream(newFile);
                try {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    ze = zis.getNextEntry();
                } catch (Exception e) {
                    throw e;
                } finally {
                    fos.close();
                }
            }
            zis.closeEntry();
        } catch (Exception e) {
            throw e;
        } finally {
            if(zis != null){
                zis.close();
            }
            is.close();
        }

        return mapFileName;
    }

    public static ResultDto validateFileImport(String path, String templatePath, int rowStart) throws UnsupportedEncodingException {
        ResultDto resultDTO = new ResultDto();
        resultDTO.setKey(com.viettel.api.config.Constants.RESULT.SUCCESS);
        if (!path.toLowerCase().contains(".xls")) {
            resultDTO.setKey(com.viettel.api.config.Constants.RESULT.FILE_TYPE_INVALID);
        } else {
            boolean check = validateFileTmp(path, templatePath, rowStart);
            if (!check) {
                resultDTO.setKey(com.viettel.api.config.Constants.RESULT.FILE_INVALID);
            }
        }
        return resultDTO;
    }
}
