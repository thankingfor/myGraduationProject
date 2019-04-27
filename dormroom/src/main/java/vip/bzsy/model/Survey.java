package vip.bzsy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyf
 * @since 2019-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class Survey extends Model<Survey> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 起床时间段（一个数组，存放两个时间段，比如[09:00, 09:59]）
     */
    @TableField("getUpTime")
    private String getUpTime;

    /**
     * 睡觉时间段，同上
     */
    @TableField("sleepTime")
    private String sleepTime;

    /**
     * 喜欢的游戏类型
     */
    private String game;

    /**
     * 喜欢的阅读类型
     */
    @TableField("`read`")
    private String read;

    /**
     * 喜欢影视作品类型
     */
    private String video;

    /**
     * 喜欢的运动类型
     */
    private String sport;

    /**
     * 喜欢的音乐类型
     */
    private String music;

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

    @TableLogic
    private Integer deleted;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
