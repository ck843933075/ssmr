package cn.hp.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ExcelTest {


    @Test
    public void test1() throws Exception {
        //1.创建  Excel对象  ,相当于 鼠标创建了一个 Excel文件
        Workbook wb = new HSSFWorkbook();
        //2. 创建sheet
        Sheet sheet = wb.createSheet("表格1");
        //3.创建行     ,0代表 索引 ,是第一行
        Row row = sheet.createRow(0);
        //4.创建 第1行的 单元格
        Cell cell0 = row.createCell(0);
        Cell cell1 = row.createCell(1);
        Cell cell2 = row.createCell(2);
        Cell cell3 = row.createCell(3);
        //给第一行每一个单元格 添加 标题内容
        cell0.setCellValue("编号");
        cell1.setCellValue("姓名");
        cell2.setCellValue("性别");
        cell3.setCellValue("年龄");

        //5.将Excel 整体写到1个位置
        OutputStream os = new FileOutputStream("C:\\test.xls");
        wb.write(os);

        os.close();
    }
}




















