package com.lym.springboot.web.util.poi;

import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * PIO 操作 Excel 工具类
 * Created by liuyanmin on 2019/10/12.
 */
public class PioExcelUtil {

    /**
     * 根据模板填充数据并导出 （支持2007版本Excel）
     *      只支持表格横列下标添加元素值，不支持 List
     * @param importFilePath 导入模板路径
     * @param exportFilePath 导出路径
     * @param sheetIndex excel 中 sheet 页的下标
     * @param cells 单元格信息
     */
    public static void createXLSX(String importFilePath, String exportFilePath, int sheetIndex, List<PioCell> cells) throws IOException {
        // 读取模板文件
        InputStream in = PioExcelUtil.class.getResourceAsStream(importFilePath);
        // 读取sheet
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        // 如果这行没有了，整个公式都不会有自动计算的效果的
        sheet.setForceFormulaRecalculation(true);

        // 给相应的单元格赋值
        for (PioCell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            Object data = cell.getData();
            if (cell.getData() instanceof Boolean) {
                sheet.getRow(row).getCell(col).setCellValue((Boolean) data);
            } else if (data instanceof Double) {
                sheet.getRow(row).getCell(col).setCellValue((Double) data);
            } else if (data instanceof Date) {
                sheet.getRow(row).getCell(col).setCellValue((Date) data);
            } else if (data instanceof Calendar) {
                sheet.getRow(row).getCell(col).setCellValue((Calendar) data);
            } else if (data instanceof RichTextString) {
                sheet.getRow(row).getCell(col).setCellValue((RichTextString) data);
            } else {
                sheet.getRow(row).getCell(col).setCellValue(data.toString());
            }
        }

        FileOutputStream out = new FileOutputStream(exportFilePath);
        workbook.write(out);
        out.close();
    }
}
