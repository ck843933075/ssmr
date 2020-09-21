package cn.hp.mapper;

import cn.hp.bean.MasterAddress;
import cn.hp.bean.MasterAddressExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MasterAddressMapper {
    int countByExample(MasterAddressExample example);

    int deleteByExample(MasterAddressExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MasterAddress record);

    int insertSelective(MasterAddress record);

    List<MasterAddress> selectByExample(MasterAddressExample example);

    MasterAddress selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MasterAddress record, @Param("example") MasterAddressExample example);

    int updateByExample(@Param("record") MasterAddress record, @Param("example") MasterAddressExample example);

    int updateByPrimaryKeySelective(MasterAddress record);


   /* int updateByPrimaryKeySelective2(MasterAddress record);*/

    int updateByPrimaryKey(MasterAddress record);
}