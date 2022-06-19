
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import fit.wenchao.second_hand_trading_platform_front.utils.DontReturn;
import fit.wenchao.second_hand_trading_platform_front.utils.MapName;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

/**
 * <p>
 * 用户表
 *
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("`user`")
public class UserPO implements Serializable {

    public interface RegisterNew {
    }

    public static final long serialVersionUID = 1L;

    @TableId(type = AUTO)
    public Integer id;

    /**
     * 用户姓名
     */
    @NotNull(groups = {RegisterNew.class})
    public String name;

    /**
     * 用户手机号
     */
    @NotNull(groups = {RegisterNew.class})
    public String phone;

    /**
     * 用户邮箱
     */
    @NotNull(groups = {RegisterNew.class})
    public String email;

    /**
     * 密码
     */
    @NotNull(groups = {RegisterNew.class})
    @DontReturn
    public String password;

    /**
     * 用户城市
     */
    @NotNull(groups = {RegisterNew.class})
    public String city;

    /**
     * 性别，0女，1男
     */
    @NotNull(groups = {RegisterNew.class})
    public Integer sex;

    /**
     * 银行账号
     */
    @NotNull(groups = {RegisterNew.class})
    @Length(max = 16, min = 16, message = "银行卡号必须为16位数", groups = {RegisterNew.class})
    public String bankAccount;

    /**
     * 用户金额
     */
    public BigDecimal amount;

    /**
     * 用户积分，用户购买商品之后，1元消费额即可转换为1个积分。每100个积分可以抵扣1元现金。
     */
    public Integer credits;

    /**
     * 用户角色id
     */
    @NotNull(groups = {RegisterNew.class})
    public Integer roleId;


}
