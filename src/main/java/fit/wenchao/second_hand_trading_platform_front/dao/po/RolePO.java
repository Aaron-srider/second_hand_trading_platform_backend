
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户角色

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
@TableName("`role`")
public class RolePO implements Serializable {

    public static final long serialVersionUID = 1L;

    public Integer id;

    /**
     * 角色名称
     */
    public String name;

    /**
     * 角色英文代码
     */
    public String code;



}
