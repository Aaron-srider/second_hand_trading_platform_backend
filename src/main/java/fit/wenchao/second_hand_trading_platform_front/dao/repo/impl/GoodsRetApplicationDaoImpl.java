package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsRetApplicationPO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.GoodsRetApplicationMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsRetApplicationDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户的退货申请，用户可以在到货后24小时内提出退货申请 dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
public class GoodsRetApplicationDaoImpl extends ServiceImpl<GoodsRetApplicationMapper, GoodsRetApplicationPO> implements GoodsRetApplicationDao {

}
