package com.dwliu.ssmintegration.utils;

import com.alibaba.fastjson.JSON;
import com.dwliu.ssmintegration.constants.ExceptionConstants;
import com.dwliu.ssmintegration.exception.MyException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author dwliu
 */
public class ExcelUtils {

    /**
     * 解决思路：采用Apache的POI的API来操作Excel，读取内容后保存到List中，再将List转Json（推荐Linked，增删快，与Excel表顺序保持一致）
     * <p>
     * Sheet表1  ————>    List1<Map<列头，列值>>
     * Sheet表2  ————>    List2<Map<列头，列值>>
     * <p>
     * 步骤1：根据Excel版本类型创建对于的Workbook以及CellSytle
     * 步骤2：遍历每一个表中的每一行的每一列
     * 步骤3：一个sheet表就是一个Json，多表就多Json，对应一个 List
     * 一个sheet表的一行数据就是一个 Map
     * 一行中的一列，就把当前列头为key，列值为value存到该列的Map中
     *
     * @param file
     * @return Map  一个线性HashMap，以Excel的sheet表顺序，并以sheet表明作为key，sheet表转换json后的字符串作为value
     * @throws IOException
     */
    public static LinkedHashMap<String, String> excel2json(File file) throws MyException {

        System.out.println("excel2json方法执行....");

        try {
            // 返回的map
            LinkedHashMap<String, String> excelMap = new LinkedHashMap<String, String>();

            // Excel列的样式，主要是为了解决Excel数字科学计数的问题
            CellStyle cellStyle = null;
            // 根据Excel构成的对象
            Workbook wb = null;
            // 如果是2007及以上版本，则使用想要的Workbook以及CellStyle
            if (file.getName().endsWith("xlsx")) {
                System.out.println("是2007及以上版本  xlsx");
                wb = new XSSFWorkbook(file);
                XSSFDataFormat dataFormat = (XSSFDataFormat) wb.createDataFormat();
                cellStyle = wb.createCellStyle();
                // 设置Excel列的样式为文本
                cellStyle.setDataFormat(dataFormat.getFormat("@"));
            } else {
                System.out.println("是2007以下版本  xls");
                POIFSFileSystem fs = new POIFSFileSystem(file);
                wb = new HSSFWorkbook(fs);
                HSSFDataFormat dataFormat = (HSSFDataFormat) wb.createDataFormat();
                cellStyle = wb.createCellStyle();
                // 设置Excel列的样式为文本
                cellStyle.setDataFormat(dataFormat.getFormat("@"));
            }

            // sheet表个数
            int sheetsCounts = wb.getNumberOfSheets();
            // 遍历每一个sheet
            for (int i = 0; i < sheetsCounts; i++) {
                Sheet sheet = wb.getSheetAt(i);
                System.out.println("第" + i + "个sheet:" + sheet.toString());

                // 一个sheet表对于一个List
                List list = new LinkedList();

                // 将第一行的列值作为正个json的key
                String[] cellNames;
                // 取第一行列的值作为key
                Row fisrtRow = sheet.getRow(0);
                // 如果第一行就为空，则是空sheet表，该表跳过
                if (null == fisrtRow) {
                    continue;
                }
                // 得到第一行有多少列
                int curCellNum = fisrtRow.getLastCellNum();
                System.out.println("第一行的列数：" + curCellNum);
                // 根据第一行的列数来生成列头数组
                cellNames = new String[curCellNum];
                // 单独处理第一行，取出第一行的每个列值放在数组中，就得到了整张表的JSON的key
                for (int m = 0; m < curCellNum; m++) {
                    Cell cell = fisrtRow.getCell(m);
                    // 设置该列的样式是字符串
                    cell.setCellStyle(cellStyle);
                    cell.setCellType(CellType.STRING);
                    // 取得该列的字符串值
                    cellNames[m] = cell.getStringCellValue();
                }
                for (String s : cellNames) {
                    System.out.print("得到第" + i + " 张sheet表的列头： " + s + ",");
                }
                System.out.println();

                // 从第二行起遍历每一行
                int rowNum = sheet.getLastRowNum();
                System.out.println("总共有 " + rowNum + " 行");
                for (int j = 1; j <= rowNum; j++) {
                    // 一行数据对于一个Map
                    LinkedHashMap rowMap = new LinkedHashMap();
                    // 取得某一行
                    Row row = sheet.getRow(j);
                    int cellNum = row.getLastCellNum();
                    // 遍历每一列
                    for (int k = 0; k < cellNum; k++) {
                        Cell cell = row.getCell(k);

                        cell.setCellStyle(cellStyle);
                        cell.setCellType(CellType.STRING);
                        // 保存该单元格的数据到该行中
                        rowMap.put(cellNames[k], cell.getStringCellValue());
                    }
                    // 保存该行的数据到该表的List中
                    list.add(rowMap);
                }
                // 将该sheet表的表名为key，List转为json后的字符串为Value进行存储
                excelMap.put(sheet.getSheetName(), JSON.toJSONString(list, false));
            }

            System.out.println("excel2json方法结束....");

            return excelMap;
        } catch (InvalidFormatException e) {
            throw new MyException(ExceptionConstants.READ_EXCEL_EXCEPTION);
        }catch (IOException e){
            throw new MyException(ExceptionConstants.READ_EXCEL_EXCEPTION);
        }catch (Exception e){
            throw new MyException(ExceptionConstants.READ_EXCEL_EXCEPTION);
        }
    }

    /**
     * @description: 根据excel文件生成对应的json字符串
     * @return
     * @throws FileNotFoundException
     */
    public static String createJson(String path) throws FileNotFoundException {
        InputStream is = new FileInputStream(path);
        StringBuffer buffer = new StringBuffer();
        try {
            Workbook workbook = new HSSFWorkbook(is);
            HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(0);
            String key = "";
            String value = "";
            buffer.append("[");
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                HSSFRow row = sheet.getRow(i);
                for(int j=0;j<row.getPhysicalNumberOfCells();j++){
                    HSSFCell cell = row.getCell(j);
                    if(i==0){
                        if(j==0){
                            key = cell.getStringCellValue();
                        }
                        if(j==1){
                            value = cell.getStringCellValue();
                        }
                    } else{
                        if(j==0){
                            buffer.append("{\"" + key + "\"" + ":" + "\"" +  getCellValue(cell) + "\"" + ",");
                        }
                        if(j==1){
                            buffer.append("\"" + value + "\"" + ":" + "\"" +  getCellValue(cell) + "\"}");
                        }

                    }
                }
                if(sheet.getPhysicalNumberOfRows()-1!=i && i!=0){
                    buffer.append(",");
                }
                buffer.append("\r");
            }
            buffer.append("]");
        } catch (IOException e) {
            System.out.println("出现异常");
            e.printStackTrace();
        }

        return buffer.toString();
    }

    /**
     * 获取当前单元格内容
     * */
    private static String getCellValue(Cell cell){
        String value = "";
        if(cell!=null){
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    if(HSSFDateUtil.isCellDateFormatted(cell)){ //日期类型
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                        value = sdf.format(date);
                    }else{
                        Integer data = (int) cell.getNumericCellValue();
                        value = data.toString();
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    Boolean data = cell.getBooleanCellValue();
                    value = data.toString();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    System.out.println("单元格内容出现错误");
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    value = String.valueOf(cell.getNumericCellValue());
                    if (value.equals("NaN")) {// 如果获取的数据值非法,就将其装换为对应的字符串
                        value = cell.getStringCellValue().toString();
                    }
                    break;
                case Cell.CELL_TYPE_BLANK:
                    System.out.println("单元格内容 为空值 ");
                    break;
                default :
                    value = cell.getStringCellValue().toString();
                    break;
            }
        }
        return value;
    }
}
