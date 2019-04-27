package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-04-26 12:10
 */
@Data
@ToString
public class DormFilterVo {

    private Integer roomId;
    private Integer peopleNum;
    private String[] getUpTime;
    private String[] sleepTime;
    private double gamePct;
    private double readPct;
    private double videoPct;
    private double sportPct;
    private double musicPctc;
    private double religionPct;
    private double quitePct;
    private double noisePct;
    private double soundPct;
    private double stayPct;
    private double total;
    public void setTotal(){
        this.total = gamePct+readPct+videoPct+sportPct+musicPctc+religionPct+quitePct+noisePct+soundPct+stayPct;
    }
}
