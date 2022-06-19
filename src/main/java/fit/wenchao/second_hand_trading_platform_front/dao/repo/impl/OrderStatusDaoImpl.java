package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.OrderStatusMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.po.OrderStatusPO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.OrderStatusDao;
import org.springframework.stereotype.Repository;

@Repository
public class OrderStatusDaoImpl extends ServiceImpl<OrderStatusMapper, OrderStatusPO> implements OrderStatusDao {

}
