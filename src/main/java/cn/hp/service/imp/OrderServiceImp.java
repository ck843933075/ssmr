package cn.hp.service.imp;

import cn.hp.bean.Order;
import cn.hp.bean.OrderExample;
import cn.hp.mapper.OrderMapper;
import cn.hp.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public PageInfo<Order> findByPages(Map<String, Object> map, Integer page, Integer limit) {
        PageHelper.startPage(page,limit);   //开始分页位置
        List<Order> list =  orderMapper.findByPages(map);
        PageInfo<Order> pageInfo = new PageInfo<Order>(list);
        return pageInfo;
    }

    public List<Order> findByPhone(String phone) {
        OrderExample example = new OrderExample();
        // 拿到 条件查询 criteria
        OrderExample.Criteria criteria = example.createCriteria();
        //  where phone=#{phone} limit address like #{address} id asc
        criteria.andPhoneEqualTo(phone);
        //criteria.andAddressLike("%郑州%");
        //example.setOrderByClause("id asc");
        List<Order> orders = orderMapper.selectByExample(example);
        return orders;
    }

    public int addOrder(Order order) {
        order.setStatus("0");//刚下单
        int insert = orderMapper.insert(order);
        return insert;
    }

    public int updateOrderStatusById(Integer id) {
        Order  o = new Order();
        o.setId(id);
        o.setStatus("1");   //已接单
        int i = orderMapper.updateByPrimaryKeySelective(o);
        return i;
    }

    public List<Order> findOrders() {
        return orderMapper.findOrders();
    }
}
















