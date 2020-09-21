package cn.hp.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.util.Random;

//短信工具类
public class SmsUtil {

    public static String sendSms(String phoneNumber,String code) {
        //配置文件类 ,读取配置文件
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4G4coDJ8XD3PzL7MZvYZ", "BY4yMEl6QEZK7gHRNyxnFRb7yjfZRQ");
        IAcsClient client = new DefaultAcsClient(profile);


        //配置请求参数
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");   //查询参数  , 服务器所在地

        //设置 手机号
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        //设置标签名称
        request.putQueryParameter("SignName", "短信通");
        //模板 code
        request.putQueryParameter("TemplateCode", "SMS_190065275");
        //模板是一个json样式
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);    //通过客户端拿到 阿里服务器 响应的 短信内容
            return response.getData();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取4位随机数
     * @return
     */
    public static String getFourRandom(){
        Random random = new Random();
        String fourRandom = random.nextInt(10000) + "";
        int randLength = fourRandom.length();
        if(randLength<4){
            for(int i=1; i<=4-randLength; i++)
                fourRandom = "0" + fourRandom ;
        }
        return fourRandom;
    }

    public static void main(String[] args) {
        String fourRandom = getFourRandom();
        System.out.print(fourRandom);
    }
}
