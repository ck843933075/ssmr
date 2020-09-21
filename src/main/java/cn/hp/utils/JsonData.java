package cn.hp.utils;

/**
 * 处理json的工具类
 */
public class JsonData {
    private Integer code;   //状态码
    private String msg; //返回的消息
    private Object data;    //返回的数据

    public JsonData(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonData() { }
    //操作成功,不需要返回数据
    public static JsonData buildSuc(String msg){
        return new JsonData(200,msg,null);
    }
    //操作成功,返回数据
    public static JsonData buildSuc(String msg, Object data){
        return new JsonData(200,msg,data);
    }
    public static JsonData buildSuc(Object data) {
        return new JsonData(200,null,data);
    }
    //操作失败
    public static JsonData buildError(String msg){
        return new JsonData(-1,msg,null);
    }



    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
