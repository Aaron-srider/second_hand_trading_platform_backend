package fit.wenchao.second_hand_trading_platform_front.dao.mapper;

import fit.wenchao.second_hand_trading_platform_front.dao.po.RegistApplicationPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户注册的申请存在该表中，由管理员审批 Mapper 接口
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public interface RegistApplicationMapper extends BaseMapper<RegistApplicationPO> {

    @Select("select * from regist_application LIMIT #{skip}, #{pageSize}")
    List<RegistApplicationPO> getPage(@Param("pageSize") Integer pageSize, @Param("skip") Integer skip);
}
