package fit.wenchao.second_hand_trading_platform_front.controller;

import fit.wenchao.second_hand_trading_platform_front.utils.Picture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class GoodsVo {

    private Integer id;

    /**
     * 所属店铺id
     */
    private Integer storeId;

    /**
     * 店铺名
     */
    private String storeName;

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
     * 商品图片列表
     */
    private List<Picture> picList;

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
}
