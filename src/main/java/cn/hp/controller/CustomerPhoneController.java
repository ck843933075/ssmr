package cn.hp.controller;

import cn.hp.utils.MapUtils;
import cn.hp.utils.RedisUtil;
import cn.hp.utils.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 客户手机端 主页(逻辑操作)
 */

@Controller
@RequestMapping("/cphone")
public class CustomerPhoneController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 回到前台手机主页
     * @return
     */
    @RequestMapping("/to_index")
    public String home(){
        return "/html/customerPhone/index.html";
    }


    /**
     * 计算2个坐标的 间距 -- 得出价格
     * @param lng
     * @param lat
     * @param oldLng
     * @param oldLat
     * @return
     */
    @RequestMapping("/getMoney")
    @ResponseBody
    public String calculationPrices(String lng,String lat,String oldLng,String oldLat){
        int result = (int) MapUtils.algorithm(Double.valueOf(lng),Double.valueOf(lat),Double.valueOf(oldLng),Double.valueOf(oldLat))/100;

        return result +"";
    }

    /**
     * 检查是否 已提交
     *
     *      方式1: 检查session中 是否 包含当前 用户手机号
     *      方式2: 使用token认证
     * @return
     */
    @RequestMapping("/isLogin")
    @ResponseBody
    public String isLogin(HttpSession session){
        String phone = (String) session.getAttribute("phone");
        if(StringUtils.isEmpty(phone)){
            //未登录
            return "error";
        }else {
            return "success";
        }
    }

    @RequestMapping("/to_login")
    public String to_login(){
        return "/html/customerPhone/html/login.html";
    }


    /**
     * 获取验证码    (设置后台验证码失效时间)
     * @return
     */
    @RequestMapping("/sendSms")
    @ResponseBody
    public String sendSms(String phone,String code){
        String fourRandom = SmsUtil.getFourRandom();    //拿到4位 数字

        String result = SmsUtil.sendSms(phone, code);
        if(result.contains("OK")){
            redisUtil.setEx(phone,fourRandom,5, TimeUnit.SECONDS);
        }else{
            return "验证码发送失败";
        }
        return fourRandom;
    }

    @RequestMapping("/login")
    @ResponseBody
    public Map<String,Object>  login(String phone,String code,HttpSession session){
        Map<String,Object> map = new HashMap<String, Object>();
        if(StringUtils.isEmpty(phone)){
            map.put("type","error");
            map.put("msg","手机号不能为空");
            return map;
        }
        if(StringUtils.isEmpty(code)){
            map.put("type","error");
            map.put("msg","验证码不能为空");
            return map;
        }

        String rcode = (String) redisUtil.get(phone);

        if(StringUtils.isEmpty(rcode)){
            map.put("type","error");
            map.put("msg","验证码已失效");
            return map;
        }else{
            if(!code.equalsIgnoreCase(rcode)){
                map.put("type","error");
                map.put("msg","验证码不正确");
                return map;
            }
        }


        //条件都通过后 TODO  后续需要扩展
        session.setAttribute("phone",phone);

        map.put("type","success");
        map.put("msg","登陆成功");
        return map;
    }
}





















