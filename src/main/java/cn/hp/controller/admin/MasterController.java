package cn.hp.controller.admin;

import cn.hp.bean.Master;
import cn.hp.service.MasterService;
import cn.hp.utils.JsonData;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/master")
public class MasterController {

    @Autowired
    private MasterService masterService;

    /**
     * 获取  维修工列表数据
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("/selectList")
    @ResponseBody
    public Object selectList(Integer page,Integer limit){
        PageInfo<Master> pageInfo = masterService.findByPages(page,limit);
        return JsonData.buildSuc(pageInfo);
    }

}















