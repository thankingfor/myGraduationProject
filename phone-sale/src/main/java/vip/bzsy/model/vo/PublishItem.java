package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyf
 * @create 2019-04-06 16:42
 */
@Data
@ToString
public class PublishItem {
    private Integer id;
    private String title;
    private BigDecimal price;
    private Integer stock;
    private String introduction;
    private String[] color;
    private List<sortClass> sort;
    private Integer status;
    private String cover;
    private Integer saleNum;
    private Long time;

    public void setColorStr(String str){
        String[] split = str.split(",");
        this.color=split;
    }

    public String getColorStr(){
        if (color.length>0){
            String cols = color[0];
            for (int i =1;i<color.length;i++){
                cols = cols+","+color[i];
            }
            return cols;
        }
        return "";
    }

    public void setSortStr(String str){
        String[] split = str.split(",");
        List<sortClass> list = new ArrayList<>();
        for (String s:split){
            String[] split1 = s.split(":");
            sortClass sortclass = new sortClass();
            sortclass.setSort(split1[0]);
            Double aDouble = Double.valueOf(split1[1]);
            sortclass.setPrice(new BigDecimal(aDouble));
            list.add(sortclass);
        }
        this.sort = list;
    }

    public String getSortStr(){
        String str ="";
        for (sortClass s:sort){
            if (str.equals("")){
                str = s.getSort()+":"+s.getPrice();
            } else {
                str = str + "," +s.getSort()+":"+s.getPrice();
            }
        }
        return str;
    }

}

@Data
@ToString
class sortClass {
    private String sort;
    private BigDecimal price;
}
