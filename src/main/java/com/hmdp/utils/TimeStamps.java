package com.hmdp.utils;

import java.time.*;

//生成时间戳
public class TimeStamps {
    static long begin_timestamp;
    //设置起始时间为2021年1月1日
    static {
        // 创建LocalDate对象表示2021年1月1日
        LocalDate date = LocalDate.of(2021, 1, 1);

        // 创建LocalDateTime对象表示2021年1月1日的午夜
        LocalDateTime dateTime = date.atStartOfDay();

        // 将LocalDateTime对象转换为时间戳（秒）
        begin_timestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
    }

    public static long now(){
        return System.currentTimeMillis()-begin_timestamp;
    }
}
