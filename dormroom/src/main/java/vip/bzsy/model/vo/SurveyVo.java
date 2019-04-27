package vip.bzsy.model.vo;


import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-04-26 10:37
 */
@Data
@ToString
public class SurveyVo {

    private Integer uid;

    private String name;

    /**
     * 起床时间段（一个数组，存放两个时间段，比如[09:00, 09:59]）
     */
    private String getUpTime;

    /**
     * 睡觉时间段，同上
     */
    private String sleepTime;

    /**
     * 喜欢的游戏类型
     */
    private String[] game;

    /**
     * 喜欢的阅读类型
     */
    private String[] read;

    /**
     * 喜欢影视作品类型
     */
    private String[] video;

    /**
     * 喜欢的运动类型
     */
    private String[] sport;

    /**
     * 喜欢的音乐类型
     */
    private String[] music;

    private Integer religion;

    /**
     * 是否希望有一个安静的环境？
     */
    private Integer quite;

    /**
     * 是否会在宿舍经常有产生噪音的行为？
     */
    private Integer noise;

    /**
     *  是否习惯使用公放设备（如使用外置音响）？
     */
    private Integer sound;

    /**
     * 是否会经常长时间待在宿舍？
     */
    private Integer stay;


    public static String[] StringtoArray(String str){
        return str.split(",");
    }

    public static String ArraytoString(String[] array){
        String strs = "";
        for (String str : array){
            if (strs.equals(""))
                strs = str;
            else
                strs = strs + "," +str;
        }
        return strs;
    }

}
