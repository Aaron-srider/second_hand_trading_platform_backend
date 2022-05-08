package fit.wenchao.second_hand_trading_platform_front.dao.mapper;

import fit.wenchao.second_hand_trading_platform_front.dao.po.StoreLevelPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商家等级，不同等级有不同费率，费率可以由管理员设置 Mapper 接口
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public interface StoreLevelMapper extends BaseMapper<StoreLevelPO> {

}
