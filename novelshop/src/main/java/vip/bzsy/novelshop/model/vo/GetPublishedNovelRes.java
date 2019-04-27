package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-12 19:43
 */
@Data
@ToString
@NoArgsConstructor
public class GetPublishedNovelRes {

    private Integer id;

    private String novelname;

    private String introduction;

    private String cover;
}
