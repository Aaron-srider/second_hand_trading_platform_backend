
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品表
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
@TableName("goods")
public class GoodsPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 所属店铺id
     */
    private Integer storeId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品类型id
     */
    private Integer typeId;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品折扣
     */
    private BigDecimal discount;

    /**
     * 商品尺寸
     */
    private String size;

    /**
     * 商品图片，json数组
     */
    private String picture;

    /**
     * 使用说明
     */
    private String instru;

    /**
     * 是否允许议价
     */
    private Integer barginOrNot;

    /**
     * 商品新旧程度，1~9成
     */
    private Integer oldDegree;

    /**
     * 历史销量
     */
    private Integer historySales;

    /**
     * 现有库存
     */
    private Integer stockQuantity;

    /**
     * 是否上架，1上架，0下架
     */
    private Integer onShelf;

    /**
     * 商品好评度，每次评价应该更新
     */
    private BigDecimal favour;



}
