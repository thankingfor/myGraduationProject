package vip.bzsy.novelshop.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-14 15:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PublishCommentReq {

    private Integer novelId;
    private String content;
    private String time;
}
