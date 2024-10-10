package cn.wjk.gulimall.common.domain.dto;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.common.domain.dto
 * @ClassName: PageDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/6 下午5:05
 * @Description: 分页查询时的DTO(先在AttrGroupController中使用)
 */
@Data
public class PageDTO {
    /**
     * 当前页码
     */
    private int page;
    /**
     * 每页记录数
     */
    private int limit;
    /**
     * 排序字段
     */
    private String sidx;
    /**
     * 排序方式
     */
    private String order;
    /**
     * 检索关键字
     */
    private String key;
    /**
     * 三级分类id
     */
    private Long catelogId;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 商品状态
     */
    private Integer status;
}
