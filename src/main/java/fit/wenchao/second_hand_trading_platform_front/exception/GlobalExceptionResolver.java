package fit.wenchao.second_hand_trading_platform_front.exception;

import fit.wenchao.second_hand_trading_platform_front.controller.BackendException;
import fit.wenchao.second_hand_trading_platform_front.utils.JsonResult;
import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import fit.wenchao.utils.io.ExistsFile;
import jdk.internal.util.xml.impl.Input;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.transform.Result;
import java.io.*;
import java.util.Base64;

@ControllerAdvice
@Slf4j
public class GlobalExceptionResolver {


    /**
     * 处理所有不可知异常
     *
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult handleException(Exception e) {
        // 打印异常堆栈信息
        log.error(e.getMessage(), e);
        return JsonResult.getInstance(null, ResultCodeEnum.UNKNOWN_ERROR, e.getMessage());
    }

    /**
     * 处理所有业务异常
     *
     * @param e 业务异常
     * @return json结果
     */
    @ExceptionHandler(BackendException.class)
    @ResponseBody
    public JsonResult handleOpdRuntimeException(BackendException e) {
        // 不打印异常堆栈信息
        if (e.getDetail() == null || e.getDetail().equals("")) {
            log.error(e.getResultCodeEnum().getMessage());
        }
        log.error(e.getDetail());
        return JsonResult.getInstance(null, e.getResultCodeEnum(), e.getDetail());
    }

    public static void main(String[] args) throws IOException {
        ExistsFile prepath = new ExistsFile("/Users/cw/Documents/screenshots");

        ExistsFile existsFile = new ExistsFile(prepath.getPath() + File.separator + "honor_v20.png");
        System.out.println(existsFile.getAbsolutePath());
        System.out.println(existsFile.exists());

        try (FileInputStream in = new FileInputStream(existsFile.getUnderlyingFile());
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            IOUtils.copy(in, out);

            byte[] encode = Base64.getEncoder().encode(out.toByteArray());

        }
    }
}
