package com.company.framework.helpers;

import com.company.framework.utils.LogUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ExcelHelpers {

    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook workbook;
    private Sheet sheet;
    private Cell cell;
    private Row row;
    private String excelFilePath;
    private Map<String, Integer> columns = new HashMap<>();

    public ExcelHelpers() {
    }

    //Set Excel File
    public void setExcelFile(String excelPath, String sheetName) {
        LogUtils.info("[ExcelHelpers] Set Excel File: " + excelPath);
        LogUtils.info("[ExcelHelpers] Sheet Name: " + sheetName);

        try {
            File f = new File(excelPath);

            if (!f.exists()) {
                LogUtils.error("[ExcelHelpers] File Excel path not found: " + excelPath);
                throw new FileNotFoundException("File Excel path not found: " + excelPath);
            }
            if (sheetName.isEmpty()) {
                LogUtils.error("[ExcelHelpers] The Sheet Name is empty.");
                throw new IllegalArgumentException("The Sheet Name is empty.");
            }

            fis = new FileInputStream(excelPath);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                LogUtils.error("[ExcelHelpers] Sheet name not found: " + sheetName);
                throw new RuntimeException("Sheet name not found: " + sheetName);
            }

            excelFilePath = excelPath;

            //adding all the column header names to the map 'columns'
            sheet.getRow(0).forEach(cell -> {
                columns.put(cell.getStringCellValue(), cell.getColumnIndex());
            });

        } catch (Exception e) {
            e.getMessage();
            LogUtils.error("[ExcelHelpers] " + e.getMessage());
        }
    }

    //This method takes the row number as a parameter and returns the data for that row.
    public Row getRowData(int rowNum) {
        row = sheet.getRow(rowNum);
        return row;
    }

    //Get Excel data from the sheet
    public Object[][] getExcelData(String excelPath, String sheetName) {
        Object[][] data = null;
        Workbook workbook = null;
        FileInputStream fis = null;

        LogUtils.info("[ExcelHelpers] Set Excel file: " + excelPath);
        LogUtils.info("[ExcelHelpers] Selected Sheet: " + sheetName);

        try {
            File f = new File(excelPath);

            if (!f.exists()) {
                throw new FileNotFoundException("File Excel path not found: " + excelPath);
            }
            if (sheetName.isEmpty()) {
                throw new IllegalArgumentException("The Sheet Name is empty.");
            }

            // load the file
            fis = new FileInputStream(excelPath);

            // load the workbook
            workbook = new XSSFWorkbook(fis);
            // load the sheet
            Sheet sheet = workbook.getSheet(sheetName);
            // load the row
            Row row = sheet.getRow(0);

            int noOfRows = sheet.getPhysicalNumberOfRows();
            int noOfCols = row.getLastCellNum();

            LogUtils.info("[ExcelHelpers] Row: " + (noOfRows - 1) + " - Column: " + noOfCols);

            Cell cell;
            data = new Object[noOfRows - 1][noOfCols];

            //FOR loop runs from 1 to drop header line (headline is 0)
            for (int i = 1; i < noOfRows; i++) {
                for (int j = 0; j < noOfCols; j++) {
                    row = sheet.getRow(i);
                    cell = row.getCell(j);

                    //This is used to determine the data type from cells in Excel and then convert it to String for ease of reading
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                data[i - 1][j] = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                data[i - 1][j] = String.valueOf(cell.getNumericCellValue());
                                break;
                            case BLANK:
                                data[i - 1][j] = "";
                                break;
                            case BOOLEAN:
                                data[i - 1][j] = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                data[i - 1][j] = cell.getCellFormula();
                                break;
                            case ERROR:
                                data[i - 1][j] = String.valueOf(cell.getErrorCellValue());
                                break;
                            case _NONE:
                            default:
                                data[i - 1][j] = null;
                                break;
                        }
                    } else {
                        data[i - 1][j] = "";
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.error("[ExcelHelpers] Error reading Excel file: " + e.getMessage());
            throw new RuntimeException("Error reading Excel file: " + excelPath, e);
        } finally {
            // Close resources
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                LogUtils.error("[ExcelHelpers] Error closing resources: " + e.getMessage());
            }
        }
        return data;
    }

    public Object[][] getDataHashTable(String excelPath, String sheetName, int startRow, int endRow) {
        LogUtils.info("[ExcelHelpers] Excel File: " + excelPath);
        LogUtils.info("[ExcelHelpers] Sheet Name: " + sheetName);

        Object[][] data = null;

        try {
            File f = new File(excelPath);

            if (!f.exists()) {
                try {
                    LogUtils.error("[ExcelHelpers] File Excel path not found.");
                    throw new RuntimeException("File Excel path not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);

            int rows = getRows();
            int columns = getColumns();

            LogUtils.info("[ExcelHelpers] Row: " + rows + " - Column: " + columns);
            LogUtils.info("[ExcelHelpers] StartRow: " + startRow + " - EndRow: " + endRow);

            data = new Object[(endRow - startRow) + 1][1];
            Hashtable<String, String> table = null;
            for (int rowNums = startRow; rowNums <= endRow; rowNums++) {
                table = new Hashtable<>();
                for (int colNum = 0; colNum < columns; colNum++) {
                    table.put(getCellData(0, colNum), getCellData(rowNums, colNum));
                }
                data[rowNums - startRow][0] = table;
            }

        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.error("[ExcelHelpers] " + e.getMessage());
        }

        return data;
    }

    // Get data from specific rows
    public Object[][] getDataFromSpecificRows(String excelPath, String sheetName, int[] rowNumbers) {
        LogUtils.info("[ExcelHelpers] Excel File: " + excelPath);
        LogUtils.info("[ExcelHelpers] Sheet Name: " + sheetName);
        LogUtils.info("[ExcelHelpers] Reading data from specific rows: " + Arrays.toString(rowNumbers));

        Object[][] data = null;

        try {
            File f = new File(excelPath);

            if (!f.exists()) {
                System.out.println("File Excel path not found.");
                throw new FileNotFoundException("File Excel path not found.");
            }

            fis = new FileInputStream(excelPath);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                LogUtils.error("[ExcelHelpers] Sheet name not found.");
                throw new RuntimeException("Sheet name not found.");
            }

            int columns = getColumns();
            LogUtils.info("[ExcelHelpers] Column count: " + columns);

            // Khởi tạo mảng data với kích thước bằng số lượng dòng được chỉ định
            data = new Object[rowNumbers.length][columns];

            // Đọc dữ liệu từ các dòng được chỉ định
            for (int i = 0; i < rowNumbers.length; i++) {
                int rowNum = rowNumbers[i];
                // Kiểm tra xem dòng có tồn tại không
                if (rowNum > sheet.getLastRowNum()) {
                    LogUtils.warn("[ExcelHelpers] WARNING: Row " + rowNum + " does not exist in the sheet.");
                    // Gán giá trị rỗng cho dòng không tồn tại
                    for (int j = 0; j < columns; j++) {
                        data[i][j] = "";
                    }
                    continue;
                }

                for (int j = 0; j < columns; j++) {
                    data[i][j] = getCellData(rowNum, j);
                }
            }

            // Đóng workbook và FileInputStream
            workbook.close();
            fis.close();

        } catch (Exception e) {
            LogUtils.error("[ExcelHelpers] Exception in getDataFromSpecificRows: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    // Get data from specific rows with Hashtable
    public Object[][] getDataHashTableFromSpecificRows(String excelPath, String sheetName, int[] rowNumbers) {
        LogUtils.info("[ExcelHelpers] Excel File: " + excelPath);
        LogUtils.info("[ExcelHelpers] Sheet Name: " + sheetName);
        LogUtils.info("[ExcelHelpers] Reading data from specific rows: " + Arrays.toString(rowNumbers));

        Object[][] data = null;

        try {
            File f = new File(excelPath);

            if (!f.exists()) {
                System.out.println("File Excel path not found.");
                throw new FileNotFoundException("File Excel path not found.");
            }

            fis = new FileInputStream(excelPath);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                LogUtils.error("[ExcelHelpers] Sheet name not found.");
                throw new RuntimeException("Sheet name not found.");
            }

            int columns = getColumns();
            // Khởi tạo mảng data với kích thước bằng số lượng dòng được chỉ định
            data = new Object[rowNumbers.length][1];

            // Đọc dữ liệu từ các dòng được chỉ định
            for (int i = 0; i < rowNumbers.length; i++) {
                int rowNum = rowNumbers[i];
                // Kiểm tra xem dòng có tồn tại không
                if (rowNum > sheet.getLastRowNum()) {
                    LogUtils.warn("[ExcelHelpers] WARNING: Row " + rowNum + " does not exist in the sheet.");
                    data[i][0] = new Hashtable<String, String>();
                    continue;
                }

                Hashtable<String, String> table = new Hashtable<>();
                for (int j = 0; j < columns; j++) {
                    // Lấy tên cột từ dòng đầu tiên (header)
                    String columnName = getCellData(0, j);
                    // Lấy giá trị từ dòng hiện tại và cột j
                    String cellValue = getCellData(rowNum, j);
                    // Thêm vào Hashtable
                    table.put(columnName, cellValue);
                }
                data[i][0] = table;
            }

            // Đóng workbook và FileInputStream
            workbook.close();
            fis.close();

        } catch (Exception e) {
            LogUtils.error("[ExcelHelpers] Exception in getDataHashTableFromSpecificRows: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    public int getRowContains(String sTestCaseName, int colNum) {
        int i;
        int rowCount = getRows();
        for (i = 0; i < rowCount; i++) {
            if (getCellData(i, colNum).equalsIgnoreCase(sTestCaseName)) {
                break;
            }
        }
        return i;
    }

    public int getRows() {
        try {
            return sheet.getLastRowNum();
        } catch (Exception e) {
            LogUtils.error("[ExcelHelpers] " + e.getMessage());
            throw (e);
        }
    }

    public int getColumns() {
        try {
            row = sheet.getRow(0);
            return row.getLastCellNum();
        } catch (Exception e) {
            LogUtils.error("[ExcelHelpers] " + e.getMessage());
            throw (e);
        }
    }

    // Get cell data
    public String getCellData(int rowNum, int colNum) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                return "";
            }
            cell = row.getCell(colNum);
            if (cell == null) {
                return "";
            }
            String CellData = null;
            switch (cell.getCellType()) {
                case STRING:
                    CellData = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        CellData = String.valueOf(cell.getDateCellValue());
                    } else {
                        CellData = String.valueOf((long) cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    CellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;
                case FORMULA:
                    CellData = cell.getCellFormula();
                    break;
                case ERROR:
                    CellData = String.valueOf(cell.getErrorCellValue());
                    break;
                case _NONE:
                default:
                    CellData = "";
                    break;
            }
            return CellData;
        } catch (Exception e) {
            LogUtils.error("[ExcelHelpers] Error getting cell data at row " + rowNum + ", col " + colNum + ": " + e.getMessage());
            return "";
        }
    }

    public String getCellData(int rowNum, String columnName) {
        return getCellData(rowNum, columns.get(columnName));
    }

    public String getCellData(String columnName, int rowNum) {
        return getCellData(rowNum, columns.get(columnName));
    }

    // Write data to excel sheet
    public void setCellData(String text, int rowNumber, int colNumber) {
        try {
            row = sheet.getRow(rowNumber);
            if (row == null) {
                row = sheet.createRow(rowNumber);
            }
            cell = row.getCell(colNumber);

            if (cell == null) {
                cell = row.createCell(colNumber);
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
            text = text.trim().toLowerCase();
            if (text == "pass" || text == "passed" || text == "success") {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            }
            if (text == "fail" || text == "failed" || text == "failure") {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
            }
            style.setFillPattern(FillPatternType.NO_FILL);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            e.getMessage();
            LogUtils.error("[ExcelHelpers] " + e.getMessage());
        }
    }

    public void setCellData(String text, int rowNumber, String columnName) {
        try {
            row = sheet.getRow(rowNumber);
            if (row == null) {
                row = sheet.createRow(rowNumber);
            }
            cell = row.getCell(columns.get(columnName));

            if (cell == null) {
                cell = row.createCell(columns.get(columnName));
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
            text = text.trim().toLowerCase();
            if (text == "pass" || text == "passed" || text == "success") {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            }
            if (text == "fail" || text == "failed" || text == "failure") {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
            }

            style.setFillPattern(FillPatternType.NO_FILL);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();

            LogUtils.info("[ExcelHelpers] Write data to excel file successfully.");

        } catch (Exception e) {
            LogUtils.error("[ExcelHelpers] Error: column name not found.");
        }
    }

}

