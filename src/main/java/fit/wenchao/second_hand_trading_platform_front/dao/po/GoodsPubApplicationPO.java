
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 存储商家的发布商品申请，有管理员审批
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
@TableName("goods_pub_application")
public class GoodsPubApplicationPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名称
     */
    private String goodsName;

    /**
     * 审批时间
     */
    private String approvalTime;

    /**
     * 店铺id
     */
    private Integer storeId;

    /**
     * 商品类别id
     */
    private Integer goodsType;

    /**
     * 商品折扣，保留2为小数
     */
    private BigDecimal discount;

    /**
     * 商品尺寸
     */
    private String size;

    /**
     * 商品原价
     */
    private BigDecimal amount;

    /**
     * 商品图片，json数组
     */
    private String picture;

    /**
     * 是否允许议价（0不允许，1允许，默认0）
     */
    private Boolean bargainOrNot;

    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 商品新旧程度，1~9成
     */
    private Integer oldDegree;

    /**
     * 是否通过，-1默认，0不通过，1通过
     */
    private Boolean permission;



}
