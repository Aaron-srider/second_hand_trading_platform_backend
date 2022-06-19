package fit.wenchao.second_hand_trading_platform_front.controller;


import fit.wenchao.second_hand_trading_platform_front.dao.po.ReceiveGoodsAddressPO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.ReceiveGoodsAddressDao;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import fit.wenchao.utils.optional.OptionalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static fit.wenchao.second_hand_trading_platform_front.utils.MapFieldName.mapFieldName;
import static fit.wenchao.second_hand_trading_platform_front.utils.MapFieldName.mapList;
import static fit.wenchao.second_hand_trading_platform_front.utils.WrapperUtils.eq;

@CrossOrigin(allowCredentials = "true")
@RestController
@Slf4j
public class ReceiveGoodsAddressController {

    @Autowired
    ReceiveGoodsAddressDao receiveGoodsAddressDao;

    @GetMapping("/receiveGoodsAddress")
    public JsonResult getReceiveGoodsAddressByUserId(Integer userId) {
        List<ReceiveGoodsAddressPO> receiveGoodsAddressPOList = receiveGoodsAddressDao.list(eq("user_id", userId));
        System.out.println(receiveGoodsAddressPOList);
        ;
        return JsonResult.ok(mapList(receiveGoodsAddressPOList));
    }


    @PutMapping("/addAddress")
    @Transactional
    public JsonResult addAddress(@RequestBody ReceiveGoodsAddressPO address
    ) {
        log.info("add address:{} ", address);

        List<ReceiveGoodsAddressPO> user_id = receiveGoodsAddressDao.list(eq("user_id", address.userId));
        if (user_id.size() >= 5) {
            throw new BackendException(ResultCodeEnum.ADDRESS_MAX, "地址不能超过5个");
        }

        if (user_id.size() == 0) {
            address.ifDefaultAddress = true;
        } else {
            address.ifDefaultAddress = false;
        }


        receiveGoodsAddressDao.save(address);
        log.info("save address:{}", address);

        return JsonResult.ok(null);
    }


    @PutMapping("/setDefaultAddress")
    @Transactional
    public JsonResult setDefaultAddress(@RequestBody Integer addressId) {
        log.info("set default for address:{}", addressId);
        ReceiveGoodsAddressPO receiveGoodsAddressPO = new ReceiveGoodsAddressPO();
        receiveGoodsAddressPO.id = addressId;
        receiveGoodsAddressPO.ifDefaultAddress = true;
        receiveGoodsAddressDao.updateById(receiveGoodsAddressPO);
        log.info("update address default to true:{}", receiveGoodsAddressPO);

        ReceiveGoodsAddressPO receiveGoodsAddressPO1 = receiveGoodsAddressDao.getById(receiveGoodsAddressPO.id);
        String userId = receiveGoodsAddressPO1.userId;
        List<ReceiveGoodsAddressPO> user_id = receiveGoodsAddressDao.list(eq("user_id", userId));

        user_id.stream().filter((address -> {
            if (!address.id.equals(addressId) && address.ifDefaultAddress) {
                return true;
            }
            return false;
        })).forEach((address -> {
            address.ifDefaultAddress = false;
            receiveGoodsAddressDao.updateById(address);
            log.info("update address default to false:{}", receiveGoodsAddressPO);
        }));

        return JsonResult.ok(null);
    }


    @DeleteMapping("/deleteAddress")
    @Transactional
    public JsonResult deleteAddress(@RequestBody Integer addressId) {
        ReceiveGoodsAddressPO byId = receiveGoodsAddressDao.getById(addressId);

        OptionalUtils.nullable(byId).map((byIdValue) -> {
            if (byIdValue.ifDefaultAddress) {
                throw new BackendException(
                        ResultCodeEnum.CANNOT_DEL_DEFAULT_ADDRESS,
                        null);
            }
            return byIdValue;
        }).orElseThrow(() -> new BackendException(ResultCodeEnum.DATA_ERROR, "地址不存在"));

        receiveGoodsAddressDao.removeById(addressId);
        return JsonResult.ok(null);
    }


}
