package com.bigfire.easychat.entity.response;

import com.bigfire.easychat.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  15:18
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@Accessors(chain = true)
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private  T data;
    private String time;
    public Result(){
        setTime(LocalDateTime.now().toString());
    }
    public Result(Integer code){
        setCode(code).setTime(LocalDateTime.now().toString());
    }
    public Result(Integer code,String msg){
        setCode(code).setMsg(msg).setTime(LocalDateTime.now().toString());
    }
    public Result(Integer code,String msg,T data){
        setCode(code).setMsg(msg).setData(data).setTime(LocalDateTime.now().toString());
    }
    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 500;
    private static final String SUCCESS_MSG = "成功";
    private static final String ERROR_MSG = "失败";
    //成功返回值
    public static Result getSuccessResult() {
        return new Result(SUCCESS_CODE,SUCCESS_MSG);
    }
    public static Result getSuccessResult(Object data) {
        return getSuccessResult().setData(data);
    }
    //错误返回值
    public static Result getErrorResult() {
        return new Result(ERROR_CODE,ERROR_MSG);
    }
    public static Result getErrorResult(String msg) {
        return new Result(ERROR_CODE,msg);
    }
    //自定义消息
    public static Result getMsgResult(String msg) {
        return new Result(SUCCESS_CODE,msg);
    }
    public static Result getMsgResult(String msg,Object data) {
        return getMsgResult(msg).setData(data);
    }
    //自定义返回Code
    public static Result getCodeResult(Integer code,String msg) {
        return new Result(code,msg);
    }
    public static Result getCodeResult(Integer code,String msg,Object data) {
        return getCodeResult(code,msg).setData(data);
    }
}
