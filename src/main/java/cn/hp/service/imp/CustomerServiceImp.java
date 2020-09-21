package cn.hp.service.imp;

import cn.hp.bean.Customer;
import cn.hp.mapper.CustomerMapper;
import cn.hp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerServiceImp implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;


    public int saveCustomer(String phone, String name, String address, Date date, String car) {

        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setCar(car);
        customer.setCreatetime(date);

        int i = customerMapper.insertSelective(customer);

        return i;
    }

    public void save(List<Customer> list) {
        for(Customer c:list){
            customerMapper.insert(c);
        }
    }
}
