
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
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
@TableName("store")
public class StorePO implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    @TableId(type = IdType.AUTO)
    public Integer id;

    /**
     * 店铺所属的商家id
     */
    public Integer merchantId;

    /**
     * 店铺名
     */
    public String name;

    /**
     * 营业执照图片
     */
    public String businessLicence;

    /**
     * 店铺等级id
     */
    public Integer levelId;

}
