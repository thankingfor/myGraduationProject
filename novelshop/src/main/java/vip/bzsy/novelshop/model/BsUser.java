package vip.bzsy.novelshop.model;

public class BsUser {
    private Integer id;

    private String identity;

    private String name;

    private String account;

    private String password;

    private Integer vp;

    private Integer ban;

    private String like;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity == null ? null : identity.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getVp() {
        return vp;
    }

    public void setVp(Integer vp) {
        this.vp = vp;
    }

    public Integer getBan() {
        return ban;
    }

    public void setBan(Integer ban) {
        this.ban = ban;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like == null ? null : like.trim();
    }
}