package fit.wenchao.second_hand_trading_platform_front.controller;

import fit.wenchao.second_hand_trading_platform_front.dao.po.RegistApplicationPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class RegisterVo {

    public interface ReceiveFrontData{}

    @NotNull(groups = ReceiveFrontData.class)
    String name;

    @NotNull(groups = ReceiveFrontData.class)
    String phone;

    @NotNull(groups = ReceiveFrontData.class)
    String email;

    @NotNull(groups = ReceiveFrontData.class)
    String city;

    @NotNull(groups = ReceiveFrontData.class)
    String sex;

    @NotNull(groups = ReceiveFrontData.class)
    String password;

    @NotNull(groups = ReceiveFrontData.class)
    @Length(max=16 ,min=16, message="银行卡号必须为16位数", groups = {ReceiveFrontData.class})
    String bankAccount;
}
