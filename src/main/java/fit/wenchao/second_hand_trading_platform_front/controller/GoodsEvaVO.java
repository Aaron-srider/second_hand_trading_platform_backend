package fit.wenchao.second_hand_trading_platform_front.controller;

import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsEvaPO;
import fit.wenchao.second_hand_trading_platform_front.utils.MapName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Accessors(chain = true)
public class GoodsEvaVO extends GoodsEvaPO {

   public String consumerName;

    @MapName("count")
     public Integer boughtGoodsCount;

}
