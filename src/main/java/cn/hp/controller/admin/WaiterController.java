package cn.hp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 客服 controller -- 可以完成 后台登陆 -- 列表显示 ...
 */
@Controller
@RequestMapping("/waiter")
public class WaiterController {

    //TODO 登陆自行实现

    /**
     * 回到后台首页
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return "/html/customerService/back.html";
    }
}
