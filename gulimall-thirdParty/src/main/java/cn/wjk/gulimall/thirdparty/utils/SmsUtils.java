package cn.wjk.gulimall.thirdparty.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package: cn.wjk.gulimall.thirdparty.utils
 * @ClassName: SmsUtils
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/18 下午5:20
 * @Description: 发送短信的工具类
 */
@Slf4j
@Component
public class SmsUtils {
    @Value("${gulimall.sms.appcode}")
    private String appcode;

    /**
     * 发送短信验证码功能
     *
     * @param phone 目标手机号
     * @param code 验证码
     */
    public void sendCode(String phone, String code) {
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = this.appcode;
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> queries = new HashMap<>();
        queries.put("mobile", phone);
        queries.put("param", "**code**:" + code + ",**minute**:3");
        queries.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        queries.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodies = new HashMap<>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, queries, bodies);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
