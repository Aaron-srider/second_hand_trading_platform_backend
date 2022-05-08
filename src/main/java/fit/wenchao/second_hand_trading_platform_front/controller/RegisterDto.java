package fit.wenchao.second_hand_trading_platform_front.controller;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class RegisterDto {
    String name;
    String phone;
    String email;
    String city;
    String sex;
    String password;
    String bankAccount;
}