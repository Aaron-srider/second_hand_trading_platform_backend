
package fit.wenchao.second_hand_trading_platform_front.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户申请注册店铺时需要申请
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
@TableName("store_regist_application")
public class StoreRegistApplicationPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 店铺属主id
     */
    private Integer merchantId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 身份证图片
     */
    private String idcard;

    /**
     * 营业执照图片
     */
    private String businessLicence;

    /**
     * 是否通过（默认-1，0不通过，1通过）
     */
    private Integer permit;

    /**
     * 审核人id
     */
    private Integer approvalId;

    /**
     * 审核时间
     */
    private String approvalTime;



}
