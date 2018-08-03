package com.viettel.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.viettel.api.dto.CellConfigExport;
import com.viettel.api.dto.ConfigFileExport;
import com.viettel.api.dto.ConfigHeaderExport;

/**
 * Created by VTN-PTPM-NV04 on 2/6/2018.
 */
public class CommonExport {
    public static final Logger LOG = Logger.getLogger(CommonExport.class);

    public static final String XLSX_FILE_EXTENTION = ".xlsx";
    public static final String DOC_FILE_EXTENTION = ".doc";
    public static final String DOCX_FILE_EXTENTION = ".docx";
    public static final String XLSM_FILE_EXTENTION = ".xlsm";
    public static final String PDF_FILE_EXTENTION = ".pdf";

    public static File exportFileResult(
            List<Object[]> lstImport,
            List<ErrorInfo> cellErrorList,
            String templatePath,
            String pathOut,
            String fileName,
            int startRow,
            int columResult
    ) {
        File folderOut = new File(pathOut);
        if (!folderOut.exists()) {
            folderOut.mkdir();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
        String strCurTimeExp = dateFormat.format(new Date());
        strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(":", "_");

        fileName += strCurTimeExp + XLSX_FILE_EXTENTION;

        String resultPath = pathOut + fileName;

        try {
            ExcelWriterUtils ewu = new ExcelWriterUtils();

            Workbook workBook = ewu.readFileExcel(templatePath);
            Sheet sheetOne = workBook.getSheetAt(0);
            
            ((XSSFSheet) sheetOne).createFreezePane(0, 0);
            ((XSSFSheet) sheetOne).getCTWorksheet().getSheetViews().getSheetViewArray(0).setTopLeftCell("A1");
            ((XSSFSheet) sheetOne).setActiveCell(new CellAddress("A1"));
            workBook.setActiveSheet(0); 
            workBook.setSelectedTab(0); 

            CellStyle cellStyle = workBook.createCellStyle();
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setWrapText(false);

            //insert data from line i + 1
            int i = startRow;
            //CellStyle cellSt = null;
            //Row rowHead = sheetOne.getRow(i - 1);
            //Cell cellHead = rowHead.getCell(0);
            CellStyle cellStResult = workBook.createCellStyle();
            cellStResult.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStResult.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStResult.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStResult.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStResult.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStResult.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStResult.setFillForegroundColor(HSSFColor.RED.index);
            cellStResult.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStResult.setWrapText(true);
            ewu.createCell(sheetOne, columResult, i - 1, BundleUtils.getLangString("common.result"), cellStResult);
            if (lstImport != null && lstImport.size() > 0) {
                for (Object[] row : lstImport) {
                    for (int k = 0; k < row.length; k++) {
                        ewu.createCell(sheetOne, k, i, row[k] == null ? "" : row[k].toString().trim(), cellStyle);
                        ewu.createCell(sheetOne, lstImport.get(0).length, i, "", cellStyle);
                    }
                    if (cellErrorList.isEmpty()) {
                        ewu.createCell(sheetOne, row.length, i, BundleUtils.getLangString("common.success"), cellStyle);
                    }
                    i++;
                }
                cellErrorList.forEach((err) -> {
                    ewu.createCell(sheetOne, lstImport.get(0).length, err.getRow(), err.getMsg(), cellStyle);
                });
            }
            ewu.saveToFileExcel(pathOut, fileName);
        } catch (Exception e) {
            LOG.error(e);
        }
        return new File(resultPath);
    }

    public static File exportExcel(
            String pathTemplate,
            String fileNameOut,
            List<ConfigFileExport> config,
            String pathOut,
            String... exportChart
    ) throws Exception {
        File folderOut = new File(pathOut);
        if (!folderOut.exists()) {
            folderOut.mkdir();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
        String strCurTimeExp = dateFormat.format(new Date());
        strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
        pathOut = pathOut + fileNameOut + strCurTimeExp + (exportChart != null && exportChart.length > 0 ? XLSM_FILE_EXTENTION : XLSX_FILE_EXTENTION);

        try {
            InputStream fileTemplate = new FileInputStream(pathTemplate);
            XSSFWorkbook workbook_temp = new XSSFWorkbook(fileTemplate);

            SXSSFWorkbook workbook = new SXSSFWorkbook(workbook_temp, 1000);

            //<editor-fold defaultstate="collapsed" desc="Declare style">
            CellStyle cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            cellStyleFormatNumber.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            cellStyleFormatNumber.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyleFormatNumber.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyleFormatNumber.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyleFormatNumber.setBorderTop(HSSFCellStyle.BORDER_THIN);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setWrapText(true);

            Font xSSFFont = workbook.createFont();
            xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
            xSSFFont.setFontHeightInPoints((short) 11);
            xSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            xSSFFont.setColor(HSSFColor.BLACK.index);

            CellStyle cellStyleTitle = workbook.createCellStyle();
            cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyleTitle.setFont(xSSFFont);

            Font xSSFFontHeader = workbook.createFont();
            xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
            xSSFFontHeader.setFontHeightInPoints((short) 10);
            xSSFFontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            xSSFFontHeader.setColor(HSSFColor.BLUE.index);

            Font subTitleFont = workbook.createFont();
            subTitleFont.setFontName(HSSFFont.FONT_ARIAL);
            subTitleFont.setFontHeightInPoints((short) 11);
            subTitleFont.setColor(HSSFColor.BLACK.index);

            CellStyle cellStyleSubTitle = workbook.createCellStyle();
            cellStyleSubTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyleSubTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyleSubTitle.setFont(subTitleFont);

            CellStyle cellStyleHeader = workbook.createCellStyle();
            cellStyleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyleHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyleHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyleHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyleHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyleHeader.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
            cellStyleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyleHeader.setWrapText(true);
            cellStyleHeader.setFont(xSSFFontHeader);

            CellStyle cellStyleLeft = workbook.createCellStyle();
            cellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyleLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyleLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyleLeft.setWrapText(true);

            CellStyle cellStyleRight = workbook.createCellStyle();
            cellStyleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            cellStyleRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyleRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyleRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyleRight.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyleRight.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyleRight.setWrapText(true);
            //gnoc_cr
            CellStyle cellStyleHeaderOver = workbook.createCellStyle();
            cellStyleHeaderOver.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cellStyleHeaderOver.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyleHeaderOver.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyleHeaderOver.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyleHeaderOver.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyleHeaderOver.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyleHeaderOver.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
            cellStyleHeaderOver.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyleHeaderOver.setWrapText(true);
            cellStyleHeaderOver.setFont(xSSFFontHeader);

            CellStyle cellStyleCenter = workbook.createCellStyle();
            cellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyleCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyleCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyleCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyleCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyleCenter.setWrapText(true);

            CellStyle right = workbook.createCellStyle();
            right.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            right.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            right.setWrapText(true);

            CellStyle left = workbook.createCellStyle();
            left.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            left.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            left.setWrapText(true);

            CellStyle center = workbook.createCellStyle();
            center.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            center.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            center.setWrapText(true);
            //</editor-fold>

            for (ConfigFileExport item : config) {
                Locale locale;
                if (StringUtils.isNotNullOrEmpty(item.getLangKey())) {
                    locale = Locale.forLanguageTag(item.getLangKey());
                } else {
                    locale = Locale.forLanguageTag("vi");
                }

                Map<String, String> fieldSplit = item.getFieldSplit();
                SXSSFSheet sheet;
                if (exportChart != null && exportChart.length > 0) {
                    sheet = workbook.getSheetAt(0);
                } else {
                    sheet = workbook.createSheet(item.getSheetName());
                }

                // Title
                Row rowMainTitle = sheet.createRow(item.getCellTitleIndex());
                Cell mainCellTitle = rowMainTitle.createCell(1);
                mainCellTitle.setCellValue(item.getTitle() == null ? "" : item.getTitle());
                mainCellTitle.setCellStyle(cellStyleTitle);
                sheet.addMergedRegion(new CellRangeAddress(item.getCellTitleIndex(), item.getCellTitleIndex(), 1, item.getMergeTitleEndIndex() + 1));

                // Sub title
                Row rowSubTitle = sheet.createRow(item.getCellTitleIndex() + 1);
                Cell cellTitle = rowSubTitle.createCell(1);
                cellTitle.setCellValue(item.getSubTitle() == null ? "" : item.getSubTitle());
                cellTitle.setCellStyle(cellStyleTitle);
                sheet.addMergedRegion(new CellRangeAddress(item.getCellTitleIndex() + 1, item.getCellTitleIndex() + 1, 1, item.getMergeTitleEndIndex() + 1));

                int indexRowData = 0;
                //<editor-fold defaultstate="collapsed" desc="Build header">
                if (item.isCreatHeader()) {
                    int index = -1;
                    Cell cellHeader;
                    Row rowHeader = sheet.createRow(item.getStartRow());
                    rowHeader.setHeight((short) 500);
                    Row rowHeaderSub = null;
                    for (ConfigHeaderExport header : item.getHeader()) {
                        if (fieldSplit != null) {
                            if (fieldSplit.get(header.getFieldName()) != null) {
                                String[] fieldSplitHead = fieldSplit.get(header.getFieldName()).split(item.getSplitChar());
                                for (String field : fieldSplitHead) {
                                    cellHeader = rowHeader.createCell(index + 2);
                                    cellHeader.setCellValue(field == null ? "" : field.replaceAll("\\<.*?>", " "));
                                    if (header.isHasMerge()) {
                                        CellRangeAddress cellRangeAddress = new CellRangeAddress(
                                                item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                                                index + 2, index + 2 + header.getMergeColumn());
                                        sheet.addMergedRegion(cellRangeAddress);
                                        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, cellRangeAddress,
                                                sheet);
                                        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, cellRangeAddress,
                                                sheet);
                                        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, cellRangeAddress,
                                                sheet);
                                        RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, cellRangeAddress,
                                                sheet);

                                        if (header.getMergeRow() > 0) {
                                            indexRowData = header.getMergeRow();
                                        }
                                        if (header.getMergeColumn() > 0) {
                                            index++;
                                        }

                                        if (header.getSubHeader().length > 0) {
                                            if (rowHeaderSub == null) {
                                                rowHeaderSub = sheet.createRow(item.getStartRow() + 1);
                                            }

                                            int k = index + 1;
                                            int s = 0;
                                            for (String sub : header.getSubHeader()) {
                                                Cell cellHeaderSub1 = rowHeaderSub.createCell(k);
                                                cellHeaderSub1.setCellValue(BundleUtils.getLangString(item.getHeaderPrefix() + "." + sub, locale));
                                                cellHeaderSub1.setCellStyle(cellStyleHeader);

                                                k++;
                                                s++;
                                            }
                                        }
                                    }
                                    cellHeader.setCellStyle(cellStyleHeader);
                                    index++;
                                }
                            } else {
                                cellHeader = rowHeader.createCell(index + 2);
                                cellHeader.setCellValue(BundleUtils.getLangString(item.getHeaderPrefix() + "." + header.getFieldName(), locale));
                                if (header.isHasMerge()) {
                                    CellRangeAddress cellRangeAddress = new CellRangeAddress(
                                            item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                                            index + 2, index + 2 + header.getMergeColumn());
                                    sheet.addMergedRegion(cellRangeAddress);
                                    RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, cellRangeAddress,
                                            sheet);
                                    RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, cellRangeAddress,
                                            sheet);
                                    RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, cellRangeAddress,
                                            sheet);
                                    RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);

                                    if (header.getMergeRow() > 0) {
                                        indexRowData = header.getMergeRow();
                                    }
                                    if (header.getMergeColumn() > 0) {
                                        index++;
                                    }
                                }
                                cellHeader.setCellStyle(cellStyleHeader);
                                index++;
                            }
                        } else {
                            cellHeader = rowHeader.createCell(index + 2);
                            cellHeader.setCellValue(BundleUtils.getLangString(item.getHeaderPrefix() + "." + header.getFieldName(), locale));
                            if (header.isHasMerge()) {
                                CellRangeAddress cellRangeAddress = new CellRangeAddress(item.getStartRow(),
                                        item.getStartRow() + header.getMergeRow(), index + 2,
                                        index + 2 + header.getMergeColumn());
                                sheet.addMergedRegion(cellRangeAddress);
                                RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);

                                if (header.getMergeRow() > 0) {
                                    indexRowData = header.getMergeRow();
                                }
                                if (header.getMergeColumn() > 0) {
                                    index++;
                                }
                            }
                            cellHeader.setCellStyle(cellStyleHeader);
                            index++;
                        }
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="Build other cell">
                if (item.getLstCreatCell() != null) {
                    Row row;
                    for (CellConfigExport cell : item.getLstCreatCell()) {
                        row = sheet.getRow(cell.getRow());
                        if (row == null) {
                            row = sheet.createRow(cell.getRow());
                        }
                        row.setHeight((short)-1);
                        Cell newCell = row.createCell(cell.getColumn());
                        if ("NUMBER".equals(cell.getStyleFormat())) {
                            newCell.setCellValue(Double.valueOf(cell.getValue()));
                        } else {
                            newCell.setCellValue(cell.getValue() == null ? "" : cell.getValue());
                        }

                        if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
                            CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                                    cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                                    cell.getColumn() + cell.getColumnMerge());
                            sheet.addMergedRegion(cellRangeAddress);
                            RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                        }

                        if ("HEAD".equals(cell.getAlign())) {
                            newCell.setCellStyle(cellStyleHeader);
                        }
                        if ("CENTER".equals(cell.getAlign())) {
                            newCell.setCellStyle(cellStyleCenter);
                        }
                        if ("LEFT".equals(cell.getAlign())) {
                            newCell.setCellStyle(cellStyleLeft);
                        }
                        if ("RIGHT".equals(cell.getAlign())) {
                            newCell.setCellStyle(cellStyleRight);
                        }
                        if ("CENTER_NONE_BORDER".equals(cell.getAlign())) {
                            newCell.setCellStyle(center);
                        }
                        if ("LEFT_NONE_BORDER".equals(cell.getAlign())) {
                            newCell.setCellStyle(left);
                        }
                        if ("RIGHT_NONE_BORDER".equals(cell.getAlign())) {
                            newCell.setCellStyle(right);
                        }
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="Fill data">
                if (item.getLstData() != null && !item.getLstData().isEmpty()) {
                    //init mapColumn
                    Object firstRow = item.getLstData().get(0);
                    Map<String, Field> mapField = new HashMap<String, Field>();
                    for (ConfigHeaderExport header : item.getHeader()) {
                        for (Field f : firstRow.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if (f.getName().equals(header.getFieldName())) {
                                mapField.put(header.getFieldName(), f);
                            }
                            String[] replace = header.getReplace();
                            if (replace != null) {
                                if (replace.length > 2) {
                                    for (int n = 2; n < replace.length; n++) {
                                        if (f.getName().equals(replace[n])) {
                                            mapField.put(replace[n], f);
                                        }
                                    }
                                }
                            }
                        }
                        if (firstRow.getClass().getSuperclass() != null) {
                            for (Field f : firstRow.getClass()
                                    .getSuperclass().getDeclaredFields()) {
                                f.setAccessible(true);
                                if (f.getName().equals(header.getFieldName())) {
                                    mapField.put(header.getFieldName(), f);
                                }
                                String[] replace = header.getReplace();
                                if (replace != null) {
                                    if (replace.length > 2) {
                                        for (int n = 2; n < replace.length; n++) {
                                            if (f.getName().equals(replace[n])) {
                                                mapField.put(replace[n], f);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //fillData
                    Row row;
                    List lstData = item.getLstData();
                    List<ConfigHeaderExport> lstHeader = item.getHeader();
                    int startRow = item.getStartRow();
                    String splitChar = item.getSplitChar();
                    for (int i = 0; i < lstData.size(); i++) {
                        row = sheet.createRow(i + startRow + 1 + indexRowData);
                        //row.setHeight((short) 500);
                        row.setHeight((short)-1);
                        Cell cell;

                        cell = row.createCell(0);
                        cell.setCellValue(i + 1);
                        cell.setCellStyle(cellStyleCenter);

                        int j = 0;
                        for (int e = 0; e < lstHeader.size(); e++) {
                            ConfigHeaderExport head = lstHeader.get(e);
                            String header = head.getFieldName();
                            String align = head.getAlign();
                            Object obj = lstData.get(i);

                            Field f = mapField.get(header);

                            if (fieldSplit != null && fieldSplit.containsKey(header)) {
                                String[] arrHead = fieldSplit.get(header).split(splitChar);
                                String value = f.get(obj) == null ? "" : f.get(obj).toString();
                                String[] fieldSplitValue = value.split(splitChar);
                                for (int m = 0; m < arrHead.length; m++) {
                                    if (head.isHasMerge() && head.getSubHeader().length > 0) {
                                        int s = 0;
                                        for (String sub : head.getSubHeader()) {
                                            cell = row.createCell(j + 1);
                                            String[] replace = head.getReplace();
                                            if (replace != null) {
                                                List<String> more = new ArrayList<>();
                                                if (replace.length > 2) {
                                                    for (int n = 2; n < replace.length; n++) {
                                                        Object objStr = mapField.get(replace[n]).get(obj);
                                                        String valueStr = objStr == null ? "" : objStr.toString();
                                                        more.add(valueStr);
                                                    }
                                                }
                                                if ("NUMBER".equals(head.getStyleFormat())) {
                                                    double numberValue = replaceNumberValue(replace[0], m,
                                                            more, s);
                                                    if (Double.compare(numberValue, -888) == 0) {
                                                        cell.setCellValue("*");
                                                    } else if (Double.compare(numberValue, -999) == 0) {
                                                        cell.setCellValue("-");
                                                    } else {
                                                        cell.setCellValue(numberValue);
                                                    }
                                                } else {
                                                    cell.setCellValue(replaceStringValue(replace[0], m, more,
                                                            s, locale));
                                                }
                                                s++;
                                            } else {
                                                String subValue = "";
                                                for (Field subf : firstRow.getClass().getDeclaredFields()) {
                                                    subf.setAccessible(true);
                                                    if (subf.getName().equals(sub)) {
                                                        String[] arrSub = (subf.get(obj) == null ? "" : subf.get(
                                                                obj).toString()).split(item.getSplitChar());
                                                        subValue = arrSub[m];
                                                    }
                                                }
                                                if ("NUMBER".equals(head.getStyleFormat())) {
                                                    if (StringUtils.isNotNullOrEmpty(subValue)) {
                                                        cell.setCellValue(Double.valueOf(subValue));
                                                    } else {
                                                        cell.setCellValue(subValue == null ? "" : subValue);
                                                    }
                                                } else {
                                                    cell.setCellValue(subValue == null ? "" : subValue);
                                                }
                                            }

                                            if ("CENTER".equals(align)) {
                                                cell.setCellStyle(cellStyleCenter);
                                            }
                                            if ("LEFT".equals(align)) {
                                                cell.setCellStyle(cellStyleLeft);
                                            }
                                            if ("RIGHT".equals(align)) {
                                                cell.setCellStyle(cellStyleRight);
                                            }
                                            j++;
                                        }
                                    } else {
                                        cell = row.createCell(j + 1);

                                        String[] replace = head.getReplace();
                                        if (replace != null) {
                                            Object valueReplace = mapField.get(replace[1]).get(obj);
                                            List<String> more = new ArrayList<>();
                                            if (replace.length > 2) {
                                                for (int n = 2; n < replace.length; n++) {
                                                    Object objStr = mapField.get(replace[n]).get(obj);
                                                    String valueStr = objStr == null ? "" : objStr.toString();
                                                    more.add(valueStr);
                                                }
                                            }
                                            if ("NUMBER".equals(head.getStyleFormat())) {
                                                double numberValue = replaceNumberValue(replace[0],
                                                        valueReplace, more, m);
                                                if (Double.compare(numberValue, -888) == 0) {
                                                    cell.setCellValue("*");
                                                } else if (Double.compare(numberValue, -999) == 0) {
                                                    cell.setCellValue("-");
                                                } else {
                                                    cell.setCellValue(numberValue);
                                                }
                                            } else {
                                                cell.setCellValue(replaceStringValue(replace[0], valueReplace,
                                                        more, m, locale));
                                            }
                                        } else {
                                            if ("NUMBER".equals(head.getStyleFormat())) {
                                                if (StringUtils.isNotNullOrEmpty(fieldSplitValue[m])) {
                                                    cell.setCellValue(Double.valueOf(fieldSplitValue[m]));
                                                } else {
                                                    cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                                                }
                                            } else {
                                                cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                                            }
                                        }

                                        if ("CENTER".equals(align)) {
                                            cell.setCellStyle(cellStyleCenter);
                                        }
                                        if ("LEFT".equals(align)) {
                                            cell.setCellStyle(cellStyleLeft);
                                        }
                                        if ("RIGHT".equals(align)) {
                                            cell.setCellStyle(cellStyleRight);
                                        }
                                        j++;
                                    }
                                }
                            } else {
                                String value = f.get(obj) == null ? "" : f.get(obj).toString();
                                cell = row.createCell(j + 1);

                                String[] replace = head.getReplace();
                                if (replace != null) {
                                    Object valueReplace = mapField.get(replace[1]).get(obj);
                                    List<String> more = new ArrayList<>();
                                    if (replace.length > 2) {
                                        for (int n = 2; n < replace.length; n++) {
                                            Object objStr = mapField.get(replace[n]).get(obj);
                                            String valueStr = objStr == null ? "" : objStr.toString();
                                            more.add(valueStr);
                                        }
                                    }
                                    if ("NUMBER".equals(head.getStyleFormat())) {
                                        double numberValue = replaceNumberValue(replace[0], valueReplace, more,
                                                i);
                                        if (Double.compare(numberValue, -888) == 0) {
                                            cell.setCellValue("*");
                                        } else if (Double.compare(numberValue, -999) == 0) {
                                            cell.setCellValue("-");
                                        } else {
                                            cell.setCellValue(numberValue);
                                        }
                                    } else {
                                        cell.setCellValue(
                                                replaceStringValue(replace[0], valueReplace, more, i, locale));
                                    }
                                } else {
                                    if ("NUMBER".equals(head.getStyleFormat())) {
                                        if (StringUtils.isNotNullOrEmpty(value)) {
                                            cell.setCellValue(Double.valueOf(value));
                                        } else {
                                            cell.setCellValue(value == null ? "" : value);
                                        }
                                    } else {
                                        cell.setCellValue(value == null ? "" : value);
                                    }
                                }

                                if ("CENTER".equals(align)) {
                                    cell.setCellStyle(cellStyleCenter);
                                }
                                if ("LEFT".equals(align)) {
                                    cell.setCellStyle(cellStyleLeft);
                                }
                                if ("RIGHT".equals(align)) {
                                    cell.setCellStyle(cellStyleRight);
                                }

                                j++;
                            }
                        }
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="Merge row">
                if (item.getLstCellMerge() != null) {
                    for (CellConfigExport cell : item.getLstCellMerge()) {
                        if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
                            CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                                    cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                                    cell.getColumn() + cell.getColumnMerge());
                            sheet.addMergedRegion(cellRangeAddress);
                            RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                            RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                        }
                    }
                }
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="Auto size column">
                sheet.trackAllColumnsForAutoSizing();
                for (int i = 0; i <= item.getHeader().size(); i++) {
                    sheet.autoSizeColumn(i);
                    if (sheet.getColumnWidth(i) > 20000) {
                        sheet.setColumnWidth(i, 20000);
                    }
                }
                //</editor-fold>
            }

            if (exportChart == null || exportChart.length == 0) {
                workbook.removeSheetAt(0);
            }

            if (exportChart != null && exportChart.length > 0) {
                //<editor-fold defaultstate="collapsed" desc="Ve bieu do">
                ConfigFileExport item = config.get(0);
                Sheet sheetConf = workbook_temp.getSheet("conf");

                // Dong bat dau du lieu cua chart
                Row rowStartConf = sheetConf.getRow(0);
                Cell cellStartConf = rowStartConf.getCell(1);
                cellStartConf.setCellValue(item.getStartRow() + 1);

                // Dong ket thuc du lieu cua chart
                Row rowEndConf = sheetConf.getRow(1);
                Cell cellEndConf = rowEndConf.getCell(1);
                cellEndConf.setCellValue(item.getStartRow() + 1 + item.getLstData().size());

                // Cot bat dau du lieu cua chart
                CellReference xStartReference = new CellReference(item.getStartRow(), Integer.valueOf(exportChart[1]));
                String xStart = "";
                String xStartString = xStartReference.formatAsString();
                for (int i = 0; i < xStartString.length(); i++) {
                    if (DataUtil.isNumber(String.valueOf(xStartString.charAt(i)))) {
                        break;
                    }
                    xStart += String.valueOf(xStartString.charAt(i));
                }

                // Cot ket thuc du lieu cua chart
                CellReference xEndReference = new CellReference(item.getStartRow(), Integer.valueOf(exportChart[2]) + Integer.valueOf(exportChart[1]) - 1);
                String xEnd = "";
                String xEndString = xEndReference.formatAsString();
                for (int i = 0; i < xEndString.length(); i++) {
                    if (DataUtil.isNumber(String.valueOf(xEndString.charAt(i)))) {
                        break;
                    }
                    xEnd += String.valueOf(xEndString.charAt(i));
                }

                // xAxis
                Row rowXvalue = sheetConf.getRow(2);
                Cell cellXvalue = rowXvalue.getCell(1);
                cellXvalue.setCellValue("=TổngHợp!$" + xStart + "${startRow}:$" + xEnd + "${startRow}");

                // Categories
                Row rowNameList = sheetConf.getRow(3);
                Cell cellNameList = rowNameList.getCell(1);
                cellNameList.setCellValue("=TổngHợp!$" + exportChart[0] + "${i}");

                // Data
                Row rowDataValue = sheetConf.getRow(4);
                Cell cellDataValue = rowDataValue.getCell(1);
                cellDataValue.setCellValue("=TổngHợp!$" + xStart + "${i}:$" + xEnd + "${i}");
                //</editor-fold>
            }

            try {
                FileOutputStream fileOut = new FileOutputStream(pathOut);
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            } catch (IOException e) {
                LOG.error(e);
            }
        } catch (FileNotFoundException e) {
            LOG.error(e);
        }
        return new File(pathOut);
    }

    public static double replaceNumberValue(String modul, Object valueReplace, List<String> more, int index) {
        double valueReturn = 0;
        String strValue = valueReplace == null ? "" : valueReplace.toString();
        DecimalFormat df = new DecimalFormat("#.##");
        switch (modul) {
            case "CDBR-TGXL-VALUE":
                int i = Integer.valueOf(strValue);
                strValue = more.get(index);
                if (StringUtils.isNotNullOrEmpty(strValue)) {
                    String[] arrValue = strValue.split("#");
                    if (arrValue.length > 0) {
                        valueReturn = Double.valueOf(arrValue[i]);
                    }
                } else {
                    valueReturn = -888; // *
                }
                break;
            case "CDBR-TGXL-RATE":
                if (StringUtils.isNotNullOrEmpty(strValue)) {
                    String[] arrValue = strValue.split("#");
                    if (arrValue.length > 0) {
                        valueReturn = (Double.valueOf(arrValue[index]) != -1 ? Double.valueOf(arrValue[index]) : 0);
                    }
                } else {
                    valueReturn = -888; // *
                }
                break;
            case "PACLM-TIME":
                if (StringUtils.isNotNullOrEmpty(strValue)) {
                    String[] arrValue = strValue.split("-");
                    if (arrValue.length > 0) {
                        valueReturn = (Double.valueOf(arrValue[index]) != -1 ? Double.valueOf(arrValue[index]) : 0);
                    }
                } else {
                    valueReturn = -888; // *
                }
                break;
            case "CDBR-TGXL-TARGET":
                if ("0".equals(more.get(0)) || "0.0".equals(more.get(0))) {
                    valueReturn = valueReplace == null ? 0 : Double.valueOf(valueReplace.toString());
                } else {
                    valueReturn = -999; // -
                }
                break;

            case "CDBR-SCLL-REASON-GROUP-RATE":
                String countErrorList = more.get(0);
                String totalErrorList = more.get(1);
                if (StringUtils.isNotNullOrEmpty(countErrorList)) {
                    if (StringUtils.isNotNullOrEmpty(totalErrorList)) {
                        String[] arrCount = countErrorList.split("#");
                        String[] arrTotal = totalErrorList.split("#");

                        if (index < arrTotal.length && ("0".equals(arrTotal[index]) || "0.0".equals(arrTotal[index]))) {
                            valueReturn = 0d;
                        } else {
                            valueReturn = Double.valueOf(df.format(Double.valueOf(arrCount[index]) * 100 / Double.valueOf(arrTotal[index])));
                        }
                    } else {
                        valueReturn = -888;
                    }
                } else {
                    valueReturn = -888;
                }
                break;
            case "CDBR-SCLL-REASON-GROUP-AVG":
                if (StringUtils.isNotNullOrEmpty(more.get(0))) {
                    if (StringUtils.isNotNullOrEmpty(more.get(1))) {
                        String[] arrCount = more.get(0).split("#");
                        String[] arrTotal = more.get(1).split("#");

                        if (index < arrTotal.length && ("0".equals(arrTotal[index]) || "0.0".equals(arrTotal[index]))) {
                            valueReturn = 0d;
                        } else {
                            Double rate = 0d;
                            for (int k = 0; k < arrCount.length; k++) {
                                if (Double.valueOf(arrTotal[k]) != 0) {
                                    rate += Double.valueOf(arrCount[k]) * 100 / Double.valueOf(arrTotal[k]);
                                }
                            }

                            valueReturn = Double.valueOf(df.format(rate / arrCount.length));
                        }
                    } else {
                        valueReturn = 0;
                    }
                } else {
                    valueReturn = 0;
                }
                break;
            case "CDBR-PAKH-TARGET":
                if ("0".equals(more.get(0)) || "0.0".equals(more.get(0))) {
                    valueReturn = valueReplace == null ? 0 : Double.valueOf(valueReplace.toString());
                } else {
                    valueReturn = -999; // -
                }
                break;
            case "CDBR-TKM-AVG":
            case "REPORT-PTBRM-CHOT-PHAT-HAPPY-CALL":
                if (StringUtils.isNotNullOrEmpty(strValue)) {
                    valueReturn = Double.valueOf(strValue);
                } else {
                    valueReturn = -888; // *
                }
                break;
            case "CDBR-TKM-RATE":
                if (StringUtils.isNotNullOrEmpty(strValue)) {
                    String[] arrValue = strValue.split("#");
                    if (arrValue.length > 0) {
                        valueReturn = (Double.valueOf(arrValue[index]) != -1 ? Double.valueOf(arrValue[index]) : 0);
                    }
                } else {
                    valueReturn = -888; // *
                }
                break;
//            case "GDTT-CDBR-VALUE":
            case "GDTT-CELL-H":
            case "NT-TOTAL":
            case "TT-NUM-CABINET":
                if (StringUtils.isNotNullOrEmpty(strValue)) {
                    String[] arrValue = strValue.split("#");
                    if (arrValue.length > 0) {
                        valueReturn = "-1".equals(arrValue[index]) ? 0 : Double.valueOf(arrValue[index]);
                    }
                } else {
                    valueReturn = -888; // *
                }
                break;
//            case "GDTT-CDBR-TARGET":
//                if (StringUtils.isNotNullOrEmpty(strValue)) {
//                    valueReturn = Double.valueOf(strValue);
//                } else {
//                    valueReturn = -999; // -
//                }
//                break;
            case "GDTT_COMPLETE_RATE":
            case "GDTT_TARGET":
            case "TT-TARGET":
                Double completedRate = Double.valueOf(strValue);
                if (Double.compare(completedRate, -1) == 0) {
                    valueReturn = -999; // -
                } else
                    valueReturn = completedRate;
                break;
            case "NT-TARGET-TH":
            case "NT-TOTAL-TH":
            case "TT-TARGET-TH":
            case "TT-PRIZE-TH":
                if ("*".equals(strValue)) {
                    valueReturn = -888;
                } else if ("-".equals(strValue)) {
                    valueReturn = -999;
                } else {
                    valueReturn = Double.valueOf(strValue);
                }
                break;
            case "GDTT-CDBR-KSUBMIN":
                Double ksubMin = Double.valueOf(strValue);
                if (Double.compare(ksubMin, -1) == 0) {
                    valueReturn = -999; // -
                } else
                    valueReturn = ksubMin;
                break;
            case "GDTT-CDBR-TARGET":
                Double value = Double.valueOf(strValue);
                if (Double.compare(value, -1) == 0) {
                    valueReturn = -999; // -
                } else
                    valueReturn = value;
                break;
            case "GDTT-CDBR-VALUE":
                String[] valueList = strValue.split("#");
                if (valueList.length > 0) {
                    valueReturn = "-1".equals(valueList[index]) ? -999 : Double.valueOf(valueList[index]);
                }
                break;
            case "SCTD-VALUE":
            case "REPORT-NVSC-SPLIT":
                valueList = strValue.split("#");
                if (valueList.length > 0) {
                    valueReturn = Double.valueOf(valueList[index]);
                }
                break;
            case "REPORT-THHH-LOOP":
            case "REPORT-PTBRM-RMM-M2-LOOP":
            case "REPORT-TTTHCT-TARGET-LOOP":
                if (StringUtils.isNotNullOrEmpty(strValue)) {
                    String[] arrValue = strValue.split("#");
                    valueReturn = Double.valueOf(arrValue[index]);
                }
                break;
            case "REPORT-TTTH-999":
                valueReturn = Double.valueOf(strValue);
                break;
        }

        return valueReturn;
    }

    public static String replaceStringValue(String modul, Object valueReplace, List<String> more, int index, Locale... locales) {
        String strReturn = "";
        String strValue = valueReplace == null ? "" : valueReplace.toString();
        switch (modul) {
            case "UNION-MEMBER-STATUS":
                if ("R".equals(strValue)){
                    strReturn = "NOK";
                }
                else if ("A".equals(strValue)){
                    strReturn = "OK";
                }
                else {
                    strReturn = "Chưa duyệt";
                }
                break;
            case "UNION-MEMBER-BARCODE":
                if (StringUtils.isNotNullOrEmpty(strValue)) {
                    if ("0".equals(strValue)){
                        strReturn = "Chưa cấp";
                    }
                    else {
                        strReturn = "Đã cấp";
                    }
                }
                else {
                    strReturn = "Chưa cấp";
                }
                break;
            case "CDBR-TGXL-RESULT":
            case "CDBR-TKM-RESULT":
                if ("0".equals(more.get(0)) || "0.0".equals(more.get(0))) {
                    if (StringUtils.isNotNullOrEmpty(strValue)) {
                        Double targetValue = Double.valueOf(more.get(1));
                        Double avgValue = Double.valueOf(strValue);

                        if (Double.valueOf(avgValue) >= targetValue) {
                            strReturn = "Đạt";
                        } else {
                            strReturn = "Không đạt";
                        }
                    } else {
                        strReturn = "Đạt";
                    }
                } else {
                    strReturn = "-";
                }
                break;
            case "CDBR-SCLL-RESULT":
            case "CDBR-PAKH-RESULT":
                if ("0".equals(more.get(0)) || "0.0".equals(more.get(0)) || "-999".equals(more.get(1))) {
                    if (StringUtils.isNotNullOrEmpty(strValue)) {
                        Double targetValue = Double.valueOf(more.get(1));
                        Double avgValue = Double.valueOf(strValue);

                        if (Double.valueOf(avgValue) <= targetValue) {
                            strReturn = "Đạt";
                        } else {
                            strReturn = "Không đạt";
                        }
                    } else {
                        strReturn = "Đạt";
                    }
                } else {
                    strReturn = "-";
                }
                break;
            case "PACLM-TIME":
                if (StringUtils.isNotNullOrEmpty(strValue)) {
                    String[] arrValue = strValue.split("-");
                    if (arrValue.length > 0) {
                        strReturn = arrValue[arrValue.length - 1];
                    }
                } else {
                    strReturn = "-"; // *
                }
                break;
            case "GDTT-CDBR-KRESULT":
                if (StringUtils.isNotNullOrEmpty(more.get(0))) {
                    Double ksubMinValue = Double.valueOf(strValue);
                    Double targetValue = Double.valueOf(more.get(0));
                    if (ksubMinValue > targetValue) {
                        strReturn = "Không đạt";
                    } else {
                        strReturn = "Đạt";
                    }
                } else {
                    strReturn = "-";
                }
                break;
            case "GDTT-CDBR-VRESULT":
                if (StringUtils.isNotNullOrEmpty(more.get(0))) {
                    if (StringUtils.isNotNullOrEmpty(strValue)) {
                        String[] arrValue = strValue.split("#");
                        Double ksubMin = Double.valueOf(arrValue[arrValue.length - 1]);
                        Double targetValue = Double.valueOf(more.get(0));
                        if (ksubMin > targetValue) {
                            strReturn = "Không đạt";
                        } else {
                            strReturn = "Đạt";
                        }
                    } else {
                        strReturn = "Đạt";
                    }
                } else {
                    strReturn = "-";
                }
                break;
            case "GNOC-CR-LIST-TOTAL-CR":
                if (StringUtils.isNotNullOrEmpty(strValue)) {
                    String[] arrValue = strValue.split("#");
                    strReturn = arrValue[index];
                }
                break;
            case "GDTT_RATE":
            case "NT-PRIZE":
                if (StringUtils.isNotNullOrEmpty(more.get(0))) {
                    Double target = Double.valueOf(strValue);
                    Double cellH = Double.valueOf(more.get(0));
                    Double completedRate = Double.valueOf(more.get(1));
                    if (Double.compare(target, -1) == 0) {
                        strReturn = "-";
                    } else if (completedRate <= 0) {
                        if (Double.compare(target, 0) == 0 && Double.compare(cellH, 0) != 0) {
                            strReturn = "Không đạt";
                        } else {
                            strReturn = "Đạt";
                        }
                    } else {
                        strReturn = "Không đạt";
                    }
                } else {
                    strReturn = "-";
                }
                break;
            case "REPORT-THHH-SINGLE-OR-COMBO":
                if ("0".equals(strValue)) {
                    strReturn = BundleUtils.getLangString("common.single", locales[0]);
                } else {
                    strReturn = BundleUtils.getLangString("common.combo", locales[0]);
                }
                break;
            case "REPORT-THHH-RM-GOODS-RECOVERED":
            case "REPORT-PTBRM-HAPPY-CALL":
            case "REPORT-PTBRM-FINED":
                if ("0".equals(strValue)) {
                    strReturn = BundleUtils.getLangString("no", locales[0]);
                } else {
                    strReturn = BundleUtils.getLangString("yes", locales[0]);
                }
                break;
            case "GNOC-CR-TARGET":
                if("-1".equals(strValue)){
                    strReturn = "-";
                }else{
                    strReturn = strValue + "%";
                }
                break;
            case "REPORT-NVSC-RATE-SPLIT":
                if (StringUtils.isNotNullOrEmpty(strValue)) {
                    String[] arrValue = strValue.split("#");
                    strReturn = "-999".equals(arrValue[index]) ? "-" : (arrValue[index]+"%");
                }
                break;

            default:
                break;
        }

        return strReturn;
    }

    public static File excelToPdf(
            String pathTemplate
            , String pathOut
            , String fileNameOut
    ) throws Exception  {
        File folderOut = new File(pathOut);
        if (!folderOut.exists()) {
            folderOut.mkdir();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
        String strCurTimeExp = dateFormat.format(new Date());
        strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
        pathOut = pathOut + fileNameOut + strCurTimeExp + PDF_FILE_EXTENTION;
        FileOutputStream fileOutputStream = new FileOutputStream(pathOut);
        try {
            FileInputStream inputStream = new FileInputStream(new File("D:\\VuHx\\QL_DOAN_VAO\\SourceCode\\report_out\\UnionMember02_03_2018_17_38_11.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            /*
            InputStream inputStream = new FileInputStream("D:\\VuHx\\QL_DOAN_VAO\\SourceCode\\report_out\\UnionMember02_03_2018_17_38_11.xlsx");
            XSSFWorkbook workbook_temp = new XSSFWorkbook(inputStream);
            SXSSFWorkbook workbook = new SXSSFWorkbook(workbook_temp, 1000);
            */

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            Document pdf = new Document();
            PdfWriter.getInstance(pdf, fileOutputStream);
            pdf.open();

            PdfPTable table = new PdfPTable(7);

            PdfPCell tableCell;
            com.itextpdf.text.Font f = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 50.0f, com.lowagie.text.Font.UNDERLINE, BaseColor.RED);
            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()){
                        case Cell.CELL_TYPE_STRING:
                            tableCell = new PdfPCell(new Phrase(cell.getStringCellValue()));
                            table.addCell(tableCell);

                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            tableCell = new PdfPCell(new Phrase((float) cell.getNumericCellValue()));
                            table.addCell(tableCell);

                            break;
                    }
                }
            }
            pdf.add(table);
            pdf.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            LOG.error(e);
        } finally {
            fileOutputStream.close();
        }
        return new File(pathOut);
    }

    public static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style;

        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font noteFont = wb.createFont();
        noteFont.setFontHeightInPoints((short) 15);
        noteFont.setColor(IndexedColors.RED.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(noteFont);
        style.setWrapText(true);
        styles.put("note", style);

        Font headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(headerFont);
        style.setWrapText(true);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style);

        //trai
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setWrapText(false);
        styles.put("cellLeft", style);


        //phai
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setWrapText(false);
        styles.put("cellRight", style);

        //giua
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setWrapText(false);
        styles.put("cellCenter", style);

        style = wb.createCellStyle();
        style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("#,##0"));
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cellnumber", style);

        return styles;
    }
    /*
    public static File exportFileDoc(
            String pathTemplate,
            String fileNameOut,
            String pathOut
    ) throws Exception {
        File folderOut = new File(pathOut);
        if (!folderOut.exists()) {
            folderOut.mkdir();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        String strCurTimeExp = dateFormat.format(new Date());
        strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
        pathOut = pathOut + fileNameOut + strCurTimeExp + DOCX_FILE_EXTENTION;

        XWPFDocument doc = new XWPFDocument(OPCPackage.open(pathTemplate));
        for (XWPFParagraph p : doc.getParagraphs()){
            List<XWPFRun> runs = p.getRuns();
            if (runs != null){
                for (XWPFRun r : runs){
                    String text = r.getText(0);
                    if (text != null && text.contains("[$item.unionName]")){
                        text = text.replace("[$item.unionName]", "DCM");
                        r.setText(text, 0);
                    }
                }
            }
        }

        for (XWPFTable tbl : doc.getTables()){
            for (XWPFTableRow row : tbl.getRows()){
                for (XWPFTableCell cell : row.getTableCells()){
                    for (XWPFParagraph p : cell.getParagraphs()){
                        for(XWPFRun r : p.getRuns()){
                            String text = r.getText(0);
                            if (text != null && text.contains("[$item.unionName]")){
                                text = text.replace("[$item.unionName]", "DCM");
                                r.setText(text, 0);
                            }
                        }
                    }
                }
            }
        }

        File fileOut = new File(pathOut);
        FileOutputStream out = new FileOutputStream(pathOut);
        doc.write(out);

        out.flush();
        out.close();
        doc.close();

        return fileOut;
    }
    */
}
