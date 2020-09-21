package cn.hp.service;

import cn.hp.bean.Order;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface OrderService {
    //条件分页查询
    PageInfo<Order> findByPages(Map<String, Object> map, Integer page, Integer limit);

    //根据手机号查询订单
    List<Order> findByPhone(String phone);

    //保存到订单
    int addOrder(Order order);

    //根据 id修改状态
    int updateOrderStatusById(Integer id);

    //查询所有订单
    List<Order> findOrders();
}
