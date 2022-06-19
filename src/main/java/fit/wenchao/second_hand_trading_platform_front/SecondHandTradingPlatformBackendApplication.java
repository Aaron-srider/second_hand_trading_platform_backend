package fit.wenchao.second_hand_trading_platform_front;

import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsEvaPO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecondHandTradingPlatformBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondHandTradingPlatformBackendApplication.class, args);
        //MybatisCodeGenerator.generateStructureCode();
    }

}
