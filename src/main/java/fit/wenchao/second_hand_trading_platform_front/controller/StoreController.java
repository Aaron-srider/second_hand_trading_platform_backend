package fit.wenchao.second_hand_trading_platform_front.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fit.wenchao.second_hand_trading_platform_front.dao.po.*;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.*;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.Picture;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils;
import fit.wenchao.utils.optional.OptionalUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.Query;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils.eq;
import static fit.wenchao.utils.collection.SimpleFactories.ofJson;
import static fit.wenchao.utils.optional.OptionalUtils.nullable;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@Slf4j
public class StoreController {

    public static class StoreVO extends StorePO {
        public String businessLicencePicStr;
        public Integer levelNum;
    }

    @Autowired
    StoreLevelDao storeLevelDao;

    @Autowired
    StoreDao storeDao;

    @GetMapping("/getStoreInfo")
    public JsonResult getStoreInfo(Integer merchantId) {
        log.info("merchantId:{}", merchantId);
        OptionalUtils.nullable(merchantId).orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR, null));
        StorePO storePO = storeDao.getOne(eq("merchant_id", merchantId));

        StoreVO storeVO1 = OptionalUtils.nullable(storePO).map(storePO1 -> {
            StoreVO storeVO = new StoreVO();
            BeanUtils.copyProperties(storePO, storeVO);
            Integer levelId = storePO1.levelId;
            StoreLevelPO storeLevelPO = storeLevelDao.getById(levelId);

            storeVO.levelNum = storeLevelPO.getStoreLevel();

            String businessLicence = storePO1.businessLicence;
            String businessLicencePicStr = OptionalUtils.nullable(businessLicence).map((businessLicenceValue) -> {
                        List<Picture> pictures =
                                JSON.parseArray(businessLicence, Picture.class);
                        return pictures;
                    }).filter((pictures -> {
                        return pictures.size() == 1;
                    })).map((pictures -> {
                        return pictures.get(0);
                    })).map((picture -> picture.getBase64Str()))
                    .orElseThrow(() -> {
                        return new BackendException(ResultCodeEnum.DATA_ERROR, "营业执照图片异常");
                    });

            storeVO.businessLicencePicStr = businessLicencePicStr;

            return storeVO;

        }).orElseThrow(() -> {
            return new BackendException(ResultCodeEnum.NOT_HAS_STORE, "该用户没有商铺");
        });

        return JsonResult.ok(storeVO1);
    }


    @Autowired
    GoodsDao goodsDao;

    public static class StoreGoodsVO extends GoodsPO {
        public String typeName;
        public String priceText;
        public String discountText;
        public String barginOrNotText;
        public String oldDegreeText;
        public String onShelfText;
    }


    @Autowired
    GoodsTypeDao goodsTypeDao;

    @GetMapping("/getStoreGoods")
    public JsonResult getStoreGoods(Page<GoodsPO> page, Integer storeId) {
        OptionalUtils.nullable(storeId).orElseThrow(() -> {
            return new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR, null);
        });
        goodsDao.page(page, eq("store_id", storeId));
        List<GoodsPO> records =
                page.getRecords();

        List<StoreGoodsVO> storeGoodsVOS = OptionalUtils.nullable(records).map(goodsPOS -> {
            List<StoreGoodsVO> storeGoodsVOList = goodsPOS.stream().map(goodsPO -> {
                StoreGoodsVO storeGoodsVO = new StoreGoodsVO();
                BeanUtils.copyProperties(goodsPO, storeGoodsVO);
                storeGoodsVO.barginOrNotText = (goodsPO.barginOrNot == 1 ? "是" : "否");

                Integer typeId = goodsPO.typeId;
                OptionalUtils.nullable(typeId).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR,
                        "goodsType data error"));
                GoodsTypePO goodsTypePO = goodsTypeDao.getById(typeId);
                storeGoodsVO.typeName = OptionalUtils.nullable(goodsTypePO).
                        map(GoodsTypePO::getName).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR,
                                "goodsType data error"));

                String priceText = goodsPO.price.setScale(2, RoundingMode.DOWN).toString();
                storeGoodsVO.priceText = priceText;

                String discountText = "";
                BigDecimal bigDecimal = goodsPO.discount.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.DOWN);
                int i = bigDecimal.intValue();
                if (i == 100) {
                    discountText = "无折扣";
                } else {
                    discountText = goodsPO.discount.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.DOWN).toString()
                            + "折";
                }

                storeGoodsVO.discountText = discountText;

                String oldDegreeText = goodsPO.oldDegree + "成";

                storeGoodsVO.oldDegreeText = oldDegreeText;

                storeGoodsVO.onShelfText = goodsPO.onShelf == 1 ? "是" : "否";

                return storeGoodsVO;


            }).collect(Collectors.toList());

            return storeGoodsVOList;
        }).orElse(new ArrayList<>());

        Page<StoreGoodsVO> storeGoodsVOPage = new Page<>();
        storeGoodsVOPage.setRecords(storeGoodsVOS);
        storeGoodsVOPage.setTotal(page.getTotal());
        storeGoodsVOPage.setSize(page.getSize());
        storeGoodsVOPage.setCurrent(page.getCurrent());

        return JsonResult.ok(storeGoodsVOPage);
    }


    @PutMapping("/downShelf")
    @Transactional
    public JsonResult downShelf(@RequestBody Map<String, Integer> goodsIdMap) {
        Integer goodsId = goodsIdMap.get("goodsId");
        log.info("下架商品：{}", goodsId);

        OptionalUtils.nullable(goodsId).orElseThrow(() -> new BackendException(
                ResultCodeEnum.FRONT_PARAM_ERROR, null
        ));

        GoodsPO goodsPO = goodsDao.getById(goodsId);
        OptionalUtils.nullable(goodsPO)
                .map(goodsPO1 -> {
                    goodsPO1.onShelf = 0;
                    goodsDao.updateById(goodsPO1);
                    log.info("update onshelf status of goods:{}",
                            goodsId);
                    return goodsPO1;
                }).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR,
                        "商品 " + goodsId + " 不存在"));
        return JsonResult.ok(null);
    }


    @PutMapping("/upShelf")
    @Transactional
    public JsonResult upShelf(@RequestBody Map<String, Integer> goodsIdMap) {
        Integer goodsId = goodsIdMap.get("goodsId");
        log.info("上架商品：{}", goodsId);

        OptionalUtils.nullable(goodsId).orElseThrow(() -> new BackendException(
                ResultCodeEnum.FRONT_PARAM_ERROR, null
        ));

        GoodsPO goodsPO = goodsDao.getById(goodsId);
        OptionalUtils.nullable(goodsPO)
                .map(goodsPO1 -> {
                    goodsPO1.onShelf = 1;
                    goodsDao.updateById(goodsPO1);
                    log.info("update onshelf status of goods:{}",
                            goodsId);
                    return goodsPO1;
                }).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR,
                        "商品 " + goodsId + " 不存在"));
        return JsonResult.ok(null);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class GoodsPubApp {
        public String goodsName;
        public Integer goodsType;
        public BigDecimal discount;
        public String size;
        public BigDecimal amount;
        public Integer barginOrNot;
        public Integer num;
        public Integer storeId;
        public Integer oldDegree;
    }


    @Autowired
    GoodsPubApplicationDao goodsPubApplicationDao;

    @PostMapping("/submitGoodsPubApplication")
    public JsonResult submitGoodsPubApplication(
            @RequestPart("basicInfo") GoodsPubApp basicInfo,
            @RequestPart("attachment") MultipartFile[] attachments) {
        log.info("pub new goods : {}", basicInfo);

        log.info("goods pic num:{}", attachments.length);


        AtomicInteger count = new AtomicInteger();
        List<Picture> pictures = Arrays.stream(attachments).map((pic) -> {
            try {
                byte[] bytes = pic.getBytes();
                byte[] encode = Base64.getEncoder().encode(bytes);
                String base64Str = new String(encode);
                Picture picture = new Picture();
                picture.base64Str = base64Str;
                picture.no = String.valueOf(count.getAndIncrement());
                picture.name = pic.getOriginalFilename();
                return picture;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        String picStr = JSONArray.toJSONString(pictures);


        GoodsPubApplicationPO goodsPubApplicationPO = new GoodsPubApplicationPO();

        BeanUtils.copyProperties(basicInfo, goodsPubApplicationPO);
        goodsPubApplicationPO.picture = picStr;
        goodsPubApplicationPO.permission = -1;

        goodsPubApplicationDao.save(goodsPubApplicationPO);

        log.info("save goodsPubApp:{}", goodsPubApplicationPO);


        return JsonResult.ok(null);
    }

    public static class GoodsPubApplicationVO extends GoodsPubApplicationPO {
        public String goodsTypeText;
        public String barginOrNotText;
        public String oldDegreeText;
        public String storeName;
        public List<Picture> pictureList;
        public String permissionText;
        //public String amount;
        //public String discount;
    }

    public GoodsPubApplicationVO convertGoodsPubAppPO2VO(GoodsPubApplicationPO goodsPubApplicationPO) {
        GoodsPubApplicationVO goodsPubApplicationVO = new GoodsPubApplicationVO();
        BeanUtils.copyProperties(goodsPubApplicationPO, goodsPubApplicationVO)
        ;
        List<GoodsTypePO> list = goodsTypeDao.list();
        GoodsTypePO goodsTypePO = list.stream().filter((type) -> {
            return Objects.equals(type.getId(), goodsPubApplicationPO.goodsType);
        }).findFirst().orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, null));

        goodsPubApplicationVO.goodsTypeText = goodsTypePO.getName();
        goodsPubApplicationVO.oldDegreeText = goodsPubApplicationPO.oldDegree + "成";
        goodsPubApplicationVO.barginOrNotText = goodsPubApplicationPO.bargainOrNot ? "是" : "否";
        //goodsPubApplicationVO.amount = goodsPubApplicationPO.
        Integer storeId = goodsPubApplicationPO.storeId;
        goodsPubApplicationVO.storeName = nullable(storeId).map((storeIdValue) -> {


            Integer permission = goodsPubApplicationPO.permission;
            switch (permission) {
                case -1:
                    goodsPubApplicationVO.permissionText = "未审批";
                    break;
                case 0:
                    goodsPubApplicationVO.permissionText = "不通过";
                    break;
                case 1:
                    goodsPubApplicationVO.permissionText = "通过";
                    break;
            }

            return storeDao.getById(storeId);
        }).map((storePO -> {
            return storePO.name;
        })).orElse("");

        goodsPubApplicationVO.pictureList = JSONArray.parseArray(goodsPubApplicationPO.picture, Picture.class);


        return goodsPubApplicationVO;
    }

    @GetMapping("/getGoodsAppList")
    public JsonResult getGoodsAppList(Page<GoodsPubApplicationPO> page) {
        log.info("page request:{}", page);


        List<GoodsPubApplicationPO> records = goodsPubApplicationDao.page(page).getRecords();

        List<GoodsPubApplicationVO> collect = records.stream()
                .map(this::convertGoodsPubAppPO2VO)
                .collect(Collectors.toList());
        Page<GoodsPubApplicationVO> goodsPubApplicationVOPage = new Page<>();
        goodsPubApplicationVOPage.setCurrent(page.getCurrent());
        goodsPubApplicationVOPage.setSize(page.getSize());
        goodsPubApplicationVOPage.setRecords(collect);
        goodsPubApplicationVOPage.setTotal(page.getTotal());

        return JsonResult.ok(goodsPubApplicationVOPage);
    }


    @GetMapping("/getGoodsPubAppById")
    public JsonResult getGoodsPubAppById(Integer goodsPubAppId) {
        log.info("get goodsPubApp:{}", goodsPubAppId);
        nullable(goodsPubAppId).orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR, null));

        List<GoodsPubApplicationPO> goodsPubApplicationPOList = goodsPubApplicationDao.list(eq("id", goodsPubAppId));

        GoodsPubApplicationVO goodsPubApplicationVO = goodsPubApplicationPOList.stream().findFirst()
                .map(this::convertGoodsPubAppPO2VO)
                .orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, null));

        return JsonResult.ok(goodsPubApplicationVO);
    }

    @Transactional
    @PutMapping("/goodsPubPass")
    public JsonResult goodsPubPass(@RequestBody Map<String, Integer> goodsPubAppIdMap) {
        Integer goodsPubAppId = goodsPubAppIdMap.get("goodsPubAppId");
        nullable(goodsPubAppId).orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR,
                null));

        List<GoodsPubApplicationPO> goodsPubApplicationPOList = goodsPubApplicationDao.list(eq("id", goodsPubAppId));
        GoodsPubApplicationPO goodsPubApplicationPO = goodsPubApplicationPOList.stream().findFirst().orElseThrow(
                () -> new BackendException(ResultCodeEnum.DATA_ERROR, null));

        goodsPubApplicationPO.permission = 1;

        goodsPubApplicationDao.updateById(goodsPubApplicationPO);
        log.info("pass goods pub app:{}", goodsPubApplicationPO);

        GoodsPO goodsPO = new GoodsPO();
        BeanUtils.copyProperties(goodsPubApplicationPO, goodsPO);
        goodsPO.id = null;
        goodsPO.typeId = goodsPubApplicationPO.goodsType;
        goodsPO.price = goodsPubApplicationPO.amount;
        goodsPO.barginOrNot = goodsPubApplicationPO.bargainOrNot ? 1 : 0;
        goodsPO.historySales = 0;
        goodsPO.stockQuantity = goodsPubApplicationPO.num;
        goodsPO.onShelf = 1;
        goodsDao.save(goodsPO);
        log.info("new goods:{}", goodsPO);

        return JsonResult.ok(null);
    }


    @Transactional
    @PutMapping("/goodsPubFail")
    public JsonResult goodsPubFail(@RequestBody Map<String, Integer> goodsPubAppIdMap) {
        Integer goodsPubAppId = goodsPubAppIdMap.get("goodsPubAppId");
        nullable(goodsPubAppId).orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR,
                null));

        List<GoodsPubApplicationPO> goodsPubApplicationPOList = goodsPubApplicationDao.list(eq("id", goodsPubAppId));
        GoodsPubApplicationPO goodsPubApplicationPO = goodsPubApplicationPOList.stream().findFirst().orElseThrow(
                () -> new BackendException(ResultCodeEnum.DATA_ERROR, null));

        goodsPubApplicationPO.permission = 0;
        goodsPubApplicationPO.picture = null;

        goodsPubApplicationDao.updateById(goodsPubApplicationPO);

        log.info("fail goods pub app:{}", goodsPubApplicationPO);
        return JsonResult.ok(null);
    }


    @Autowired
    UserDao userDao;

    @GetMapping("/getStoreId")
    public JsonResult getStoreId(Integer userId) {
        log.info("userId:{}", userId);
        UserPO byId = userDao.getById(userId);
        nullable(byId).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, "用户不存在"));
        List<StorePO> merchant_id = storeDao.list(eq("merchant_id", byId.id));
        if (merchant_id.size() > 1) {
            throw new BackendException(ResultCodeEnum.ONE_STORE_APPLICATION_ONE_USER
                    , null);
        }

        return merchant_id.stream().findFirst()
                .map(storePO -> JsonResult.ok(storePO.id)).orElse(null);
    }

    @Autowired
    OrderDao orderDao;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class ShipGoodsVO {
        public String goodsName;
        public String goodsTypeText;
        public Integer buyCount;
        public String orderId;
        public String goodsPrice;
        public String discount;
        public String totalPrice;
        public String address;
        public String consumerName;
        public String phone;
    }

    @Autowired
    ReceiveGoodsAddressDao receiveGoodsAddressDao;


    @GetMapping("/getShipGoods")
    public JsonResult getShipGoods(Page<OrderPO> page, Integer storeId) {

        log.info("storeId:{}", storeId);

        List<GoodsPO> storeGoodsList = goodsDao.list(eq("store_id", storeId));

        List<Integer> goodsIdList = storeGoodsList.stream()
                .map((goodsPO -> goodsPO.id)).collect(Collectors.toList());
        log.info("store goodsIdList:{}", goodsIdList);

        if (goodsIdList.size() <= 0) {
            Page<ShipGoodsVO> shipGoodsVOPage = new Page<>();
            shipGoodsVOPage.setRecords(new ArrayList<>())
                    .setTotal(page.getTotal())
                    .setSize(page.getSize())
                    .setCurrent(page.getCurrent())
            ;

            log.info("return shipGoodsPage:{}", shipGoodsVOPage);
            return JsonResult.ok(shipGoodsVOPage);
        }

        orderDao.page(page, WrapperUtils
                .<OrderPO>eq("order_status_id", 2)
                .in("goods_id", goodsIdList)
        );

        List<OrderPO> records = page.getRecords();

        List<ShipGoodsVO> address_id = records.stream().map((orderPO -> {
            ShipGoodsVO shipGoodsVO = new ShipGoodsVO();

            GoodsPO goodsDaoById = goodsDao.getById(orderPO.goodsId);
            shipGoodsVO.goodsName = nullable(goodsDaoById).map((goodsPO -> {
                return goodsPO.goodsName;
            })).orElse(null);

            shipGoodsVO.goodsTypeText = nullable(goodsDaoById).map((goodsPO -> {
                        return goodsPO.typeId;
                    })).map((typeIdValue) -> {
                        return goodsTypeDao.getById(typeIdValue);
                    }).map((GoodsTypePO::getName))
                    .orElse(null);

            shipGoodsVO.buyCount = orderPO.count;

            shipGoodsVO.orderId = orderPO.orderId;

            shipGoodsVO.goodsPrice = orderPO.price.setScale(2, RoundingMode.DOWN)
                    .toString();

            shipGoodsVO.totalPrice = orderPO.totalPrice.setScale(2, RoundingMode.DOWN)
                    .toString();

            shipGoodsVO.discount = orderPO.discount.setScale(2, RoundingMode.DOWN)
                    .toString();
            String address = receiveGoodsAddressDao.
                    list(eq("id", orderPO.addressId)).stream().findFirst()
                    .map((receiveGoodsAddressPO -> receiveGoodsAddressPO.province
                            + " " + receiveGoodsAddressPO.city + " "
                            + receiveGoodsAddressPO.detail)).orElse(null);
            shipGoodsVO.address = address;

            nullable(userDao.getById(orderPO.consumerId))
                    .ifPresent((userPO -> {
                        shipGoodsVO.consumerName = userPO.name;
                        shipGoodsVO.phone = userPO.phone;
                    }));
            return shipGoodsVO;

        })).collect(Collectors.toList());


        Page<ShipGoodsVO> shipGoodsVOPage = new Page<>();
        shipGoodsVOPage.setRecords(address_id)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent())
        ;

        log.info("return shipGoodsPage:{}", shipGoodsVOPage);
        return JsonResult.ok(shipGoodsVOPage);
    }


    @PutMapping("/deliverGoods")
    @Transactional
    public JsonResult deliverGoods(@RequestBody String orderId) {
        log.info("deliver goods orderId:{}", orderId);

        OrderPO orderPO = orderDao.getOne(eq("order_id", orderId));

        nullable(orderPO).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR,
                "order does not exists"));

        orderPO.orderStatusId = 5;

        orderDao.updateById(orderPO);
        log.info("update order status to 5 of order_id:{}", orderId);

        return JsonResult.ok(ofJson("orderId", orderId));
    }


}
