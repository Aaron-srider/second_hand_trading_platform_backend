package fit.wenchao.second_hand_trading_platform_front.utils.verifyCode;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import fit.wenchao.second_hand_trading_platform_front.vo.VeriyfyCodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Enumeration;

import static fit.wenchao.utils.string.StrUtils.ft;

@Controller
@Slf4j
@CrossOrigin(allowCredentials = "true")
public class CaptchaController {

    @Autowired
    private Producer captchaProducer;

    @GetMapping("/verifyCode")
    @ResponseBody
    public JsonResult verifyCode(HttpServletRequest request, String code) {
        String codeInSession = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        System.out.println("session:" + request.getSession());
        System.out.println("sessionid:" + request.getSession().getId());
        System.out.println("session new:" + request.getSession().isNew());
        if (code == null || !code.equals(codeInSession)) {
            String failMsg = ft("verify code failed,front code:{}, session code:{}", code, codeInSession);
            log.info(failMsg);
            return JsonResult.getInstance(null, ResultCodeEnum.VERIFY_ERROR);
        }

        log.info("verify code success,front code:{}, session code:{}", code, codeInSession);
        return JsonResult.getInstance(null, ResultCodeEnum.SUCCESS);
    }

    @RequestMapping("/kaptcha/getKaptchaImage")
    @ResponseBody
    public JsonResult getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        System.out.println("session:" + request.getSession());
        System.out.println("sessionid:" + request.getSession().getId());
        System.out.println("session new:" + request.getSession().isNew());

        setNoCacheHeaders(response);

        // create the text for the image
        String capText = captchaProducer.createText();
        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        log.info("生成验证码并存入session：{},{}", Constants.KAPTCHA_SESSION_KEY, capText);


        log.info("session 中的信息：");

        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String s = attributeNames.nextElement();
            System.out.println(s + "：" + session.getAttribute(s));
        }

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        log.info("生成验证码图片");

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
            // write the data out
            ImageIO.write(bi, "jpg", byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String base64Str = Base64.getEncoder().encodeToString(bytes);
            return JsonResult.ok(base64Str);
        }

    }


    private void setNoCacheHeaders(HttpServletResponse response) {
        //response.setDateHeader("Expires", 0);

        //// Set standard HTTP/1.1 no-cache headers.
        //response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        //
        //// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        //response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        //
        //// Set standard HTTP/1.0 no-cache header.
        //response.setHeader("Pragma", "no-cache");
    }
}
