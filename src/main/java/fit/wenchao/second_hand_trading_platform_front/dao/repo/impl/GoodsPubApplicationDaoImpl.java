package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsPubApplicationPO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.GoodsPubApplicationMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsPubApplicationDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 * 存储商家的发布商品申请，有管理员审批 dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
public class GoodsPubApplicationDaoImpl extends ServiceImpl<GoodsPubApplicationMapper, GoodsPubApplicationPO> implements GoodsPubApplicationDao {

}
