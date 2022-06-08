package cn.techaction.mall.pojo;



public enum ResponeCode {
    SUCESS(0,"SUCESS"),
    ERROR(1,"ERROR")
    ;

    private final int code;
    private final String desc;

    private ResponeCode(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
