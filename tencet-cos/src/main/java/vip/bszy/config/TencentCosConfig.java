package vip.bszy.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author ：李延富
 * @date ：Created in 2019/12/17 22:40
 * description ：
 */
@Component
@Getter
public class TencentCosConfig {

    @Value("${secretId}")
    private String secretId;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${bucket}")
    private String bucket;

    @Bean
    public COSClient init() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        // 北京 ap-beijing
        Region region = new Region("ap-beijing");
        ClientConfig clientConfig = new ClientConfig(region);

        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }
}
