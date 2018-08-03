package com.viettel.api.web.rest.boc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.viettel.api.config.Constants;
import com.viettel.api.dto.Datatable;
import com.viettel.api.dto.ResultDto;
import com.viettel.api.dto.boc.BocCodeDto;
import com.viettel.api.dto.boc.BocTargetDto;
import com.viettel.api.dto.boc.CataLocationDto;
import com.viettel.api.dto.boc.KVDto;
import com.viettel.api.service.boc.BocTargetService;
import com.viettel.api.utils.BundleUtils;
import com.viettel.api.utils.CommonExport;
import com.viettel.api.utils.ExcelWriterUtils;
import com.viettel.api.utils.StringUtils;

/**
 * Created by VTN-PTPM-NV23 on 2/6/2018.
 */
@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "bocTarget")
public class BocTargetResource {

    protected Logger logger = LoggerFactory.getLogger(BocTargetResource.class);
    
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String HEADER_CACHE_CONTROL = "must-revalidate, post-check=0, pre-check=0";
    public static final String MEDIATYPE_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Autowired
    BocTargetService bocTargetService;
    
    @PreAuthorize("@roleChecker.hasValidRole('MANAGER_TARGET')")
    @PostMapping("/search")
    @Timed
    public ResponseEntity<Datatable> search(@RequestBody BocTargetDto bocTargetDto) throws URISyntaxException {
        Datatable data = bocTargetService.search(bocTargetDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getTypeTarget")
    @Timed
    public ResponseEntity<List<BocCodeDto>> getTypeTarget() throws URISyntaxException {
    	List<BocCodeDto> data = bocTargetService.getTypeTarget();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @GetMapping("/getListProvince")
    @Timed
    public ResponseEntity<List<CataLocationDto>> getListProvince() throws URISyntaxException {
    	List<CataLocationDto> data = bocTargetService.getListProvince();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @PostMapping("/getListDistrictByProvinceCode")
    @Timed
    public ResponseEntity<List<CataLocationDto>> getListDistrictByProvinceCode(@RequestBody CataLocationDto cataLocationDto) throws URISyntaxException {
    	List<CataLocationDto> data = bocTargetService.getListDistrictByProvinceCode(cataLocationDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @PreAuthorize("@roleChecker.hasValidRole('MANAGER_TARGET_DELETE')")
    @PostMapping("/delete")
    @Timed
    public ResponseEntity<ResultDto> delete(@RequestBody BocTargetDto bocTargetDto) throws URISyntaxException {
    	ResultDto data = bocTargetService.delete(bocTargetDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @PreAuthorize("@roleChecker.hasValidRole('MANAGER_TARGET_EXPORT')")
    @PostMapping("/exportTarget")
    @Timed
    public ResponseEntity<byte[]> exportTarget(@RequestBody BocTargetDto bocTargetDto) throws Exception {
        File fileExport = bocTargetService.exportTarget(bocTargetDto);

        String filePath = fileExport.getPath();
        InputStream is = new FileInputStream(filePath);

        byte[] contents = IOUtils.toByteArray(is);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        String filename = fileExport.getName();
        List<String> customHeaders = new ArrayList<>();
        customHeaders.add("Content-Disposition");
        headers.setAccessControlExposeHeaders(customHeaders);
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
        is.close();
        return response;
    }
    
    @PreAuthorize("@roleChecker.hasValidRole('MANAGER_TARGET_IMPORT')")
    @PostMapping("/importData")
    @Timed
    public ResponseEntity<byte[]> importData(@RequestParam("files") List<MultipartFile> files, @RequestParam("formDataJson") String formDataJson) throws URISyntaxException, IOException {
        ResultDto resultDTO = bocTargetService.importData(files.get(0));
        String messageCode = resultDTO.getKey();
        HttpHeaders headers = new HttpHeaders();
        
        String filename = "";
        byte[] contents = null;
        if(resultDTO.getFile() != null) {
        	File fileExport = resultDTO.getFile();
            String filePath = fileExport.getPath();
            InputStream is = new FileInputStream(filePath);
            contents = IOUtils.toByteArray(is);
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
            filename = fileExport.getName();
            List<String> customHeaders = new ArrayList<>();
            customHeaders.add("Content-Disposition");
            headers.setAccessControlExposeHeaders(customHeaders);
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        }
        headers.set("X-Message-Code", messageCode);
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
        return response;
    }
    
    @PreAuthorize("@roleChecker.hasValidRole('MANAGER_TARGET_IMPORT')")
    @PostMapping("/downloadFileTemp")
    @Timed
    public ResponseEntity<byte[]> downloadFileTemp(){
        try {
            String fileName = "TARGET_IMPORT_TEMP.xlsx";

            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            String templatePathOut = fullPath + "META-INF" + File.separator
                    + "templates" 
                    + File.separator + fileName;

            String pathArr[] = fullPath.split("/target/classes");
            String rootPath = pathArr[0].substring(0, pathArr[0].lastIndexOf("/"));
            rootPath += File.separator + "report_out" + File.separator;

            File fileTemplate;

            //<editor-fold desc="Create template import">
            ExcelWriterUtils ewu = new ExcelWriterUtils();

            templatePathOut = StringUtils.removeSeparator(templatePathOut);
            FileInputStream inputStream = new FileInputStream(templatePathOut);

            XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
            XSSFSheet sheetOne = workBook.getSheetAt(0);
            workBook.setSheetName(0, BundleUtils.getLangString("exportTarget.sheetNameTarget"));
            
            String[] header = new String[]{
                    BundleUtils.getLangString("importTarget.stt")
                    , BundleUtils.getLangString("importTarget.targetMonth")
                    , BundleUtils.getLangString("exportTarget.targetName")
                    , BundleUtils.getLangString("exportTarget.regionCode")
                    , BundleUtils.getLangString("exportTarget.targetNum")
                    , BundleUtils.getLangString("exportTarget.warning1")
                    , BundleUtils.getLangString("exportTarget.warning2")
            };
            String[] headerStar = new String[]{
            		BundleUtils.getLangString("importTarget.targetMonth")
                    , BundleUtils.getLangString("exportTarget.targetName")
                    , BundleUtils.getLangString("exportTarget.regionCode")
                    , BundleUtils.getLangString("exportTarget.targetNum")
            };

            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetOne);
            List<String> listHeader = Arrays.asList(header);
            List<String> listHeaderStar = Arrays.asList(headerStar);

            Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

            //Tao title
            sheetOne.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeader.size() - 1));
            Row titleRow = sheetOne.createRow(1);
            titleRow.setHeightInPoints(45);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(BundleUtils.getLangString("importTarget.titleImport"));
            titleCell.setCellStyle(styles.get("title"));

            //Tao note
            sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
            Row noteRow = sheetOne.createRow(2);
            noteRow.setHeightInPoints(18);
            Cell noteCell = noteRow.createCell(0);
            noteCell.setCellValue(BundleUtils.getLangString("common.notNullStarField"));
            noteCell.setCellStyle(styles.get("note"));

            //Tao header
            XSSFFont fontStar = workBook.createFont();
            fontStar.setColor(IndexedColors.RED.getIndex());

            Row headerRow = sheetOne.createRow(3);
            headerRow.setHeightInPoints(18);
            for (int i = 0; i < listHeader.size(); i++) {
                Cell headerCell = headerRow.createCell(i);
                XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
                for (String checkHeader : listHeaderStar) {
                    if (checkHeader.equalsIgnoreCase(listHeader.get(i))) {
                        rt.append("(*)", fontStar);
                    }
                }
                headerCell.setCellValue(rt);
                headerCell.setCellStyle(styles.get("header"));
                //sheetOne.setColumnWidth(i, 7000);
                sheetOne.autoSizeColumn(i);
            }
            sheetOne.setColumnWidth(0, 3000);
            
            //sheet province district
            List<KVDto> lstData = bocTargetService.getListProvinceDistrictForExport();
            XSSFSheet sheetTwo = workBook.getSheetAt(1);
            workBook.setSheetName(1, BundleUtils.getLangString("exportTarget.sheetNameRegion"));
            Row headerRowTwo = sheetTwo.createRow(0);
            
            Font xSSFFont = workBook.createFont();
            xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
            xSSFFont.setFontHeightInPoints((short) 11);
            xSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            xSSFFont.setColor(HSSFColor.BLACK.index);

            CellStyle cellStyleTitle = workBook.createCellStyle();
            cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyleTitle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            cellStyleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyleTitle.setFont(xSSFFont);
            
            Cell sttHeaderCell = headerRowTwo.createCell(0);
            sttHeaderCell.setCellValue(BundleUtils.getLangString("common.stt"));
            sttHeaderCell.setCellStyle(cellStyleTitle);
            Cell codeHeaderCell = headerRowTwo.createCell(1);
            codeHeaderCell.setCellValue(BundleUtils.getLangString("exportTarget.regionCode"));
            codeHeaderCell.setCellStyle(cellStyleTitle);
            Cell nameHeaderCell = headerRowTwo.createCell(2);
            nameHeaderCell.setCellValue(BundleUtils.getLangString("exportTarget.regionName"));
            nameHeaderCell.setCellStyle(cellStyleTitle);
            for (int i = 0; i < lstData.size(); i++) {
            	Row dataRow = sheetTwo.createRow(i + 1);

                Cell sttNameCell = dataRow.createCell(0);
                sttNameCell.setCellValue(lstData.get(i).getKvId());

                Cell dataCodeCell = dataRow.createCell(1);
                dataCodeCell.setCellValue(lstData.get(i).getKvCode());
                
                Cell dataNameCell = dataRow.createCell(2);
                dataNameCell.setCellValue(lstData.get(i).getKvName());
			}

            // Combo loại mục tiêu
            XSSFSheet sheetParam = workBook.getSheetAt(2);
            int row = 1;
            int partnerTypeColumn = listHeader.indexOf(BundleUtils.getLangString("exportTarget.targetName"));
            List<BocCodeDto> lsTypeTarget = bocTargetService.getTypeTarget();
            for (BocCodeDto dto : lsTypeTarget) {
                ewu.createCell(sheetParam, 0, row++, dto.getDecode(), styles.get("cell"));
            }
            sheetParam.autoSizeColumn(0);
            Name memberTypeName = workBook.createName();
            memberTypeName.setNameName("targetName");
            memberTypeName.setRefersToFormula("param!$A$2:$A$" + row);

            XSSFDataValidationConstraint partnerTypeConstraint = new XSSFDataValidationConstraint(DataValidationConstraint.ValidationType.LIST, "targetName");
            CellRangeAddressList cellRangePartnerType = new CellRangeAddressList(4, 65000, partnerTypeColumn, partnerTypeColumn);
            XSSFDataValidation dataValidationPartnerType = (XSSFDataValidation) dvHelper.createValidation(partnerTypeConstraint, cellRangePartnerType);
            dataValidationPartnerType.setShowErrorBox(true);
            sheetOne.addValidationData(dataValidationPartnerType);
            
            int targetNumColumn = listHeader.indexOf(BundleUtils.getLangString("exportTarget.targetNum"));
            int warning1Column = listHeader.indexOf(BundleUtils.getLangString("exportTarget.warning1"));
            int warning2Column = listHeader.indexOf(BundleUtils.getLangString("exportTarget.warning2"));
            DataFormat fmt = workBook.createDataFormat();
            CellStyle cellStyleNumber = workBook.createCellStyle();
            cellStyleNumber.setDataFormat(fmt.getFormat("@"));
            sheetOne.setDefaultColumnStyle(targetNumColumn, cellStyleNumber);
            sheetOne.setDefaultColumnStyle(warning1Column, cellStyleNumber);
            sheetOne.setDefaultColumnStyle(warning2Column, cellStyleNumber);

            sheetOne.createFreezePane(0, 0);
            sheetOne.getCTWorksheet().getSheetViews().getSheetViewArray(0).setTopLeftCell("A1");
            sheetOne.setActiveCell(new CellAddress("A1"));
            workBook.setActiveSheet(0); 
            workBook.setSelectedTab(0); 
            
            // Ghi file
            String fileOutName = "IMPORT_TARGET" + "_" + System.currentTimeMillis() + ".xlsx";
            ewu.saveToFileExcel(workBook, rootPath, fileOutName);
            String resultPath = rootPath + fileOutName;
            fileTemplate = new File(resultPath);
            //</editor-fold>

            String filePath = fileTemplate.getPath();
            InputStream is = new FileInputStream(filePath);

            byte[] contents = IOUtils.toByteArray(is);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(MEDIATYPE_EXCEL));
            String filename = fileTemplate.getName();
            List<String> customHeaders = new ArrayList<>();
            customHeaders.add(CONTENT_DISPOSITION);
            headers.setAccessControlExposeHeaders(customHeaders);
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl(HEADER_CACHE_CONTROL);

            return new ResponseEntity<>(contents, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
