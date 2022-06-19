package fit.wenchao.second_hand_trading_platform_front.controller;

import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsPO;
import fit.wenchao.second_hand_trading_platform_front.utils.MapName;
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

public class GoodsVo extends GoodsPO {

    /**
     * 店铺名
     */
    public String storeName;

    /**
     * 商品图片列表
     */
    @MapName("pictureList")
    public List<Picture> picList;

}
