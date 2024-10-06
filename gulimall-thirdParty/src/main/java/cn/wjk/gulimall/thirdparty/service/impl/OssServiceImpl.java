package cn.wjk.gulimall.thirdparty.service.impl;

import cn.wjk.gulimall.thirdparty.config.OssConfig;
import cn.wjk.gulimall.thirdparty.service.OssService;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Package: cn.wjk.gulimall.thirdparty.service.impl
 * @ClassName: OssServiceImpl
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/5 下午8:28
 * @Description:
 */
@Service
@RequiredArgsConstructor
public class OssServiceImpl implements OssService {
    private final OssConfig ossConfig;
    /**
     * 签名直传
     */
    public Map<String, String> putOss() {
        //以日期为文件夹组织对象存储
        String format = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String prefix = format + "/";
        //https://gulimall-159.oss-cn-chengdu.aliyuncs.com/HunterPie.exe
        String host = "https://" + ossConfig.getBucket() + "." + ossConfig.getEndpoint();
        @SuppressWarnings("deprecation")
        OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKey(), ossConfig.getSecretKey());
        Map<String, String> respMap = new LinkedHashMap<>();

        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, prefix);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap.put("accessid", ossConfig.getAccessKey());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", prefix);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));
        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return respMap;
    }
}
