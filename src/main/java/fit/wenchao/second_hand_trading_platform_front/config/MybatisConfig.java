package fit.wenchao.second_hand_trading_platform_front.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value = "fit.wenchao.second_hand_trading_platform_front.dao.mapper")
public class MybatisConfig {
 
}