
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

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 用户名称
     */
    public String goodsName;

    /**
     * 审批时间
     */
    public String approvalTime;

    /**
     * 店铺id
     */
    public Integer storeId;

    /**
     * 商品类别id
     */
    public Integer goodsType;

    /**
     * 商品折扣，保留2为小数
     */
    public BigDecimal discount;

    /**
     * 商品尺寸
     */
    public String size;

    /**
     * 商品原价
     */
    public BigDecimal amount;

    /**
     * 商品图片，json数组
     */
    public String picture;

    /**
     * 是否允许议价（0不允许，1允许，默认0）
     */
    public Boolean bargainOrNot;

    /**
     * 商品数量
     */
    public Integer num;

    /**
     * 商品新旧程度，1~9成
     */
    public Integer oldDegree;

    /**
     * 是否通过，-1默认，0不通过，1通过
     */
    public Integer permission;



}
