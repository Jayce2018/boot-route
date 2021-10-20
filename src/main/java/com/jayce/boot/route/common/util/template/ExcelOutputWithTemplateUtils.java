package com.jayce.boot.route.common.util.template;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

class ExcelConstant {
    public final static Integer INTEGER = 1;
    public final static Integer DOUBLE = 2;
    public final static Integer STRING = 3;
    public final static Integer DATE = 4;
    public final static Integer BOOLEAN = 5;
    public final static Map<Integer, CellStyle> STYLE_MAP = new HashMap<>();
}

/**
 * 按模板导出数据
 *
 * @author sunjie
 * @date 2020/3/3 18:59
 */
public class ExcelOutputWithTemplateUtils {
    private final static String XLS = "xls";
    private final static String XLSX = ".xlsx";


    /**
     * 本地导出出口
     */
    public static void exportLocal(String fileName, String excelTemplateUrl, Boolean isResources, HashMap<Integer, Object[][]> dateMap, String exportUrl) throws Exception {
        ExcelVO excelVO = transform(fileName, excelTemplateUrl, isResources, dateMap);
        export(excelVO.getWorkbook(), fileName, 1, excelVO.getSuffix(), exportUrl, null);
    }

    /**
     * 流导出出口
     */
    public static void exportResponse(String fileName, String excelTemplateUrl, Boolean isResources, HashMap<Integer, Object[][]> dateMap, HttpServletResponse response) throws Exception {
        ExcelVO excelVO = transform(fileName, excelTemplateUrl, isResources, dateMap);
        export(excelVO.getWorkbook(), fileName, 2, excelVO.getSuffix(), null, response);
    }

    /**
     * 解析模板
     */
    public static void parseExcelResource(String uri, Boolean isResources) throws Exception {
        HashMap<Integer, Integer> columnSizeMap = new HashMap<Integer, Integer>();
        Workbook workbook = getWorkBook(uri, isResources);
        //原始结果
        List<JSONObject> objectList = new ArrayList<>();
        //获取一共有多少sheet，然后遍历
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            //获取sheet中一共有多少行，遍历行（注意第一行是标题）
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            for (int j = 0; j < physicalNumberOfRows; j++) {
                //获取每一行有多少单元格，遍历单元格
                Row row = sheet.getRow(j);
                int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                columnSizeMap.put(i, physicalNumberOfCells);
                for (int k = 0; k < physicalNumberOfCells; k++) {
                    Cell cell = row.getCell(k);
                    //纪录数据
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("sheet", i);
                    jsonObject.put("index", "(" + j + "," + k + ")");
                    jsonObject.put("value", cell.getStringCellValue());
                    objectList.add(jsonObject);

                }
            }
        }
        System.out.println("sheet列数：" + columnSizeMap);
        System.out.println("原始模板数据:" + JSONArray.toJSON(objectList));
    }

    /**
     * getWorkBook
     */
    private static Workbook getWorkBook(String uri, Boolean isResources) {
        try {
            String[] strings = uri.split("\\.");
            String originalFilename = strings[strings.length - 1];
            // 获取文件输入流
            InputStream inputStream;
            if (isResources) {
                Resource resource = new ClassPathResource(uri);
                inputStream = resource.getInputStream();
            } else {
                inputStream = new FileInputStream(uri);
            }


            //创建一个workbook，对应一个Excel文件
            Workbook workbook;
            if (originalFilename.equals(XLS)) {
                workbook = new HSSFWorkbook(new POIFSFileSystem(inputStream));
            } else {
                workbook = new XSSFWorkbook(inputStream);
            }
            return workbook;
        } catch (Exception e) {
            System.out.println("throw new BusinessException(MultiCommonErrorCode.EXCEL_EXCEPTION_URL);");
        }
        return null;
    }

    /**
     * 单元格样式表,追加数据格式
     *
     * @param wb wb
     */
    private static void initStyleMap(Workbook wb) {
        //intStyle
        CellStyle intStyle = defaultStyle(wb);
        DataFormat intDataFormat = wb.createDataFormat();
        intStyle.setDataFormat(intDataFormat.getFormat("0"));
        ExcelConstant.STYLE_MAP.put(ExcelConstant.INTEGER, intStyle);
        //doubleStyle 注意excel小数型有效数字15，超过会自动截断
        CellStyle doubleStyle = defaultStyle(wb);
        DataFormat doubleDataFormat = wb.createDataFormat();
        doubleStyle.setDataFormat(doubleDataFormat.getFormat("0.00E+00"));
        ExcelConstant.STYLE_MAP.put(ExcelConstant.DOUBLE, doubleStyle);
        //stringStyle
        CellStyle stringStyle = defaultStyle(wb);
        DataFormat stringDataFormat = wb.createDataFormat();
        stringStyle.setDataFormat(stringDataFormat.getFormat("General"));
        ExcelConstant.STYLE_MAP.put(ExcelConstant.STRING, intStyle);
        //dateStyle
        CellStyle dateStyle = defaultStyle(wb);
        DataFormat dateDataFormat = wb.createDataFormat();
        dateStyle.setDataFormat(dateDataFormat.getFormat("yyyy/mm/dd hh:mm:ss"));
        ExcelConstant.STYLE_MAP.put(ExcelConstant.DATE, dateStyle);
        //booleanStyle
        CellStyle booleanStyle = defaultStyle(wb);
        DataFormat booleanDataFormat = wb.createDataFormat();
        booleanStyle.setDataFormat(booleanDataFormat.getFormat("General"));
        ExcelConstant.STYLE_MAP.put(ExcelConstant.BOOLEAN, dateStyle);
    }

    /**
     * 默认样式
     *
     * @param wb wb
     */
    private static CellStyle defaultStyle(Workbook wb) {
        //预设cell创建样式
        // 为数据内容设置特点新单元格样式2 自动换行 上下居中左右也居中
        CellStyle cellStyle = wb.createCellStyle();
        // 设置自动换行
        cellStyle.setWrapText(true);
        // 创建一个上下居中格式
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 左右居中
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置边框
        cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

        return cellStyle;
    }

    /**
     * 数据转换
     */
    private static ExcelVO transform(String fileName, String excelUrl, Boolean isResources, HashMap<Integer, Object[][]> dateMap) throws Exception {
        ExcelVO excelVO = new ExcelVO();
        //文件后缀
        String[] strings = excelUrl.split("\\.");
        String suffix = "." + strings[strings.length - 1];

        // 第一步，创建一个webbook，对应一个Excel文件
        Workbook wb = getWorkBook(excelUrl, isResources);
        //初始化样式表
        initStyleMap(wb);

        int numberOfSheets = wb.getNumberOfSheets();
        //sheet级别
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = wb.getSheetAt(i);
            //取得sheet渲染数据
            Object[][] sheetData = dateMap.get(i);
            //sheet无数据跳过
            if (null == sheetData) {
                continue;
            }
            //row级别
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            for (int j = 0; j < sheetData.length; j++) {
                //1、模板行直接写值
                if (j < numberOfRows && null != sheet.getRow(j)) {
                    Row row = sheet.getRow(j);
                    for (int k = 0; k < sheetData[j].length; k++) {
                        Cell cell = row.getCell(k);
                        if (null != sheetData[j][k]) {
                            conversion(sheetData[j][k], cell);
                        }
                    }
                } else {
                    //2、数据行新建并写值
                    Row row = sheet.createRow(j);
                    row.setHeightInPoints(25);
                    for (int k = 0; k < sheetData[j].length; k++) {
                        Cell cell = row.createCell(k);
                        if (null != sheetData[j][k]) {
                            conversion(sheetData[j][k], cell);
                        }
                    }
                }
            }
        }

        excelVO.setWorkbook(wb);
        excelVO.setFileName(fileName);
        excelVO.setSuffix(suffix);
        return excelVO;
    }

    /**
     * java对象转poi单元格对象
     *
     * @param item item
     * @param cell cell
     */
    private static void conversion(Object item, Cell cell) {
        if (Integer.class == item.getClass() || Short.class == item.getClass() || Long.class == item.getClass()) {
            cell.setCellValue(Double.parseDouble(item.toString()));
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellStyle(ExcelConstant.STYLE_MAP.get(ExcelConstant.INTEGER));
        } else if (Float.class == item.getClass() || Double.class == item.getClass()) {
            cell.setCellValue(Double.parseDouble(item.toString()));
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellStyle(ExcelConstant.STYLE_MAP.get(ExcelConstant.DOUBLE));
        } else if (String.class == item.getClass()) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue((String) item);
            cell.setCellStyle(ExcelConstant.STYLE_MAP.get(ExcelConstant.STRING));
        } else if (Date.class == item.getClass()) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue((Date) item);
            cell.setCellStyle(ExcelConstant.STYLE_MAP.get(ExcelConstant.DATE));
        } else if (Boolean.class == item.getClass()) {
            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
            cell.setCellValue((Boolean) item);
            cell.setCellStyle(ExcelConstant.STYLE_MAP.get(ExcelConstant.BOOLEAN));
        } else {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(item.toString());
            cell.setCellStyle(ExcelConstant.STYLE_MAP.get(ExcelConstant.STRING));
        }


    }

    /**
     * export
     */
    private static void export(Workbook wb, String fileName, Integer type, String suffix, String exportUrl, HttpServletResponse response) throws IOException {
        switch (type) {
            //本地导出
            case 1: {
                try {
                    FileOutputStream fout = new FileOutputStream(exportUrl + "/" + fileName + suffix);
                    wb.write(fout);
                    String strSuccess = "导出" + fileName + "成功！";
                    System.out.println(strSuccess + ",路径：" + exportUrl + "/" + fileName + suffix);
                    fout.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    String strFaile = "导出" + fileName + "失败！";
                    System.out.println(strFaile);
                }
                break;
            }
            //网页输出
            case 2: {
                //response.setContentType("application/ms-excel;charset=UTF-8");
                response.setContentType("*/*;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
                OutputStream out = response.getOutputStream();
                try {
                    // 将数据写出去
                    wb.write(out);
                    String strSuccess = "导出" + fileName + suffix + "成功！";
                    System.out.println(strSuccess);
                } catch (Exception e) {
                    e.printStackTrace();
                    String strFaile = "导出" + fileName + suffix + "失败！";
                    System.out.println(strFaile);
                } finally {
                    out.close();
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        HashMap<Integer, Object[][]> dateMap = new HashMap<>();
        Object[][] stringDate = new Object[4][13];
        stringDate[0][0] = "书籍导出标题";
        stringDate[3][0] = 0;
        stringDate[3][1] = "哈姆雷特";
        stringDate[3][2] = "jayce";
        stringDate[3][3] = new Date();
        stringDate[3][4] = "sydfdasd-sadsb";
        stringDate[3][5] = 14.11111111111111111111111111111111111;
        stringDate[3][6] = true;
        dateMap.put(0, stringDate);
        //parseExcelResource("d:/excel/测试模板.xlsx");
        exportLocal("模板结果测试", "/excel/测试模板.xlsx", true, dateMap, "d:/excel/result");
    }

}
