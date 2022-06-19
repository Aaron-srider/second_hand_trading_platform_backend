package fit.wenchao.second_hand_trading_platform_front.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fit.wenchao.second_hand_trading_platform_front.dao.po.StorePO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.StoreRegistApplicationPO;
import fit.wenchao.second_hand_trading_platform_front.dao.po.UserRolePO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.StoreDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.StoreRegistApplicationDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserDao;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.UserRoleDao;
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

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static fit.wenchao.utils.collection.SimpleFactories.ofList;

/**
 * <p>
 * 用户申请注册店铺时需要申请 前端控制器
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@Slf4j
public class StoreRegistApplicationController {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class BasicInfo {
        public String storeName;
        public String idcard;
        public Integer merchantId;
    }

    @Autowired
    StoreRegistApplicationDao storeRegistApplicationDao;

    @PostMapping(value = "/submitStoreApplication")
    @Transactional
    public JsonResult submitStoreApplication(@RequestPart("basicInfo") BasicInfo basicInfo,
                                             @RequestPart("attachment") MultipartFile attachment) throws IOException {
        log.info("basicInfo:{}", basicInfo);
        log.info("attachment:{}", attachment);

        OptionalUtils.nullable(basicInfo).filter((basicInfo1 -> basicInfo1.idcard != null &&
                basicInfo1.storeName != null &&
                basicInfo1.merchantId != null)).orElseThrow(() -> new BackendException(ResultCodeEnum.FRONT_PARAM_ERROR, null));


        List<StoreRegistApplicationPO> merchant_id = storeRegistApplicationDao.list(WrapperUtils.eq("merchant_id", basicInfo.merchantId));

        //一个人只能申请一个店铺
        if (merchant_id.size() > 0) {
            throw new BackendException(ResultCodeEnum.ONE_STORE_APPLICATION_ONE_USER, null);
        }

        StoreRegistApplicationPO storeRegistApplicationPO = new StoreRegistApplicationPO();

        storeRegistApplicationPO.storeName = basicInfo.storeName;
        storeRegistApplicationPO.idcard = basicInfo.idcard;
        Picture picture = new Picture();
        picture.base64Str = new String(Base64.getEncoder().encode(attachment.getBytes()));
        picture.no = "1";
        picture.name = attachment.getOriginalFilename();
        List<Picture> pictures = ofList(picture);

        storeRegistApplicationPO.businessLicence = JSONArray.toJSONString(pictures);
        storeRegistApplicationPO.merchantId = basicInfo.merchantId;
        storeRegistApplicationPO.permit = -1;

        storeRegistApplicationDao.save(storeRegistApplicationPO);

        log.info("insert application:{}", storeRegistApplicationPO);

        return JsonResult.ok(null);
    }


    public static class StoreRegistVO extends StoreRegistApplicationPO {
        public String merchantName;
        public String idcard;
        public String permitText;
        public String businessPicStr;
    }

    @Autowired
    UserDao userDao;

    @GetMapping("/getPageStoreApp")
    public JsonResult getPageStoreApp(Page<StoreRegistApplicationPO> page) {
        storeRegistApplicationDao.page(page);

        List<StoreRegistApplicationPO> records = page.getRecords();


        List<StoreRegistVO> resultList = records.stream().map((storeRegistApplicationPO -> {
            StoreRegistVO storeRegistVO = new StoreRegistVO();
            BeanUtils.copyProperties(storeRegistApplicationPO, storeRegistVO);
            Integer merchantId = storeRegistVO.merchantId;
            String merchantName = OptionalUtils.nullable(merchantId)
                    .map((merchantIdValue -> {
                        log.info("商家:{}", merchantIdValue);
                        return userDao.getById(merchantId);
                    })).map((merchantPOValue) -> {
                        log.info("商家名称:{}", merchantPOValue.name);
                        return merchantPOValue.name;
                    }).orElseThrow(() -> {
                        return new BackendException(ResultCodeEnum.DATA_ERROR, "商家数据异常");
                    });
            storeRegistVO.merchantName = merchantName;
            Integer permit = storeRegistApplicationPO.permit;
            String permitText = OptionalUtils.nullable(permit).map((permitValue) -> {
                if (permitValue == -1) {
                    return "未批";
                } else if (permitValue == 0) {
                    return "不通过";
                } else if (permitValue == 1) {
                    return "通过";
                }
                return null;
            }).orElseThrow(() -> {
                return new BackendException(ResultCodeEnum.DATA_ERROR, "审批状态数据异常");
            });

            storeRegistVO.permitText = permitText;


            String businessLicence = storeRegistApplicationPO.businessLicence;
            List<Picture> pictures = JSON.parseArray(businessLicence, Picture.class);
            String pictureBase64Str = OptionalUtils.nullable(pictures).filter((pictures1 -> pictures1.size() > 0))
                    .map(pictures1 -> pictures1.get(0))
                    .map((picture1 -> picture1.base64Str))
                    .orElseThrow(() -> {
                        return new BackendException(ResultCodeEnum.DATA_ERROR, "营业执照图片异常");
                    });

            storeRegistVO.businessPicStr = pictureBase64Str;
            return storeRegistVO;


        })).collect(Collectors.toList());

        Page page1 = new Page();
        page1.setSize(page.getSize());
        page1.setCurrent(page.getCurrent());
        page1.setTotal(page.getTotal());
        page1.setRecords(resultList);

        return JsonResult.ok(page1);
    }


    @Autowired
    StoreDao storeDao;

    @Autowired
    UserRoleDao userRoleDao;

    @PutMapping("/storeRegistPass")
    @Transactional
    public JsonResult storeRegistPass(@RequestBody Integer storeRegistApplicationId) {
        log.info("storeRegistApplicationId: {}", storeRegistApplicationId);

        StoreRegistApplicationPO storeRegistApplicationPO = new StoreRegistApplicationPO();
        storeRegistApplicationPO.id = storeRegistApplicationId;
        storeRegistApplicationPO.permit = 1;
        storeRegistApplicationDao.updateById(storeRegistApplicationPO);
        log.info("update storeRegistApplicationPO premit to : {}", storeRegistApplicationPO.permit);

        StoreRegistApplicationPO storeRegistApplicationPO1 = storeRegistApplicationDao.getById(storeRegistApplicationId);
        OptionalUtils.nullable(storeRegistApplicationPO1).map((storeRegistApplicationPO2 ->
                storeRegistApplicationPO2.merchantId)).map(merchantIdValue -> {
            return userDao.getById(merchantIdValue);
        }).map(merchant -> {
            merchant.roleId = 2;
            userDao.updateById(merchant);
            UserRolePO userRolePO = new UserRolePO();
            userRolePO.userId = merchant.id;
            userRolePO.roleId = 2;
            userRoleDao.save(userRolePO);
            log.info("add role :{} to user:{}", userRolePO.roleId, merchant);
            return merchant;
        }).orElseThrow(() -> {
            return new BackendException(ResultCodeEnum.DATA_ERROR, null);
        });


        StorePO storePO = new StorePO();
        storePO.name = storeRegistApplicationPO1.storeName;
        storePO.merchantId = storeRegistApplicationPO1.merchantId;
        storePO.businessLicence = storeRegistApplicationPO1.businessLicence;
        //默认等级为5
        storePO.levelId = 5;
        storeDao.save(storePO);
        log.info("新增店铺:{}", storePO);

        return JsonResult.ok(null);
    }

    @PutMapping("/storeRegistFail")
    @Transactional
    public JsonResult storeRegistFail(@RequestBody Integer storeRegistApplicationId) {
        log.info("storeRegistApplicationId: {}", storeRegistApplicationId);

        StoreRegistApplicationPO storeRegistApplicationPO = new StoreRegistApplicationPO();
        storeRegistApplicationPO.id = storeRegistApplicationId;
        storeRegistApplicationPO.permit = 0;
        storeRegistApplicationDao.updateById(storeRegistApplicationPO);
        log.info("update storeRegistApplicationPO premit to : {}", storeRegistApplicationPO.permit);

        return JsonResult.ok(null);
    }


}
