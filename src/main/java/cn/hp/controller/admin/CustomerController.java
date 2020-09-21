package cn.hp.controller.admin;

import cn.hp.bean.Customer;
import cn.hp.service.CustomerService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台客户管理
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 跳转到客户列表
     * @return
     */
    @RequestMapping("/selectList")
    public String selectList(){
        return "/html/customer/table.html";
    }

    /**
     * 上传
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Object upload(MultipartFile file , HttpServletRequest request) throws IOException {
        Map<String,Object>  map = new HashMap<String, Object>();

        System.out.println("测试");
        //文件原始名称
        String originalFilename = file.getOriginalFilename();

        //TODO 如果想限制 文件类型 , originalFilename  截取后缀  .png

        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));//文件后缀

        if(!suffix.equals("xls")){
            map.put("type","error");
            map.put("msg","文件格式不正确,请选择03版excel");
        }

        //指定上传位置
        String realPath = request.getServletContext().getRealPath("/upload");

        File  f = new File(realPath);
        if(!f.exists()){
            f.mkdir();
        }

        //拼接 路径 ,开启上传
        String filename = realPath+File.separator+originalFilename;
        System.out.println("filename==="+filename);

        try {
            file.transferTo(new File(filename));
            if(!suffix.equals("xls")){
                map.put("type","success");
                map.put("msg","文件上传成");
            }
        } catch (IOException e) {
            map.put("type","error");
            map.put("msg","文件上传失败");
            e.printStackTrace();
        }

        //解析 上传问题
        //1.创建  Excel对象  ,直接关联已存在 excel文件
        Workbook wb = new HSSFWorkbook(new FileInputStream("filename"));

        //2. 获取 sheet1 表格对象
        HSSFSheet sheet1 = (HSSFSheet) wb.getSheet("sheet1");

        List<Customer> list = new ArrayList<Customer>();

        //3.取出 sheet1中所有行
        for(Row row:sheet1){
            //过滤第1行数据 -- 不能保存到数据库
            if(row.getRowNum()==0){
                continue;
            }

            //取出 行数据 ()
            String name = row.getCell(0).getStringCellValue();  //取出 第1个单元格中的 数据
            String time = row.getCell(1).getStringCellValue();
            String card = row.getCell(2).getStringCellValue();
            String car = row.getCell(3).getStringCellValue();
            String sex = row.getCell(4).getStringCellValue();

            Customer c = new Customer();
            c.setName(name);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.setCreatetime(date);
            c.setIdcard(card);
            c.setCar(car);
            c.setSex(sex);

            list.add(c);
        }
        //保存集合到数据库
       customerService.save(list);
       return map;
    }
}




















