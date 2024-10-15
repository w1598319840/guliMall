package cn.wjk.gulimall.search.service;

import cn.wjk.gulimall.common.domain.vo.SearchVO;
import cn.wjk.gulimall.search.domain.dto.SearchDTO;

/**
 * @Package: cn.wjk.gulimall.search.service
 * @ClassName: MallSearchService
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/15 下午6:56
 * @Description: 商城搜索的Service
 */
public interface MallSearchService {
    /**
     * 检索商品
     */
    SearchVO search(SearchDTO searchDTO);
}
