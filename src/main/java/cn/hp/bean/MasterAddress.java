package cn.hp.bean;

import cn.hp.utils.LngAndLatUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class MasterAddress {
    private Integer id;

    private String lng;

    private String lat;

    private Integer mid;

    private String status;


    private String address;

    public String getAddress() {
       // String address ="";
        if(lng!=null&&lat!=null){
            try {
                address =LngAndLatUtil.getLngAndLat(lng, lat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng == null ? null : lng.trim();
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat == null ? null : lat.trim();
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}