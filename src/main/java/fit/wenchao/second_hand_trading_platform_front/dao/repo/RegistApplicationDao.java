package fit.wenchao.second_hand_trading_platform_front.dao.repo;

import fit.wenchao.second_hand_trading_platform_front.dao.po.RegistApplicationPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户注册的申请存在该表中，由管理员审批 dao类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
public interface RegistApplicationDao extends IService<RegistApplicationPO> {

    List<RegistApplicationPO> getPage(Integer pageSize, Integer pageNo);
}
