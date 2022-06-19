package fit.wenchao.second_hand_trading_platform_front.utils;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    TOKEN_EXPIRED(100, "token已失效"),
    USER_INFO_NOT_COMPLETE(101, "用户信息未完善"),
    FRONT_DATA_MISSING(102, "前端数据缺失"),
    USER_UNREGISTERED(103, "此id用户未注册"),
    OAUTH_CODE_INVALID(104, "无效的 oauth_code"),
    OAUTH_CODE_BEEN_USED(105, "oauth_code 已被使用"),
    USER_INFO_COMPLETED(106, "用户信息无需完善"),
    ATTACHMENT_NOT_EXISTS(107, "附件不存在"),
    FRONT_DATA_REDUNDANT(108, "前端数据冗余"),

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(202, "服务异常"),
    //DATA_ERROR(204, "数据异常"),

    SIGN_ERROR(300, "签名错误"),
    SIGN_MISSING(301, "签名丢失"),

    PAY_PASSWORD_ERROR(401, "支付密码错误"),
    REPEAT_ERROR(402, "重复提交"),

    VERIFY_ERROR(501, "验证码错误"),
    FRONT_PARAM_ERROR(502, "前端参数错误"),
    REPEAT_REGISTRATION(503, "重复注册"),
    UNKNOWN_ERROR(500, "未知后端异常"),
    ORDER_EXISTS(504, "重复下单"),
    USER_NOT_EXISTS(505, "用户不存在"),
    USER_DATA_ERROR(506, "用户数据错误"),

    DATA_ERROR(506, "数据异常"),
    MONEY_NOT_ENOUGH(507, "余额不足"),
    ORDER_COMMENT_EXISTS(508, "订单已评价"),
    ONE_STORE_APPLICATION_ONE_USER(509, "每个用户只能有一个店铺"),
    NOT_HAS_STORE(510, "该用户没有商铺"),
    STOCK_NOT_ENOUGH(511, "商品库存不足"), ADDRESS_MAX(520, "地址不能超过5个"), CANNOT_DEL_DEFAULT_ADDRESS(521, "不能删除默认地址");
    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
