package top.sleepnano.kkcommon.util;

import top.sleepnano.kkcommon.vo.Result;

public class VoBuilder {
    public static Result ok(String msg, Object data){
        return new Result("1",msg,data);
    }
    public static Result wrong(String msg, Object data){
        return new Result("0",msg,data);
    }
    public static Result error(String msg, Object data){
        return new Result("-1",msg,data);
    }

}
