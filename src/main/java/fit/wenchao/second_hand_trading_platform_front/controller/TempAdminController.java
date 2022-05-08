package fit.wenchao.second_hand_trading_platform_front.controller;

import com.alibaba.fastjson.JSONObject;
import fit.wenchao.second_hand_trading_platform_front.dao.po.GoodsPO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.GoodsDao;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static fit.wenchao.utils.collection.SimpleFactories.ofJson;
import static java.util.Arrays.asList;


@Slf4j
@CrossOrigin(allowCredentials = "true")
@RestController
public class TempAdminController {
    @Autowired
    GoodsDao goodsDao;
    @PostMapping("/tempAdmin/uploadGoodPic/{goodId}")
    public JsonResult uploadAttachment(
            @RequestPart("attachment") MultipartFile[] attachments
            , @PathVariable(name = "goodId") Integer goodId
    ) throws IOException {
        log.info("files: {}", asList(attachments));

        if(attachments.length == 0) {
            return JsonResult.getInstance(null, ResultCodeEnum.FRONT_PARAM_ERROR);
        }

        AtomicInteger count = new AtomicInteger();
        List<JSONObject> list = Arrays.stream(attachments).map(file -> {
            log.info("process pic:{}", file.getOriginalFilename());
            try {
                byte[] encode = Base64.getEncoder().encode(file.getBytes());
                String s = new String(encode);

                return ofJson("no", count.getAndIncrement(),
                        "name", file.getOriginalFilename(),
                        "base64Str", s);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }).collect(Collectors.toList());


        GoodsPO goodsPO = GoodsPO.builder().id(goodId).picture(JSONObject.toJSONString(list))
                .build();

        log.info("update good:{}", goodsPO);

        goodsDao.updateById(goodsPO);

        return JsonResult.ok(null);


    }
}
