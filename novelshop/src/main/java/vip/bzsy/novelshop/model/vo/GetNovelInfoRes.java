package vip.bzsy.novelshop.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-13 19:36
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetNovelInfoRes {

    private String novelName;

    private String editorName;

    private Integer editorId;

    private String introduction;

    private Integer clicknum;

    private Integer commentnum;

    private Integer downloadnum;

    private String sort;

    private Integer price;

    private String cover;

    private String time;

}
