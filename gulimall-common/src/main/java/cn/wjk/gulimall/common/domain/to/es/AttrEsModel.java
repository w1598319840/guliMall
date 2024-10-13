package cn.wjk.gulimall.common.domain.to.es;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.common.domain.to.es
 * @ClassName: AttrEsModel
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/12 下午3:54
 * @Description: Attr在ES中的数据模型
 */
@Data
public class AttrEsModel {
    private Long attrId;
    private String attrName;
    private String attrValue;
}
