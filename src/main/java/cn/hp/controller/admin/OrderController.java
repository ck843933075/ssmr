package cn.hp.controller.admin;

import cn.hp.bean.Order;
import cn.hp.service.CustomerService;
import cn.hp.service.MasterService;
import cn.hp.service.OrderService;
import cn.hp.utils.JsonData;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作订单
 */
@Controller
@RequestMapping("/order")
@Transactional
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MasterService masterService;

    /**
     * 回到订单列表页 --  table.html
     * @return
     */
    @RequestMapping("/selectList")
    public String toOrderList(){
        return "/html/orders/table.html";
    }

    /**
     * 得到分页的 订单数据
     * @return
     */
    @RequestMapping("/getOrders")
    @ResponseBody
    public Object getOrders(String phone, String status,
                            @RequestParam(value = "page",defaultValue = "1") Integer page,
                            @RequestParam(value = "limit",defaultValue = "5") Integer limit){
        Map<String,Object> map = new HashMap<String, Object>();
        if(!StringUtils.isEmpty(phone)){
            map.put("phone",phone);
        }
        if(!StringUtils.isEmpty(status)){
            map.put("status",status);
        }
        PageInfo<Order> pageInfo = orderService.findByPages(map,page,limit);
        return JsonData.buildSuc(pageInfo);
    }

    /**
     * 回到添加订单页面
     * @return
     */
    @RequestMapping("/add")
    public String add(){
        return "/html/orders/addOrder.html";
    }

    /**
     * 完成订单添加   (是客服完成的 )
     *  当前订单 如果已经完成 ,才允许 同1个手机号 再次添加数据
     *
     *   还要保存数据到客户
     *
     *   同1个手机号 ,只能包含 1个未完成的订单 ,如果数据库已存在未完成 ,那就不能再添加了.
     * @return
     */
    @RequestMapping("/addOrder")
    @ResponseBody
    public Object addOrder(Order order,String name,String car){
        List<Order> orders =  orderService.findByPhone(order.getPhone());
        if(orders==null||orders.size()==0){
            //保存数据到 客户表
            int x = customerService.saveCustomer(order.getPhone(),name,order.getAddress(),new Date(),car);
            //保存数据到 订单表
            int y = orderService.addOrder(order);
            if(x>0&&y>0){
                return "success";
            }
        }else{
            for(Order o: orders){
                String status = o.getStatus();
                if(status.equals("3")||status.equals("4")){  //表示当前订单已完成 ,可以再次添加新订单
                    //保存数据到 客户表
                    int x = customerService.saveCustomer(order.getPhone(),name,order.getAddress(),new Date(),car);
                    //保存数据到 订单表
                    int y = orderService.addOrder(order);
                    if(x>0&&y>0){
                        return "success";
                    }
                }else{
                    return "error";
                }
            }
        }
        return  "error";
    }

    /**
     * 跳转到指派订单页面
     * @return
     */
    @RequestMapping("/sendMaster")
    public String sendMaster(){
        return "/html/orders/master/table.html";
    }


    /**
     * 派单 --  对于维修工 就是接单
     *  mid:    维修工id
     *  id:     订单表id
     */
    @RequestMapping("/receiveMaster")
    @ResponseBody
    public  Object  receiveMaster(Integer mid,Integer id){

        //1.根据 订单id 修改 订单 状态 --  "已接单"
        int i = orderService.updateOrderStatusById(id);
        if(i>0){
            //1.1 根据mid 修改 维修工的状态
            int x = masterService.updateMasterStatusByMid(mid);
            return x;//2.将结果返回
        }
        return 0;   //失败了
    }

    /**
     * 下载订单数据到Excel
     *
     * 思路:
     * 1. 创建 Excel ,填充 表头数据
     *
     * 2.查询订单集合数据
     *  2.1 将集合数据内容 填充到 Excel中
     *
     *  订单号
     * 客户
     * 手机号
     * 下单时间
     * 地址
     * 状态
     */
    @RequestMapping("/down")
    @ResponseBody
    public void down() throws Exception {
        //1.创建  Excel对象  ,相当于 鼠标创建了一个 Excel文件
        Workbook wb = new HSSFWorkbook();
        //2. 创建sheet
        Sheet sheet = wb.createSheet("表格1");
        //3.创建行     ,0代表 索引 ,是第一行
        Row row = sheet.createRow(0);
        //存放表头的数组
        Cell[] cells = new Cell[6];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = row.createCell(i);   //在第一行创建 6个单元格 ,属于表头的内容
        }
        cells[0].setCellValue("订单号");
        cells[1].setCellValue("客户");
        cells[2].setCellValue("手机号");
        cells[3].setCellValue("下单时间");
        cells[4].setCellValue("地址");
        cells[5].setCellValue("状态");

        //下面填充 第2行到  ....无限行数据
        List<Order> list = orderService.findOrders();

        for (int i = 0; i < list.size(); i++) {
             //创建 其他行 (第1行去除)
            Row row1 = sheet.createRow(i + 1);
            Cell[] cells1 = new Cell[6];    // 其他行中 ,每一行只有6个单元格
            for (int j = 0; j < cells1.length; j++) {   //创建其他行里面的 单元格
                cells1[j] = row1.createCell(j);
            }

            //在循环中填充数据
            Order order = list.get(i);
            cells1[0].setCellValue(order.getId());
            cells1[1].setCellValue(order.getCustomer().getName());
            cells1[2].setCellValue(order.getPhone());
            cells1[3].setCellValue(order.getCreatetime());
            cells1[4].setCellValue(order.getAddress());
            String status = order.getStatus();
            String str = "";
            if(status.equals("0")){
                str="刚下单";
            }else  if(status.equals("1")){
                str="已接单";
            }else  if(status.equals("2")){
                str="已到达正在修";
            }else  if(status.equals("3")){
                str="已完成";
            }
            cells1[5].setCellValue(str);
        }

        //写出
        OutputStream os = new FileOutputStream("C:\\order1.xls");
        wb.write(os);

        os.close();
    }
}

















