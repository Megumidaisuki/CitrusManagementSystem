package com.feidian.common.annotation;

import java.lang.annotation.*;

/**
 * @author lep
 * @date 2022-08-05 15:41
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

    /**
     * 在  seconds 秒内 , 最大只能请求 maxCount 次
     *
     * @return
     */
    //    秒
    int seconds() default 1;

    //    最大数量
    int maxCount() default 1;
}
