
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

    private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    private Integer id;

    /**
     * 店铺所属的商家id
     */
    private Integer merchantId;

    /**
     * 店铺名
     */
    private String name;

    /**
     * 营业执照图片
     */
    private String businessLicence;

    /**
     * 店铺等级id
     */
    private Integer levelId;



}
