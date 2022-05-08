package fit.wenchao.second_hand_trading_platform_front.dao.repo.impl;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import fit.wenchao.second_hand_trading_platform_front.dao.po.RegistApplicationPO;
import fit.wenchao.second_hand_trading_platform_front.dao.mapper.RegistApplicationMapper;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.RegistApplicationDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户注册的申请存在该表中，由管理员审批 dao实现类
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@Repository
@Slf4j
public class RegistApplicationDaoImpl extends ServiceImpl<RegistApplicationMapper, RegistApplicationPO> implements RegistApplicationDao {

    @Autowired
    RegistApplicationMapper registApplicationMapper;

    @Override
    public List<RegistApplicationPO> getPage(Integer pageSize, Integer pageNo) {
        Integer skip = (pageNo - 1) * pageSize;
        log.info("skip:{} pageSize:{}", skip, pageSize);
        return registApplicationMapper.getPage(pageSize, skip);

    }
}
