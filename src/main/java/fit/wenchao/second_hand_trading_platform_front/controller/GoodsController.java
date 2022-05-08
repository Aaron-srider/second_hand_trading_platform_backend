package fit.wenchao.second_hand_trading_platform_front.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.RegistApplicationPO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.StoreDao;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.PageVo;
import fit.wenchao.second_hand_trading_platform_front.utils.Picture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils.eq;

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
    public JsonResult getGoodsPage(Integer pageSize,
                                   Integer pageNo,
                                   String goodsName,
                                   String sortPolicy) {
        log.info("pageSize:{} pageNo:{} goodsName:{}", pageSize, pageNo, goodsName);
        Page<GoodsPO> objectPage = new Page<>();
        objectPage.setCurrent(pageNo);
        objectPage.setSize(pageSize);
        Page<GoodsPO> page;

        if (goodsName != null && !goodsName.equals("")) {
            QueryWrapper<GoodsPO> queryWrapper = new QueryWrapper<GoodsPO>()
                    .like("goods_name", goodsName);
            if ("price-asc".equals(sortPolicy)) {
                queryWrapper.orderByAsc("price");
            } else if ("price-desc".equals(sortPolicy)) {
                queryWrapper.orderByDesc("price");
            } else if ("sales".equals(sortPolicy)) {
                queryWrapper.orderByDesc("history_sales");
            } else if ("favour".equals(sortPolicy)) {
                queryWrapper.orderByDesc("favour");
            }
            page = goodsDao.page(objectPage, queryWrapper);
        } else {
            QueryWrapper<GoodsPO> queryWrapper = new QueryWrapper<GoodsPO>();
            if ("price-asc".equals(sortPolicy)) {
                queryWrapper.orderByAsc("price");
            } else if ("price-desc".equals(sortPolicy)) {
                queryWrapper.orderByDesc("price");
            } else if ("sales".equals(sortPolicy)) {
                queryWrapper.orderByDesc("history_sales");
            } else if ("favour".equals(sortPolicy)) {
                queryWrapper.orderByDesc("favour");
            }
            page = goodsDao.page(objectPage, queryWrapper);
        }

        Long total = page.getTotal();
        List<GoodsPO> list = page.getRecords();

        List<GoodsVo> result = list.stream().map((goodsPO -> {
            String picture = goodsPO.getPicture();
            List<Picture> picList = JSONArray.parseArray(picture, Picture.class);

            GoodsVo build = GoodsVo.builder().build();
            BeanUtils.copyProperties(goodsPO, build);
            build.setPicture(null).setPicList(picList);
            String storeName = storeDao.getOne(eq("id", goodsPO.getStoreId())).getName();
            build.setStoreName(storeName);

            return build;
        })).collect(Collectors.toList());


        PageVo<GoodsVo> build = PageVo.<GoodsVo>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .pageData(result)
                .total(Math.toIntExact(total))
                .build();
        return JsonResult.ok(build);
    }
}
