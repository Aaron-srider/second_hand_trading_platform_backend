
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 存储商品的类型
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
@TableName("goods_type")
public class GoodsTypePO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 商品类别
     */
    private String name;

    /**
     * 商品英文代号
     */
    private String code;



}
