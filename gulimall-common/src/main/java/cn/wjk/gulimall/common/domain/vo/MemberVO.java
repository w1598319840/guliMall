package cn.wjk.gulimall.common.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Package: cn.wjk.gulimall.common.domain.vo
 * @ClassName: MemberVO
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/22 下午1:01
 * @Description:
 */
@Data
public class MemberVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
}
