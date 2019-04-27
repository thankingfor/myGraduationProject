package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lyf
 * @create 2019-03-12 20:11
 */
@Data
@ToString
public class UploadChapterReq {

    private Integer id;

    private MultipartFile file;
}
