package cn.wjk.gulimall.ware.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品库存
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 14:00:15
 */
@Data
@TableName("wms_ware_sku")
public class WareSkuEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * 仓库id
     */
    private Long wareId;
    /**
     * 库存数
     */
    private Integer stock;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * 锁定库存
     */
    private Integer stockLocked;

}
