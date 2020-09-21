package cn.hp.service;

import cn.hp.bean.Master;
import com.github.pagehelper.PageInfo;

public interface MasterService {

    PageInfo<Master> findByPages(Integer page, Integer limit);
    //根据外键修改 状态
    int updateMasterStatusByMid(Integer mid);
}
