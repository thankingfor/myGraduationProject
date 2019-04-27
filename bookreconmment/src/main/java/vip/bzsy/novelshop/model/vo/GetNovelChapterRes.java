package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-13 11:25
 */
@Data
@ToString
@NoArgsConstructor
public class GetNovelChapterRes {

    private Integer id;

    private Integer chapter;

    private String title;
}
