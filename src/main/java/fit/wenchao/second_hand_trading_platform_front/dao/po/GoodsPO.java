
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import fit.wenchao.second_hand_trading_platform_front.utils.Picture;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static fit.wenchao.utils.optional.OptionalUtils.nullable;

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

    public static final long serialVersionUID = 1L;

    public Integer id;

    /**
     * 所属店铺id
     */
    public Integer storeId;

    /**
     * 商品名称
     */
    public String goodsName;

    /**
     * 商品类型id
     */
    public Integer typeId;

    /**
     * 商品价格
     */
    public BigDecimal price;

    /**
     * 商品折扣
     */
    public BigDecimal discount;

    /**
     * 商品尺寸
     */
    public String size;

    /**
     * 商品图片，json数组
     */
    public String picture;

    /**
     * 使用说明
     */
    public String instru;

    /**
     * 是否允许议价
     */
    public Integer barginOrNot;

    /**
     * 商品新旧程度，1~9成
     */
    public Integer oldDegree;

    /**
     * 历史销量
     */
    public Integer historySales;

    /**
     * 现有库存
     */
    public Integer stockQuantity;

    /**
     * 是否上架，1上架，0下架
     */
    public Integer onShelf;

    /**
     * 商品好评度，每次评价应该更新
     */
    public BigDecimal favour;


    public List<Picture> pictureList() {
        return nullable(picture)
                .map((pictureValue) -> JSONArray.parseArray(picture, Picture.class))
                .orElse(null);
    }


}
