package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;


/**
 * @author lyf
 * @create 2019-04-24 10:47
 */
@Data
@ToString
public class WordVo {

    private Integer id;

    private String word;

    private String[] chinese;

    private Integer unit;

    private String chineseStr;

    public String getChineseStr(){
        String chineseStr = "";
        if (chinese.length>=1){
            chineseStr=chinese[0];
        }
        for (int i = 1; i < chinese.length ; i++){
            chineseStr = chineseStr + "," +chinese[i];
        }
        return chineseStr;
    }

    public static String[] setChineseStr(String str){
        return str.split(",");
    }
}
