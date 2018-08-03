package com.viettel.api.utils;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by VTN-PTPM-NV23 on 12/20/2017.
 */
public class ExcelWriterUtils {
    private Workbook workbook;
    private static final Logger logger = Logger.getLogger(ExcelWriterUtils.class);
    private FileOutputStream fileOut;

    /**
     * Method to create a workbook to work with excel
     *
     * @param filePathName ThuanNHT
     */
    public void createWorkBook(String filePathName) {
        if (filePathName.endsWith(".xls") || filePathName.endsWith(".XLS")) {
            workbook = new HSSFWorkbook();
        } else if (filePathName.endsWith(".xlsx") || filePathName.endsWith(".XLSX")) {
            workbook = new XSSFWorkbook();
        }
    }

    /**
     * Method to create a new excel(xls,xlsx) file with file Name
     *
     * @param fileName ThuanNHT
     */
    public void saveToFileExcel(String pathName, String fileName) {
        try {
            File f = new File(pathName);
            if (!f.exists()){
                boolean b = f.mkdirs();
            }
            // R3292_EDIT_DUNGNV50_13122012_START
            fileOut = new FileOutputStream(pathName + fileName);
            // R3292_EDIT_DUNGNV50_13122012_END
            workbook.write(fileOut);
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                fileOut.close();
                workbook = null;
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }

    /**
     * Method to create a new excel(xls,xlsx) file with file Name
     *
     * @param workBook
     * @param pathName
     * @param fileName ThuanNHT
     */
    public void saveToFileExcel(Workbook workBook, String pathName, String fileName) {
        try {
            File f = new File(pathName);
            if (!f.exists()) {
                boolean b = f.mkdirs();
            }
            // R3292_EDIT_DUNGNV50_13122012_START
            fileOut = new FileOutputStream(pathName + fileName);
            // R3292_EDIT_DUNGNV50_13122012_END
            workBook.write(fileOut);
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                fileOut.close();
                workbook = null;
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }

    /**
     * method to create a sheet
     *
     * @param sheetName ThuanNHT
     */
//    public Sheet createSheet(String sheetName) {
//        String temp = WorkbookUtil.createSafeSheetName(sheetName);
//        return workbook.createSheet(temp);
//    }
    /**
     * method t create a row
     *
     * @param r
     * @return ThuanNHT
     */
    public Row createRow(Sheet sheet, int r) {
        Row row = sheet.createRow(r);
        return row;
    }

    /**
     * method to create a cell with value
     *
     * @param cellValue ThuanNHT
     */
    public Cell createCell(Row row, int column, String cellValue) {
        // Create a cell and put a value in it.
        Cell cell = row.createCell(column);
        cell.setCellValue(cellValue);
        return cell;
    }

    /**
     * method to create a cell with value
     *
     * @param cellValue ThuanNHT
     */
    public Cell createCell(Sheet sheet, int c, int r, String cellValue) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        // Create a cell and put a value in it.
        Cell cell = row.createCell(c);
        cell.setCellValue(cellValue);
        return cell;
    }

    /**
     * method to create a cell with value with style
     *
     * @param cellValue ThuanNHT
     */
    public Cell createCell(Sheet sheet, int c, int r, String cellValue, CellStyle style) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        // Create a cell and put a value in it.
        Cell cell = row.createCell(c);
        cell.setCellValue(cellValue);
        cell.setCellStyle(style);
        return cell;
    }

    public Cell createCell(Sheet sheet, int c, int r, Object cellValue, CellStyle style, int align) throws Exception {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        Cell cell = row.createCell(c);
        cell.setCellType(align);
        cell.setCellValue(cellValue == null ? "" : cellValue.toString());
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * Method get primitive content Of cell
     *
     * @param sheet
     * @param c
     * @param r
     * @return
     */
    public static Object getCellContent(Sheet sheet, int c, int r) {
        Cell cell = getCellOfSheet(r, c, sheet);
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return "";

        }
    }

    /**
     * Method set sheet is selected when is opened
     *
     * @param posSheet
     */
    public void setSheetSelected(int posSheet) {
        try {
            workbook.setActiveSheet(posSheet);
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage(), ex);
            workbook.setActiveSheet(0);
        }
    }

    /**
     * method to merge cell
     *
     * @param sheet
     * @param firstRow based 0
     * @param lastRow based 0
     * @param firstCol based 0
     * @param lastCol based 0
     */
    public static void mergeCells(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(
                firstRow, //first row (0-based)
                lastRow, //last row  (0-based)
                firstCol, //first column (0-based)
                lastCol //last column  (0-based)
        ));
    }

    /**
     * method to fill color background for cell
     *
     * @param cell
     * @param colors:BLACK, WHITE, RED, BRIGHT_GREEN, BLUE, YELLOW, PINK,
     * TURQUOISE, DARK_RED, GREEN, DARK_BLUE, DARK_YELLOW, VIOLET, TEAL,
     * GREY_25_PERCENT, GREY_50_PERCENT, CORNFLOWER_BLUE, MAROON, LEMON_CHIFFON,
     * ORCHID, CORAL, ROYAL_BLUE, LIGHT_CORNFLOWER_BLUE, SKY_BLUE,
     * LIGHT_TURQUOISE, LIGHT_GREEN, LIGHT_YELLOW, PALE_BLUE, ROSE, LAVENDER,
     * TAN, LIGHT_BLUE, AQUA, LIME, GOLD, LIGHT_ORANGE, ORANGE, BLUE_GREY,
     * GREY_40_PERCENT, DARK_TEAL, SEA_GREEN, DARK_GREEN, OLIVE_GREEN, BROWN,
     * PLUM, INDIGO, GREY_80_PERCENT, AUTOMATIC;
     */
    public void fillAndColorCell(Cell cell, IndexedColors colors) {
        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(colors.getIndex());
        cell.setCellStyle(style);
    }
    // datpk  lay object tu Row

    public static Object getCellContentRow(int c, Row row) {
        Cell cell = getCellOfSheetRow(c, row);
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return "";

        }
    }

    /**
     * Method get text content Of cell
     *
     * @param sheet
     * @param c
     * @param r
     * @return
     */
    public static String getCellStrContent(Sheet sheet, int c, int r) {
        Cell cell = getCellOfSheet(r, c, sheet);
        if (cell == null) {
            return "";
        }
        String temp = getCellContent(sheet, c, r).toString().trim();
        if (temp.endsWith(".0")) {
            return temp.substring(0, temp.length() - 2);
        }
        return temp;
    }
    // datpk getStringconten tu Row

    public static String getCellStrContentRow(int c, Row row) {
        Cell cell = getCellOfSheetRow(c, row);
        if (cell == null) {
            return "";
        }
        String temp = getCellContentRow(c, row).toString().trim();
        if (temp.endsWith(".0")) {
            return temp.substring(0, temp.length() - 2);
        }
        return temp;
    }

    /**
     * method to create validation from array String.But String do not exceed
     * 255 characters
     *
     * @param arrValidate * ThuanNHT
     */
    public void createDropDownlistValidateFromArr(Sheet sheet, String[] arrValidate, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddressList addressList = new CellRangeAddressList(
                firstRow, lastRow, firstCol, lastCol);
        DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(arrValidate);
        HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
        dataValidation.setSuppressDropDownArrow(false);
        HSSFSheet sh = (HSSFSheet) sheet;
        sh.addValidationData(dataValidation);
    }

    /**
     * Method to create validation from spread sheet via range
     *
     * @param range
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol * ThuanNHT
     */
    public void createDropDownListValidateFromSpreadSheet(String range, int firstRow, int lastRow, int firstCol, int lastCol, Sheet shet) {
        Name namedRange = workbook.createName();
        Random rd = new Random();
        String refName = ("List" + rd.nextInt()).toString().replace("-", "");
        namedRange.setNameName(refName);
//        namedRange.setRefersToFormula("'Sheet1'!$A$1:$A$3");
        namedRange.setRefersToFormula(range);
        DVConstraint dvConstraint = DVConstraint.createFormulaListConstraint(refName);
        CellRangeAddressList addressList = new CellRangeAddressList(
                firstRow, lastRow, firstCol, lastCol);
        HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
        dataValidation.setSuppressDropDownArrow(false);
        HSSFSheet sh = (HSSFSheet) shet;
        sh.addValidationData(dataValidation);
    }

    public void createDropDownListValidateFromSpreadSheet(String sheetName, String columnRangeName,
                                                          int rowRangeStart, int rowRangeEnd, int firstRow, int lastRow, int firstCol, int lastCol, Sheet shet) {
        String range = "'" + sheetName + "'!$" + columnRangeName + "$" + rowRangeStart + ":" + "$" + columnRangeName + "$" + rowRangeEnd;
        Name namedRange = workbook.createName();
        Random rd = new Random();
        String refName = ("List" + rd.nextInt()).toString().replace("-", "");
        namedRange.setNameName(refName);
//        namedRange.setRefersToFormula("'Sheet1'!$A$1:$A$3");
        namedRange.setRefersToFormula(range);
        DVConstraint dvConstraint = DVConstraint.createFormulaListConstraint(refName);
        CellRangeAddressList addressList = new CellRangeAddressList(
                firstRow, lastRow, firstCol, lastCol);
        HSSFDataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
        dataValidation.setSuppressDropDownArrow(false);
        HSSFSheet sh = (HSSFSheet) shet;
        sh.addValidationData(dataValidation);
    }

    public Sheet getSheetAt(int pos) {
        return workbook.getSheetAt(pos);
    }

    public Sheet getSheet(String name) {
        return workbook.getSheet(name);
    }

    /**
     * Method to read an excel file
     *
     * @param filePathName
     * @return * ThuanNHT
     */
    public Workbook readFileExcel(String filePathName) {
        InputStream inp = null;
        try {
            // R3292_EDIT_DUNGNV50_13122012_START
            inp = new FileInputStream(filePathName);
            // R3292_EDIT_DUNGNV50_13122012_END
            workbook = WorkbookFactory.create(inp);
        } catch (FileNotFoundException ex) {
            logger.error(ex);
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                if(inp != null) {
                    inp.close();
                }
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
        return workbook;
    }

    /**
     *  * ThuanNHT
     *
     * @param r
     * @param c
     * @param sheet
     * @return
     */
    public static Cell getCellOfSheet(int r, int c, Sheet sheet) {
        try {
            Row row = sheet.getRow(r);
            if (row == null) {
                return null;
            }
            return row.getCell(c);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * set style for cell
     *
     * @param cell
     * @param halign
     * @param valign
     * @param border
     * @param borderColor
     */
    public void setCellStyle(Cell cell, short halign, short valign, short border, short borderColor, int fontHeight) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) fontHeight);
        style.setAlignment(halign);
        style.setVerticalAlignment(valign);
        style.setBorderBottom(border);
        style.setBottomBorderColor(borderColor);
        style.setBorderLeft(border);
        style.setLeftBorderColor(borderColor);
        style.setBorderRight(border);
        style.setRightBorderColor(borderColor);
        style.setBorderTop(border);
        style.setTopBorderColor(borderColor);
        cell.setCellStyle(style);
    }

    /**
     * Method to draw an image on excel file
     *
     * @param imgSrc
     * @param sheet
     * @param colCorner
     * @param rowCorner
     * @throws IOException
     */
    public void drawImageOnSheet(String imgSrc, Sheet sheet, int colCorner, int rowCorner) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(imgSrc);
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            if (imgSrc.endsWith(".jpg") || imgSrc.endsWith(".JPG") || imgSrc.endsWith(".jpeg") || imgSrc.endsWith(".JPEG")) {
                pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            } else if (imgSrc.endsWith(".png") || imgSrc.endsWith(".PNG")) {
                pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            }

            CreationHelper helper = workbook.getCreationHelper();
            // Create the drawing patriarch.  This is the top level container for all shapes.
            Drawing drawing = sheet.createDrawingPatriarch();
            //add a picture shape
            ClientAnchor anchor = helper.createClientAnchor();
            //set top-left corner of the picture,
            //subsequent call of Picture#resize() will operate relative to it
            anchor.setCol1(colCorner);
            anchor.setRow1(rowCorner);
            Picture pict = drawing.createPicture(anchor, pictureIdx);

            //auto-size picture relative to its top-left corner
            pict.resize();
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    public void setStandardCellStyle(Cell cell) {
        setCellStyle(cell, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, CellStyle.BORDER_THIN, IndexedColors.BLACK.getIndex(), 12);
    }

    // datpk: lay cell tu Row
    public static Cell getCellOfSheetRow(int c, Row row) {
        try {
            if (row == null) {
                return null;
            }
            return row.getCell(c);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static Boolean compareToLong(String str, Long t) {
        Boolean check = false;
        try {
            Double d = Double.valueOf(str);
            Long l = d.longValue();
            if (l.equals(t)) {
                check = true;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            check = false;
        }
        return check;
    }

    public static Boolean doubleIsLong(String str) {
        Boolean check = false;
        try {
            Double d = Double.valueOf(str);
            Long l = d.longValue();
            if (d.equals(Double.valueOf(l))) {
                check = true;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            check = false;
        }
        return check;
    }

    public static void main(String[] arg) {
        try {
//            Runtime r = Runtime.getRuntime();
//            long freeMem = r.freeMemory();
//
            ExcelWriterUtils ewu = new ExcelWriterUtils();
//            ewu.readFileExcel("C:\\DIEMLOM1111111111111111111.xls");
//            Sheet shet = ewu.getSheetAt(0);
//            String a = ExcelWriterUtils.getCellStrContent(shet, 8, 17);
//            ewu.createSheet("Toi la ai3");
//            for (int i = 0; i < 60000; i++) {
//                for (int j = 0; j < 4; j++) {
//                   String str = ExcelWriterUtils.getCellStrContent(ewu.getSheetAt(0), 2, 4);
//                   Double db = Double.parseDouble(str);
//                    ExcelWriterUtils.getCellStrContent(ewu.getSheetAt(0), 3, 4);
//                }
//
//            }
//            ewu.setSheetSelected(5);
//            mergeCells(shet, 1, 5, 1, 5);
//            ewu.setStandardCellStyle(getCellOfSheet(2, 2, shet));
//            ewu.setCellStyleBottomBorder(getCellOfSheet(2, 2, shet), CellStyle.BORDER_DOUBLE, IndexedColors.RED.getIndex());
//            ewu.setCellStyleBottomBorder(getCellOfSheet(2, 2, shet), CellStyle.BORDER_DOUBLE, IndexedColors.GREEN.getIndex());
//            String[] arr = new String[lst.size()];
//            arr = lst.toArray(arr);
//            ewu.createDropDownlistValidation(arr);
//            ewu.readFileExcel("C:\\a.xls");
//            ewu.setSheet(ewu.getWorkbook().getSheetAt(0));
//            ewu.createDropDownListValidateFromSpreadSheet("'Sheet2'!$B$4:$B$11", 8, 12, 4, 5, ewu.getWorkbook().getSheetAt(0));
//            Cell cel = getCellOfSheet(17, 5, sh1);
//            ewu.saveToFileExcel("C:\\a.xlsx");

//            freeMem = r.freeMemory();
//            r.gc();
//            freeMem = r.freeMemory();
//
//            ExcelWriterUtils ewu2 = new ExcelWriterUtils();
//            ewu2.readFileExcel("C:\\aaa.xls");
////            Sheet shet2 = ewu2.createSheet("Toi la ai3");
//            for (int i = 0; i < 60000; i++) {
//                for (int j = 0; j < 4; j++) {
//                    ewu2.createCell(ewu2.getSheetAt(1), j, i, "Hang thu " + i);
//                }
//
//            }
            ewu.readFileExcel("C:\\a.xlsx");
//            Sheet she = ewu.getSheetAt(0);
            ewu.readFileExcel("C:\\b.xlsx");
//            Sheet she1 = ewu.getSheetAt(0);

//            for (int i = 40000; i < 60000; i++) {
//                for (int j = 0; j < 10; j++) {
//                    ewu.createCell(ewu.getSheetAt(0), j, i, "Hang thu " + i);
//                }
//
//            }
//
////            String[] arr = new String[lst.size()];
////            arr = lst.toArray(arr);
////            ewu.createDropDownlistValidation(arr);
////            ewu.readFileExcel("C:\\a.xls");
////            ewu.setSheet(ewu.getWorkbook().getSheetAt(0));
////            ewu.createDropDownListValidateFromSpreadSheet("'Sheet2'!$B$4:$B$11", 8, 12, 4, 5, ewu.getWorkbook().getSheetAt(0));
////            Cell cel = getCellOfSheet(17, 5, sh1);
//            ewu2.saveToFileExcel("C:\\aaa.xls");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public boolean validateSheet(Row row, int endCol) {
        if (row == null) {
            return false;
        }
        Cell cell;
        for (int i = 0; i <= endCol; i++) {
            cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
            if (cell == null || cell.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkRowNotNull(Row row, int endCol) {
        if (row == null) {
            return false;
        }
        Cell cell;
        for (int i = 1; i <= endCol; i++) {
            cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
            if (cell != null && !cell.toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public XSSFRow getOrCreateRow(XSSFSheet sheet, int rowIndex) {
        if (sheet.getRow(rowIndex) == null) {
            return sheet.createRow(rowIndex);
        }
        return sheet.getRow(rowIndex);
    }

    //R3560_vannh4_fix_10042013_start
    //mo rong ham doc file excel
    public Workbook readFileExcel(InputStream inputStream) {
        try {
            workbook = WorkbookFactory.create(inputStream);
            workbook.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
        } catch (FileNotFoundException ex) {
            logger.error(ex);
        } catch (Exception ex) {
            logger.error(ex);
        }

        return workbook;
    }

    public Map<String, CellStyle> getStyleMap(Workbook wor){
        Map<String, CellStyle> cellStyleMap = new HashMap<String, CellStyle>();
        try {
            Font fTitle = wor.createFont();
            fTitle.setFontName("Arial");
            fTitle.setCharSet(Font.DEFAULT_CHARSET);
            fTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
            fTitle.setFontHeightInPoints((short)14);

            Font fHeader = wor.createFont();
            fHeader.setFontName("Arial");
            fHeader.setCharSet(Font.DEFAULT_CHARSET);
            fHeader.setBoldweight(Font.BOLDWEIGHT_BOLD);
            fHeader.setFontHeightInPoints((short)11);

            Font fNormal = wor.createFont();
            fNormal.setFontName("Arial");
            fNormal.setCharSet(Font.DEFAULT_CHARSET);
            fNormal.setFontHeightInPoints((short)11);

            Font fNormalRed = wor.createFont();
            fNormalRed.setFontName("Arial");
            fNormalRed.setCharSet(Font.DEFAULT_CHARSET);
            fNormalRed.setFontHeightInPoints((short)11);
            fNormalRed.setColor(HSSFColor.RED.index);

            //title
            CellStyle title = wor.createCellStyle();
            title.setAlignment(CellStyle.ALIGN_CENTER);
            title.setFont(fTitle);
            title.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellStyleMap.put("title", title);

            //header
            CellStyle header = wor.createCellStyle();
            header.setAlignment(CellStyle.ALIGN_CENTER);
            header.setFont(fHeader);
            header.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            header.setBorderBottom(CellStyle.BORDER_THIN);
            header.setBorderLeft(CellStyle.BORDER_THIN);
            header.setBorderRight(CellStyle.BORDER_THIN);
            header.setBorderTop(CellStyle.BORDER_THIN);
            cellStyleMap.put("header", header);

            //normal
            CellStyle normal = wor.createCellStyle();
            normal.setAlignment(CellStyle.ALIGN_LEFT);
            normal.setFont(fNormal);
            normal.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            normal.setBorderBottom(CellStyle.BORDER_THIN);
            normal.setBorderLeft(CellStyle.BORDER_THIN);
            normal.setBorderRight(CellStyle.BORDER_THIN);
            normal.setBorderTop(CellStyle.BORDER_THIN);
            cellStyleMap.put("normal", normal);

            //normal
            CellStyle normalNoBorder = wor.createCellStyle();
            normalNoBorder.setAlignment(CellStyle.ALIGN_LEFT);
            normalNoBorder.setFont(fNormal);
            normal.setBorderBottom(CellStyle.BORDER_THIN);
            normalNoBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellStyleMap.put("normalNoBorder", normalNoBorder);

            //normal_Red
            CellStyle normalRed = wor.createCellStyle();
            normalRed.setAlignment(CellStyle.ALIGN_LEFT);
            normalRed.setFont(fNormalRed);
            normalRed.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            normalRed.setBorderBottom(CellStyle.BORDER_THIN);
            normalRed.setBorderLeft(CellStyle.BORDER_THIN);
            normalRed.setBorderRight(CellStyle.BORDER_THIN);
            normalRed.setBorderTop(CellStyle.BORDER_THIN);
            cellStyleMap.put("normalRed", normalRed);

            //order
            CellStyle order = wor.createCellStyle();
            order.setAlignment(CellStyle.ALIGN_CENTER);
            order.setFont(fNormal);
            order.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            order.setBorderBottom(CellStyle.BORDER_THIN);
            order.setBorderLeft(CellStyle.BORDER_THIN);
            order.setBorderRight(CellStyle.BORDER_THIN);
            order.setBorderTop(CellStyle.BORDER_THIN);
            cellStyleMap.put("order", order);


        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return cellStyleMap;
    }

    public CellStyle getCellStyleByCell(int rowNum, int colNum, Sheet sheet, CellStyle styleDefault) {
        CellStyle style = styleDefault;

        try {
            Row row = sheet.getRow(rowNum);
            if (null != row) {
                Cell cell = row.getCell(colNum);
                if (null != cell) {
                    style.cloneStyleFrom(cell.getCellStyle());
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return style;
    }

    public Cell createCell(Row row, int column, String cellValue, CellStyle style) {
        // Create a cell and put a value in it.
        Cell cell = row.createCell(column);
        cell.setCellValue(cellValue);
        cell.setCellStyle(style);
        return cell;
    }
    //R3560_vannh4_fix_10042013_end


    public static List readExcel(File file, int iSheet, int iBeginRow, int iFromCol, int iToCol, int rowBack) throws FileNotFoundException, IOException {
        List lst = new ArrayList();
        FileInputStream flieInput = new FileInputStream(file);
        SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");

        HSSFWorkbook workbook;
        try {
            workbook = new HSSFWorkbook(flieInput);
            HSSFSheet worksheet = workbook.getSheetAt(iSheet);
            int irowBack = 0;
            for (int i = iBeginRow; i <= worksheet.getLastRowNum(); i++) {
                Object[] obj = new Object[iToCol - iFromCol + 1];
                HSSFRow row = worksheet.getRow(i);

                if (row != null && true) {
                    int iCount = 0;
                    int check = 0;
                    for (int j = iFromCol; j <= iToCol; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null && true) {
                            switch (cell.getCellType()) {
//                                case Cell.CELL_TYPE_STRING:
//                                    obj[iCount] = cell.getStringCellValue().trim();
//                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    Double doubleValue = (Double) cell.getNumericCellValue();
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        Date date = HSSFDateUtil.getJavaDate(doubleValue);
                                        String dateFmt = cell.getCellStyle().getDataFormatString();
                                        obj[iCount] = sp.format(date);
                                        break;
                                    }
                                    List<String> lstValue = DataUtil.splitDot(String.valueOf(doubleValue));
                                    if (lstValue.get(1).matches("[0]+")) {
                                        obj[iCount] = lstValue.get(0);
                                    } else {
                                        obj[iCount] = String.format("%.2f", doubleValue).trim();
                                    }

                                    break;
                                case Cell.CELL_TYPE_BLANK:
                                    check++;
                                    break;
                                default:
                                    System.out.println("");
                                    obj[iCount] = cell.getStringCellValue().trim();
                                    break;
                            }
                        } else {
                            obj[iCount] = null;
                        }
                        iCount += 1;
                    }
                    if (check != (iToCol - iFromCol + 1)) {
                        lst.add(obj);
                    }

                } else {
                    irowBack += 1;
                }
                if (irowBack == rowBack) {
                    break;
                }
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            lst = null;
        }finally {
            flieInput.close();
        }
        return lst;
    }

//    public static boolean compareHeader(String source, String dest) {
//        List<CellObject> titleSource = new ArrayList<CellObject>();
//        InputStream inputStreamsDest = null;
//        try {
//            InputStream inputStreamsSource = new FileInputStream(source);
//            inputStreamsDest = new FileInputStream(dest);
//            if (source.endsWith(".xls") || source.endsWith(".XLS")) {
//                HSSFSheet sheet = new HSSFWorkbook(inputStreamsSource).getSheetAt(0);
//
//                int r = 0;
//                while (sheet != null && r <= sheet.getLastRowNum()) {
//                    Row row = sheet.getRow(r);
//                    int indexHeader = 0;
//                    while (row != null && indexHeader <= row.getLastCellNum()) {
//                        if (row.getCell(indexHeader) == null) {
//                            indexHeader++;
//                            continue;
//                        }
//                        CellObject obj = new CellObject();
//                        if (row.getCell(indexHeader).getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            obj.setRow(r);
//                            obj.setCell(indexHeader);
//                            obj.setValue(row.getCell(indexHeader).getNumericCellValue() + "");
//                            titleSource.add(obj);
//                        }
//                        if (row.getCell(indexHeader).getCellType() == Cell.CELL_TYPE_STRING) {
//                            obj.setRow(r);
//                            obj.setCell(indexHeader);
//                            obj.setValue(row.getCell(indexHeader).getStringCellValue());
//                            titleSource.add(obj);
//                        }
//                        indexHeader++;
//                    }
//                    r++;
//                }
//            } else if (source.endsWith(".xlsx") || source.endsWith(".XLSX")) {
//                XSSFSheet sheet = new XSSFWorkbook(inputStreamsSource).getSheetAt(0);
//                int r = 0;
//                while (sheet != null && r <= sheet.getLastRowNum()) {
//                    Row row = sheet.getRow(r);
//                    int indexHeader = 0;
//                    while (row != null && indexHeader <= row.getLastCellNum()) {
//                        if (row.getCell(indexHeader) == null) {
//                            indexHeader++;
//                            continue;
//                        }
//                        CellObject obj = new CellObject();
//                        if (row.getCell(indexHeader).getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            obj.setRow(r);
//                            obj.setCell(indexHeader);
//                            obj.setValue(row.getCell(indexHeader).getNumericCellValue() + "");
//                            titleSource.add(obj);
//                        }
//                        if (row.getCell(indexHeader).getCellType() == Cell.CELL_TYPE_STRING) {
//                            obj.setRow(r);
//                            obj.setCell(indexHeader);
//                            obj.setValue(row.getCell(indexHeader).getStringCellValue());
//                            titleSource.add(obj);
//                        }
//                        indexHeader++;
//                    }
//                    r++;
//                }
//            }
//            if (dest.endsWith(".xls") || dest.endsWith(".XLS")) {
//                HSSFSheet sheet = new HSSFWorkbook(inputStreamsDest).getSheetAt(0);
//                for (int i = 0; i < titleSource.size(); i++) {
//                    Row row = sheet.getRow(titleSource.get(i).getRow());
//                    if (row == null) {
//                        return false;
//                    } else {
//                        String sourceValue = titleSource.get(i).getValue();
//                        if (row.getCell(titleSource.get(i).getCell()) == null) {
//                            return false;
//                        }
//                        if (row.getCell(titleSource.get(i).getCell()).getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            if (!sourceValue.equalsIgnoreCase(row.getCell(titleSource.get(i).getCell()).getNumericCellValue() + "")) {
//                                return false;
//                            }
//                        }
//                        if (row.getCell(titleSource.get(i).getCell()).getCellType() == Cell.CELL_TYPE_STRING) {
//                            if (!sourceValue.equalsIgnoreCase(row.getCell(titleSource.get(i).getCell()).getStringCellValue())) {
//                                return false;
//                            }
//                        }
//                    }
//                }
//                return true;
//
//            } else if (dest.endsWith(".xlsx") || dest.endsWith(".XLSX")) {
//                XSSFSheet sheet = new XSSFWorkbook(inputStreamsDest).getSheetAt(0);
//                for (int i = 0; i < titleSource.size(); i++) {
//                    Row row = sheet.getRow(titleSource.get(i).getRow());
//                    if (row == null) {
//                        return false;
//                    } else {
//                        String sourceValue = titleSource.get(i).getValue();
//                        if (row.getCell(titleSource.get(i).getCell()) == null) {
//                            return false;
//                        }
//                        if (row.getCell(titleSource.get(i).getCell()).getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            if (!sourceValue.equalsIgnoreCase(row.getCell(titleSource.get(i).getCell()).getNumericCellValue() + "")) {
//                                return false;
//                            }
//                        }
//                        if (row.getCell(titleSource.get(i).getCell()).getCellType() == Cell.CELL_TYPE_STRING) {
//                            if (!sourceValue.equalsIgnoreCase(row.getCell(titleSource.get(i).getCell()).getStringCellValue())) {
//                                return false;
//                            }
//                        }
//                    }
//                }
//                return true;
//            }
//            inputStreamsSource.close();
//            return false;
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//            return false;
//        } finally {
//            inputStreamsDest.close();
//        }
//    }

//    public static void concatFile(String source, String dest, int rs, int cs, int rd, int cd) {
//        try {
//            InputStream inputStreamsSource = new FileInputStream(source);
//            InputStream inputStreamsDest = new FileInputStream(dest);
//            HSSFWorkbook wbHFF = null;
//            XSSFWorkbook wbXFF = null;
//            HSSFSheet sheetDestHFF;
//            Row rowDest = null;
//            int rdCount = rd;
//            if (source.endsWith(".xls") || source.endsWith(".XLS")) {
//                HSSFSheet sheet = new HSSFWorkbook(inputStreamsSource).getSheetAt(0);
//                for (int i = rs; sheet.getRow(i) != null; i++) {
//                    Row row = sheet.getRow(i);
//                    int cdCount = cd;
//                    if (dest.endsWith(".xls") || dest.endsWith(".XLS")) {
//                        if (wbHFF == null) {
//                            wbHFF = new HSSFWorkbook(inputStreamsDest);
//                        }
//                        sheetDestHFF = wbHFF.getSheetAt(0);
//                        rowDest = sheetDestHFF.createRow(rdCount);
//                    }
//                    if (dest.endsWith(".xlsx") || dest.endsWith(".XLSX")) {
//                        if (wbXFF == null) {
//                            wbXFF = new XSSFWorkbook(inputStreamsDest);
//                        }
//                        sheetDestHFF = wbHFF.getSheetAt(0);
//                        rowDest = sheetDestHFF.createRow(rdCount);
//                    }
//                    for (int k = cs; row.getCell(k) != null; k++) {
//                        if (rowDest != null) {
//                            Cell cell = rowDest.createCell(cdCount);
//                            if (row.getCell(k).getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                                cell.setCellValue(row.getCell(k).getNumericCellValue());
//                            }
//                            if (row.getCell(k).getCellType() == Cell.CELL_TYPE_STRING) {
//                                cell.setCellValue(row.getCell(k).getStringCellValue());
//                            }
//                        }
//                        cdCount++;
//                    }
//                    rdCount++;
//                }
//            }
//
//            if (source.endsWith(".xlsx") || source.endsWith(".XLSX")) {
//                XSSFSheet sheet = new XSSFWorkbook(inputStreamsSource).getSheetAt(0);
//                for (int i = rs; sheet.getRow(i) != null; i++) {
//                    Row row = sheet.getRow(i);
//                    int cdCount = cd;
//                    for (int k = cs; row.getCell(k) != null; k++) {
//                        if (rowDest != null) {
//                            Cell cell = rowDest.createCell(cdCount);
//                            if (row.getCell(k).getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                                cell.setCellValue(row.getCell(k).getNumericCellValue());
//                            }
//                            if (row.getCell(k).getCellType() == Cell.CELL_TYPE_STRING) {
//                                cell.setCellValue(row.getCell(k).getStringCellValue());
//                            }
//                        }
//                    }
//                    cdCount++;
//                }
//                rdCount++;
//            }
//            inputStreamsDest.close();
//            inputStreamsSource.close();
//            FileOutputStream outFile = new FileOutputStream(dest);
//            if (dest.endsWith(".xlsx") || dest.endsWith(".XLSX")) {
//                wbXFF.write(outFile);
////                wbXFF.close();
//            }
//            if (dest.endsWith(".xls") || dest.endsWith(".XLS")) {
//                wbHFF.write(outFile);
//            }
//            outFile.close();
//
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//        }
//    }

    //R585445 sonvt19 Cau hinh file dinh kem khi tao Wo (thêm validate file excel trống) - start
    public static Boolean isEmptyExcelFile (String pathFileImport, int iSheet, String fileUpload) throws IOException {
        File file = new File(pathFileImport);
        FileInputStream flieInput = new FileInputStream(file);

        try {
            if (fileUpload.endsWith(".xls") || fileUpload.endsWith(".XLS")) {
                HSSFWorkbook workbook = new HSSFWorkbook(flieInput);
                HSSFSheet worksheet = workbook.getSheetAt(iSheet);
                if (worksheet.getPhysicalNumberOfRows() == 0){
                    return true;
                } else {
                    return false;
                }
            } else if (fileUpload.endsWith(".xlsx") || fileUpload.endsWith(".XLSX")) {
                XSSFWorkbook workbook = new XSSFWorkbook(flieInput);
                XSSFSheet worksheet = workbook.getSheetAt(iSheet);
                if (worksheet.getPhysicalNumberOfRows() == 0){
                    return true;
                } else {
                    return false;
                }
            }

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return true;
        }finally {
            flieInput.close();
        }
        return false;
    }

    //R585445 sonvt19 Cau hinh file dinh kem khi tao Wo (thêm validate file excel trống) - end

    //sonvt19 Giao tiếp với NIMS - cấp phát IP Port - start
    public static int findHeadeRow (String pathFileImport, int iSheet, String fileUpload,int iRowBack) throws IOException {
        File file = new File(pathFileImport );
        FileInputStream flieInput = new FileInputStream(file);
        int rowHeader = -1;
        Boolean headerFound = false;
        try {
            if (fileUpload.endsWith(".xls") || fileUpload.endsWith(".XLS")) {
                HSSFWorkbook workbook = new HSSFWorkbook(flieInput);
                HSSFSheet worksheet = workbook.getSheetAt(iSheet);
                Iterator<Row> rows = worksheet.iterator();
                for(int i = 0; i < iRowBack; i++ ){
//                while (rows.hasNext()){
//                    Row row = rows.next();
                    rowHeader = i;
                    if(!isCellEmpty(getCellOfSheet(i,0,worksheet)) && !isCellEmpty(getCellOfSheet(i,1,worksheet))){
                        headerFound = true;
                        break;
                    }

                }
            } else if (fileUpload.endsWith(".xlsx") || fileUpload.endsWith(".XLSX")) {
                XSSFWorkbook workbook = new XSSFWorkbook(flieInput);
                XSSFSheet worksheet = workbook.getSheetAt(iSheet);
                Iterator<Row> rows = worksheet.iterator();
                while (rows.hasNext()){
                    Row row = rows.next();
                    if(!isCellEmpty(row.getCell(0)) && !isCellEmpty(row.getCell(1))){
                        headerFound = true;
                        break;
                    }
                    rowHeader++;
                }
            }

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return -1;
        }finally {
            flieInput.close();
        }
        if(headerFound){
            return rowHeader;
        }
        return -1;
    }

    public static int findLastColumn(String pathFileImport, int iSheet, String fileUpload, int rowHeader) throws IOException {
        File file = new File(pathFileImport );
        FileInputStream flieInput = new FileInputStream(file);
        try {
            if (fileUpload.endsWith(".xls") || fileUpload.endsWith(".XLS")) {
                HSSFWorkbook workbook = new HSSFWorkbook(flieInput);
                HSSFSheet worksheet = workbook.getSheetAt(iSheet);
                return worksheet.getRow(rowHeader).getLastCellNum() - 1;
            } else if (fileUpload.endsWith(".xlsx") || fileUpload.endsWith(".XLSX")) {
                XSSFWorkbook workbook = new XSSFWorkbook(flieInput);
                XSSFSheet worksheet = workbook.getSheetAt(iSheet);
                return worksheet.getRow(rowHeader).getLastCellNum() - 1;
            }

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return -1;
        }finally {
            flieInput.close();
        }
        return -1;
    }

    public static Boolean isCellEmpty(Cell cell){
        if(cell == null){
            return true;
        }
        if(cell.getCellType()== Cell.CELL_TYPE_BLANK){
            return true;
        }
        if("".equals(cell.getStringCellValue())){
            return true;
        }
        return false;
    }

    public static Boolean isCorrectFile ( String fileUpload) throws FileNotFoundException {
        if (fileUpload.endsWith(".xls") || fileUpload.endsWith(".XLS")) {
            return true;
        }
//        else if (fileUpload.endsWith(".xlsx") || fileUpload.endsWith(".XLSX")) {
//            return true;
//        }

        return false;
    }

    public static List readExcelAddBlankRow(File file, int iSheet, int iBeginRow, int iFromCol, int iToCol, int rowBack) throws FileNotFoundException, IOException {
        List lst = new ArrayList();
        FileInputStream flieInput = new FileInputStream(file);
        SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");

        HSSFWorkbook workbook;
        try {
            workbook = new HSSFWorkbook(flieInput);
            HSSFSheet worksheet = workbook.getSheetAt(iSheet);
            int irowBack = 0;
            for (int i = iBeginRow; i <= worksheet.getLastRowNum(); i++) {
                Object[] obj = new Object[iToCol - iFromCol + 1];
                HSSFRow row = worksheet.getRow(i);

                if (row != null && true) {
                    int iCount = 0;
                    int check = 0;
                    for (int j = iFromCol; j <= iToCol; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null && true) {
                            switch (cell.getCellType()) {
//                                case Cell.CELL_TYPE_STRING:
//                                    obj[iCount] = cell.getStringCellValue().trim();
//                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    Double doubleValue = (Double) cell.getNumericCellValue();
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        Date date = HSSFDateUtil.getJavaDate(doubleValue);
                                        String dateFmt = cell.getCellStyle().getDataFormatString();
                                        obj[iCount] = sp.format(date);
                                        break;
                                    }
                                    List<String> lstValue = DataUtil.splitDot(String.valueOf(doubleValue));
                                    if (lstValue.get(1).matches("[0]+")) {
                                        obj[iCount] = lstValue.get(0);
                                    } else {
                                        obj[iCount] = String.format("%.2f", doubleValue).trim();
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
                        lst.add(obj);
                    } else{
                        obj[0] = "blankRow";
                        lst.add(obj);
                    }

                } else {
                    irowBack += 1;
                    obj[0] = "blankRow";
                    lst.add(obj);
                }
                if (irowBack == rowBack) {
                    break;
                }
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            lst = null;
        }finally {
            flieInput.close();
        }
        return lst;
    }
    //sonvt19 Giao tiếp với NIMS - cấp phát IP Port - end

    //sonvt19 - 751708 - bo sung chuc nang import/export cac danh muc cau hinh cua gnoc - start
    public static List readExcelAddBlankRowXSSF(File file, int iSheet, int iBeginRow, int iFromCol, int iToCol, int rowBack) throws FileNotFoundException, IOException {
        List lst = new ArrayList();
        FileInputStream flieInput = new FileInputStream(file);
        SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");

        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(flieInput);
            XSSFSheet worksheet = workbook.getSheetAt(iSheet);
            int irowBack = 0;
            for (int i = iBeginRow; i <= worksheet.getLastRowNum(); i++) {
                Object[] obj = new Object[iToCol - iFromCol + 1];
                Row row = worksheet.getRow(i);

                if (row != null && true) {
                    int iCount = 0;
                    int check = 0;
                    for (int j = iFromCol; j <= iToCol; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null && true) {
                            switch (cell.getCellType()) {
//                                case Cell.CELL_TYPE_STRING:
//                                    obj[iCount] = cell.getStringCellValue().trim();
//                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    Double doubleValue = (Double) cell.getNumericCellValue();
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        Date date = HSSFDateUtil.getJavaDate(doubleValue);
                                        String dateFmt = cell.getCellStyle().getDataFormatString();
                                        obj[iCount] = sp.format(date);
                                        break;
                                    }
                                    List<String> lstValue = DataUtil.splitDot(String.valueOf(doubleValue));
                                    if (lstValue.get(1).matches("[0]+")) {
                                        obj[iCount] = lstValue.get(0);
                                    } else {
                                        obj[iCount] = String.format("%.2f", doubleValue).trim();
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
                        lst.add(obj);
                    }

                } else {
                    irowBack += 1;

                }
                if (irowBack == rowBack) {
                    break;
                }
            }
        } catch (IOException ex) {
            lst = null;
            logger.error(ex.getMessage(), ex);
        }finally {
            flieInput.close();
        }
        return lst;
    }

    public boolean validateFileData(String fileTemplatePath, String fileImportPath, int sheetIndex, int rowHeader) throws  Exception {
        Workbook workBook = readFileExcel(fileTemplatePath);
        Sheet sheetTemplate = workBook.getSheetAt(sheetIndex);

        Workbook workBook2 = readFileExcel(fileImportPath);
        Sheet sheetFileImport = workBook2.getSheetAt(sheetIndex);

        Row rowFileImport = sheetFileImport.getRow(rowHeader);
        Row rowTemplate = sheetTemplate.getRow(rowHeader);
        String arrHeaderTemplate = "";
        String arrHeaderFileImport = "";
        int k = rowTemplate.getLastCellNum();
        for (int i = 0; i < k; i++) {
            rowTemplate.getCell(i).setCellType(CellType.STRING);
            if (rowTemplate.getCell(i).getStringCellValue() != null) {
                arrHeaderTemplate += "," + rowTemplate.getCell(i).getStringCellValue();
            } else {
                break;
            }
        }
        int l = rowFileImport.getLastCellNum();
        for (int j = 0; j < l; j++) {
            rowFileImport.getCell(j).setCellType(CellType.STRING);
            if (rowFileImport.getCell(j).getStringCellValue() != null) {
                arrHeaderFileImport += "," + rowFileImport.getCell(j).getStringCellValue();
            } else {
                break;
            }
        }
        return arrHeaderTemplate.equals(arrHeaderFileImport);
    }


}
