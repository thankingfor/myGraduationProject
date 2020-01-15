package vip.bszy.example;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.CreateBucketRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：李延富
 * @date ：Created in 2019/12/17 23:13
 * description ：
 */
@Component
public class BucketExample {

    @Resource
    private COSClient cosClient;

    /**
     * 创建Bucket
     */
    public Bucket create(String bucket) {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
        // 设置 bucket 的权限为 PublicRead(公有读私有写), 其他可选有私有读写, 公有读写
        createBucketRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        try{
            Bucket bucketResult = cosClient.createBucket(createBucketRequest);
            System.out.println(bucketResult);
            return bucketResult;
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
        return null;
    }

    public List<Bucket> getAllBucket() {
        try {
            List<Bucket> buckets = cosClient.listBuckets();
            System.out.println(buckets);
            return buckets;
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
        return null;
    }
}
