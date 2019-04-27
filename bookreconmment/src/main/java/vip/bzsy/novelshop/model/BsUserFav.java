package vip.bzsy.novelshop.model;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BsUserFav {
    private Integer id;

    private Integer uid;

    private Integer novelid;

    private Integer status;

    public BsUserFav(Integer uid,Integer novelid,Integer status){
        this.uid = uid;
        this.novelid = novelid;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getNovelid() {
        return novelid;
    }

    public void setNovelid(Integer novelid) {
        this.novelid = novelid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}