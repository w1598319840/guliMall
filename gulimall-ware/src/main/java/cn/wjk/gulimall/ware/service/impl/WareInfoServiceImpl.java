package cn.wjk.gulimall.ware.service.impl;

import cn.wjk.gulimall.common.domain.dto.PageDTO;
import cn.wjk.gulimall.common.utils.PageUtils;
import cn.wjk.gulimall.common.utils.Query;
import cn.wjk.gulimall.ware.dao.WareInfoDao;
import cn.wjk.gulimall.ware.entity.WareInfoEntity;
import cn.wjk.gulimall.ware.service.WareInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils pageQueryDetail(PageDTO pageDTO) {
        if (pageDTO == null) {
            return PageUtils.emptyPageUtils();
        }

        boolean isAsc = "asc".equalsIgnoreCase(pageDTO.getOrder());
        Page<WareInfoEntity> page = new Page<>(pageDTO.getPage(), pageDTO.getLimit());
        QueryWrapper<WareInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(StringUtils.isNotEmpty(pageDTO.getSidx()), isAsc, pageDTO.getSidx());
        String key = pageDTO.getKey();
        queryWrapper.and(StringUtils.isNotEmpty(key), wrapper ->
                wrapper.eq("id", key)
                        .or()
                        .like("name", key)
                        .or()
                        .like("address", key)
        );
        this.page(page, queryWrapper);

        return new PageUtils(page);
    }
}