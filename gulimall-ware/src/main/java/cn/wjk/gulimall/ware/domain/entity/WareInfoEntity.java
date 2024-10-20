package cn.wjk.gulimall.ware.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 仓库信息
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-04 14:00:15
 */
@Data
@TableName("wms_ware_info")
public class WareInfoEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 仓库名
     */
    private String name;
    /**
     * 仓库地址
     */
    private String address;
    /**
     * 区域编码
     */
    private String areacode;

}
