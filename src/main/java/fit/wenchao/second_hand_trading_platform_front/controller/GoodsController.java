package fit.wenchao.second_hand_trading_platform_front.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.StorePO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.StoreDao;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.PageVo;
import fit.wenchao.second_hand_trading_platform_front.utils.Picture;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils.eq;
import static fit.wenchao.utils.optional.OptionalUtils.nullable;

/**
 * <p>
 * 商品表 前端控制器`
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@Slf4j
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    StoreDao storeDao;

    @GetMapping
    public JsonResult getGoodsPage(
            Page<GoodsPO> page,
            String goodsName,
            String sortPolicy) {

        QueryWrapper<GoodsPO> queryWrapper = new QueryWrapper<>();

        if (!"".equals(goodsName)) {
            queryWrapper.like("goods_name", goodsName);
        }

        if ("price-asc".equals(sortPolicy)) {
            queryWrapper.orderByAsc("price");
        } else if ("price-desc".equals(sortPolicy)) {
            queryWrapper.orderByDesc("price");
        } else if ("sales".equals(sortPolicy)) {
            queryWrapper.orderByDesc("history_sales");
        } else if ("favour".equals(sortPolicy)) {
            queryWrapper.orderByDesc("favour");
        }

        goodsDao.page(page, queryWrapper);

        List<GoodsPO> goodsPOList = nullable(page)
                .map(Page::getRecords)
                .orElseThrow(() -> new BackendException(ResultCodeEnum.UNKNOWN_ERROR, null));

        List<GoodsVo> result = goodsPOList.stream()
                .map((goodsPO -> {
                    GoodsVo goodsVo = GoodsVo.builder().build();
                    BeanUtils.copyProperties(goodsPO, goodsVo);
                    List<Picture> pictures = goodsPO.pictureList();
                    goodsVo.setPicture(null).setPicList(pictures);
                    StorePO storeWhichGoodsBelong2 = storeDao.getOne(eq("id", goodsPO.getStoreId()));
                    String storeName = nullable(storeWhichGoodsBelong2)
                            .map(StorePO::getName)
                            .orElse(null);
                    goodsVo.setStoreName(storeName);
                    return goodsVo;
                })).collect(Collectors.toList());

        PageVo<GoodsVo> build = PageVo.<GoodsVo>builder()
                .pageNo(Math.toIntExact(page.getCurrent()))
                .pageSize(Math.toIntExact(page.getSize()))
                .pageData(result)
                .total(Math.toIntExact(page.getTotal()))
                .build();
        return JsonResult.ok(build);
    }
}
