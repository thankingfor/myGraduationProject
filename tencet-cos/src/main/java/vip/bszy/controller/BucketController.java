package vip.bszy.controller;

import com.qcloud.cos.model.Bucket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.bszy.entity.common.JsonResponse;
import vip.bszy.example.BucketExample;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：李延富
 * @date ：Created in 2019/12/17 23:20
 * description ：
 */
@RestController
@RequestMapping("/api/v1/bucket")
public class BucketController {

    @Resource
    private BucketExample bucketExample;

    @GetMapping("/create")
    public JsonResponse createBucket(String bucketName) {
        Bucket bucket = bucketExample.create(bucketName);
        return new JsonResponse(bucket);
    }

    @GetMapping("/all")
    public JsonResponse getAllBucket() {
        List<Bucket> allBucket = bucketExample.getAllBucket();
        return new JsonResponse(allBucket);
    }
}
