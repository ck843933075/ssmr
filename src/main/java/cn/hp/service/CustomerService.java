package cn.hp.service;

import cn.hp.bean.Customer;

import java.util.Date;
import java.util.List;

//客户业务层
public interface CustomerService {
    //保存到客户
    int saveCustomer(String phone, String name, String address, Date date, String car);

    //保存
    void save(List<Customer> list);
}
