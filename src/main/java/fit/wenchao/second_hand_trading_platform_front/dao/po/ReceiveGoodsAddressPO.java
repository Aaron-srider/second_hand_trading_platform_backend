package fit.wenchao.second_hand_trading_platform_front.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import fit.wenchao.second_hand_trading_platform_front.utils.DontReturn;
import fit.wenchao.second_hand_trading_platform_front.utils.MapName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("receive_goods_address")
public class ReceiveGoodsAddressPO {

    @TableId(type = IdType.AUTO)
    public Integer id;

    @MapName("name")
    public String receiverName;

    public String phone;

    public String province;

    public String city;

    public String detail;

    @MapName("default")
    public Boolean ifDefaultAddress;

    @DontReturn
    public String userId;
}
