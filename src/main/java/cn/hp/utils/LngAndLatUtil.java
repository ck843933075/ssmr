package cn.hp.utils;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

public class LngAndLatUtil {
	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String address = LngAndLatUtil.getLngAndLat("113.6246106","34.79432716");
		System.out.println(address);
	}
	static String ak = "A30zzID7Nm83tGTTjgegr7IbzIOSjCYA";
	
	
	public static String getLngAndLat(String lng,String lat) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String address = null;
    	Map<String,String> map = new LinkedHashMap();
    	String url="http://api.map.baidu.com/geocoder?location="+lat+","+lng+"&output=json&ak="+ak;
    	String jsonString = LngAndLatUtil.goGet(url);
        try {
            JSONObject obj = JSONObject.fromObject(jsonString);
            address = obj.getJSONObject("result").getString("formatted_address");
        } catch (Exception e) {
            map.put("error", "未找到相匹配的经纬度，请检查地址");
        }
        return address;
    }
	
	
	/**
	 * @time: 2020年5月7日下午12:46:39
	 * @return：Map
	 * @description:return  经纬度
	 */
	public static Map getLngAndLat(String address) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    	Map<String,String> map = new LinkedHashMap();
    	String url="http://api.map.baidu.com/geocoding/v3/?address="+address+"&output=json&ak="+ak;
    	String jsonString = LngAndLatUtil.goGet(url);
        try {
            JSONObject obj = JSONObject.fromObject(jsonString);
            if (obj.get("status").toString().equals("0")) {
                double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
                double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
                map.put("lng", getDecimal(lng)+"");
                map.put("lat", getDecimal(lat)+"");
            } else {
                map.put("error", "未找到相匹配的经纬度！");
                System.out.println("未找到相匹配的经纬度！");
            }
        } catch (Exception e) {
            map.put("error", "未找到相匹配的经纬度，请检查地址");
        }
        return map;
    }
 
    public static double getDecimal(double num) {
        if (Double.isNaN(num)) {
            return 0;
        }
        BigDecimal bd = new BigDecimal(num);
        num = bd.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num;
    }
 
    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return json.toString();
    }
    
    
    public static String goGet(String url) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		BufferedReader bufferedReader=null;
		HttpResponse httpResponse=null;
		StringBuffer sb = new StringBuffer();
		try {
			//HttpClient发出一个HttpGet请求
			httpResponse=httpClient.execute(httpGet);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		int statusCode=httpResponse.getStatusLine().getStatusCode();
		if (statusCode==HttpStatus.SC_OK) {
			HttpEntity httpEntity=httpResponse.getEntity();
			if (httpEntity!=null) {
				try {
					bufferedReader=new BufferedReader
					(new InputStreamReader(httpEntity.getContent(), "UTF-8"), 8*1024);
				    String line=null;
					while ((line=bufferedReader.readLine())!=null) {
						sb.append(line);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
    	return sb.toString();
    }
}
