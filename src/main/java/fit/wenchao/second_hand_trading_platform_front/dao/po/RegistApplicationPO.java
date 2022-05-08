
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import fit.wenchao.second_hand_trading_platform_front.utils.dataValidate.Permission;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.intellij.lang.annotations.RegExp;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.ws.BindingType;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * 用户注册的申请存在该表中，由管理员审批
 * </p>
 *
 * @author wc
 * @since 2022-04-26
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("regist_application")
public class RegistApplicationPO implements Serializable {

    public interface InsertNewApplication{}

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名称
     */
    @NotNull(groups = {InsertNewApplication.class})
    private String name;

    /**
     * 用户手机号
     */
    @NotNull(groups = {InsertNewApplication.class})
    private String phone;

    /**
     * 用户邮箱
     */
    @NotNull(groups = {InsertNewApplication.class})
    private String email;

    /**
     * 登录密码
     */
    @NotNull(groups = {InsertNewApplication.class})
    private String password;

    /**
     * 用户城市
     */
    @NotNull(groups = {InsertNewApplication.class})
    private String city;

    /**
     * 性别，0女，1男
     */
    @NotNull(groups = {InsertNewApplication.class})
    private Integer sex;

    /**
     * 银行账号（16位）
     */
    @NotNull(groups = {InsertNewApplication.class})
    @Length(max=16 ,min=16, message="银行卡号必须为16位数", groups = {InsertNewApplication.class})
    private String bankNo;

    /**
     * 是否通过，-1默认，0不通过，1通过
     */
    @NotNull(groups = {InsertNewApplication.class})
    @Permission(groups = {InsertNewApplication.class})
    private Byte permission;

    /**
     * 审批时间
     */
    @NotNull(groups = {InsertNewApplication.class})
    @Pattern(regexp = "\\d{4}(-\\d{2,2}){2} \\d{2}(:\\d{2}){2}", message = "日期格式为：yyyy-MM-dd hh:mm:ss",groups = {InsertNewApplication.class})
    private String approvalTime;

    @TableField(exist = false)
    Integer total;


}
