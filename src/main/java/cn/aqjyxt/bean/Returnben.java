package cn.aqjyxt.bean;


public class Returnben {
    private String msg;

    private String number;

    private String token;

//    private String username;
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSuccess(String number) {
        this.number = number;
    }

    public void setData(String token) {
        this.token = token;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getSuccess() {
        return this.number;
    }

    public String getData() {
        return this.token;
    }
}
