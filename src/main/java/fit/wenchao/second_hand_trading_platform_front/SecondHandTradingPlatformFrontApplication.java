package fit.wenchao.second_hand_trading_platform_front;

import fit.wenchao.mybatisCodeGen.codegen.MybatisCodeGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecondHandTradingPlatformFrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondHandTradingPlatformFrontApplication.class, args);
        //MybatisCodeGenerator.generateStructureCode();
    }

}
