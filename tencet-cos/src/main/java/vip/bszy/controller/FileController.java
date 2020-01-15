package vip.bszy.controller;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vip.bszy.config.TencentCosConfig;
import vip.bszy.entity.common.JsonResponse;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ：李延富
 * @date ：Created in 2019/12/17 23:34
 * description ：
 */
@RestController
@RequestMapping("/api/v1/object")
public class FileController {

    @Resource
    private COSClient cosClient;

    @Resource
    private TencentCosConfig cosConfig;

    @Value("${basefile}")
    private String basefile;


    @PostMapping("/upload")
    public JsonResponse createObject(MultipartFile multipartFile, String path) {
        try {
            // 指定要上传的文件
            String name = multipartFile.getOriginalFilename();
            InputStream inputStream = multipartFile.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(cosConfig.getBucket(), basefile + path + "/" + name, inputStream, objectMetadata);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return new JsonResponse(putObjectResult);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonResponse();
    }

    @PostMapping("/upload/folder")
    public JsonResponse createFolder(String path) {
        // 目录对象即是一个/结尾的空文件，上传一个长度为 0 的 byte 流
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0);

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(cosConfig.getBucket(), basefile + path, input, objectMetadata);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        return new JsonResponse(putObjectResult);
    }
}
