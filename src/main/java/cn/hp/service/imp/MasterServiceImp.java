package cn.hp.service.imp;

import cn.hp.bean.Master;
import cn.hp.bean.MasterAddress;
import cn.hp.bean.MasterAddressExample;
import cn.hp.mapper.MasterAddressMapper;
import cn.hp.mapper.MasterMapper;
import cn.hp.service.MasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterServiceImp implements MasterService {

    @Autowired
    private MasterMapper masterMapper;

    @Autowired
    private MasterAddressMapper masterAddressMapper;

    public PageInfo<Master> findByPages(Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<Master>  list = masterMapper.findByPages();

        return new PageInfo<Master>(list);
    }

    public int updateMasterStatusByMid(Integer mid) {
        MasterAddress ma = new MasterAddress();
        ma.setMid(mid);
        ma.setStatus("0");
        //masterAddressMapper.updateByPrimaryKeySelective2(ma);

        MasterAddressExample example = new MasterAddressExample();
        MasterAddressExample.Criteria criteria = example.createCriteria();
        criteria.andMidEqualTo(mid);

        return   masterAddressMapper.updateByExampleSelective(ma,example);
    }
}













