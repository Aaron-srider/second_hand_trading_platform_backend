package fit.wenchao.second_hand_trading_platform_front.controller;

import fit.wenchao.second_hand_trading_platform_front.utils.ResultCodeEnum;
import lombok.Getter;

@Getter
public class BackendException extends RuntimeException {
    String exName;
    String detail;
    ResultCodeEnum resultCodeEnum;

    public BackendException(ResultCodeEnum resultCodeEnum, String detail) {
        this.exName = resultCodeEnum.getMessage();
        this.detail = detail;
        this.resultCodeEnum = resultCodeEnum;
    }

}
