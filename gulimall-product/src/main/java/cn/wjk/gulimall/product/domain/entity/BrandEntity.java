package cn.wjk.gulimall.product.domain.entity;

import cn.wjk.gulimall.common.validator.annotation.ListValue;
import cn.wjk.gulimall.common.validator.group.AddGroup;
import cn.wjk.gulimall.common.validator.group.UpdateGroup;
import cn.wjk.gulimall.common.validator.group.UpdateStatusGroup;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.io.Serial;
import java.io.Serializable;

/**
 * 品牌
 *
 * @author wenjiakai
 * @email 1598319840@qq.com
 * @date 2024-10-03 20:19:51
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @NotNull(message = "修改时品牌id不能为空", groups = {UpdateGroup.class, UpdateStatusGroup.class})
    @Null(message = "新增时品牌id必须为空", groups = AddGroup.class)
    @TableId
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @NotBlank(message = "logo不能为空", groups = {AddGroup.class})
    @URL(message = "logo必须是一个合法的url地址", groups = {UpdateGroup.class, AddGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @NotNull(groups = {UpdateStatusGroup.class, AddGroup.class})
    @ListValue(value = {0, 1}, groups = {UpdateGroup.class, AddGroup.class, UpdateStatusGroup.class})
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotEmpty(groups = AddGroup.class)
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须在a~z/A~Z范围内，有且只能有一个字母",
            groups = {UpdateGroup.class, AddGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(groups = AddGroup.class)
    @Min(value = 0, message = "排序字段必须大于0", groups = {UpdateGroup.class, AddGroup.class})
    private Integer sort;

}
