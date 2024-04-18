package com.hmdp.utils.limit;

//自定义限流异常
public class LimitException extends RuntimeException{
    public LimitException(){super();}
    public LimitException(String msg){super(msg);}
    @Override
    public String toString() {
        return "系统繁忙！请稍后再试试";
    }
}
