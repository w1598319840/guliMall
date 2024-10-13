package cn.wjk.gulimall.product.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @Package: cn.wjk.gulimall.product.domain.vo
 * @ClassName: Catelog2VO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/13 下午2:28
 * @Description:
 */
@Data
public class Catelog2VO {
    private String catalog1Id;
    private List<Catelog3VO> catalog3List;
    private String id;
    private String name;
}
