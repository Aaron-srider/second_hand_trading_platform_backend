package fit.wenchao.second_hand_trading_platform_front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fit.wenchao.second_hand_trading_platform_front.dao.po.RegistApplicationPO;
import fit.wenchao.second_hand_trading_platform_front.dao.repo.RegistApplicationDao;
import fit.wenchao.second_hand_trading_platform_front.service.RegistApplicationService;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.PageVo;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户注册的申请存在该表中，由管理员审批 前端控制器
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */
@RestController
@CrossOrigin(allowCredentials = "true")
@Slf4j
public class RegistApplicationController {

    @Autowired
    RegistApplicationDao registApplicationDao;

    @Autowired
    RegistApplicationService registApplicationService;

    @PutMapping("/registerPass")
    public JsonResult registerPass(@RequestBody Integer registerApplicationId) throws Exception {
        registApplicationService.pass(registerApplicationId);
        return JsonResult.ok(ResultCodeEnum.SUCCESS);
    }

    @GetMapping("/registApplications")
    public JsonResult registApplications(Integer pageSize, Integer pageNo) {
        log.info("pageSize:{} pageNo:{}",pageSize, pageNo);
        List<RegistApplicationPO> registApplicationPOList =  registApplicationDao.getPage(pageSize, pageNo);


        QueryWrapper<RegistApplicationPO> queryWrapper = new QueryWrapper<RegistApplicationPO>();

        queryWrapper.select("count(*) as total");
        RegistApplicationPO registApplicationPOTotal = registApplicationDao.getOne(queryWrapper);
        Integer total = registApplicationPOTotal.getTotal();
        PageVo<RegistApplicationPO> registApplicationPOPage = new PageVo<>();



        registApplicationPOList.forEach((regist) -> {
            if(regist.getPermission()==null) {
                regist.setPermission((byte) -1);
            }
        });

        registApplicationPOPage.setTotal(total);
        registApplicationPOPage.setPageData(registApplicationPOList);
        registApplicationPOPage.setPageSize(pageSize);
        registApplicationPOPage.setPageNo(pageNo);


        return JsonResult.ok(registApplicationPOPage);


    }

}
