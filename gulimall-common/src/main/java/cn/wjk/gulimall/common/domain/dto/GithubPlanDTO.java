package cn.wjk.gulimall.common.domain.dto;

import lombok.Data;

/**
 * @Package: cn.wjk.gulimall.common.domain.dto
 * @ClassName: GithubPlanDTO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/21 下午3:18
 * @Description:
 */
@Data
public class GithubPlanDTO {
    private String name;
    private int space;
    private int collaborators;
    private int private_repos;
}
