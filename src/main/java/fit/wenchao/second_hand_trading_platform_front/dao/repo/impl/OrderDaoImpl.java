package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import fit.wenchao.second_hand_trading_platform_front.dao.po.OrderPO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.OrderMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.OrderDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单表
 dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
public class OrderDaoImpl extends ServiceImpl<OrderMapper, OrderPO> implements OrderDao {

}
