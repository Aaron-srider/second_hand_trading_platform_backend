package fit.wenchao.second_hand_trading_platform_front.dao.repo;

import fit.wenchao.second_hand_trading_platform_front.dao.po.StoreLevelPO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商家等级，不同等级有不同费率，费率可以由管理员设置 dao类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public interface StoreLevelDao extends IService<StoreLevelPO> {

}
