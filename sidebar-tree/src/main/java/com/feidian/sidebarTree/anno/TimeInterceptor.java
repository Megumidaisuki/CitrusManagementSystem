package com.feidian.sidebarTree.anno;


import com.feidian.common.annotation.AccessLimit;
import com.feidian.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * 拦截器
 *
 * @author lep
 * @date 2022-08-05 15:45
 */
@Component
public class TimeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisCache redisCache;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//       1. 判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod) {
//       2. 取出当前方法的对象
            HandlerMethod handler1 = (HandlerMethod) handler;
//       3. 获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = handler1.getMethodAnnotation(AccessLimit.class);
//            3.1 : 不包含此注解,则不进行操作
            if (accessLimit != null) {
//                3.2 ： 判断请求是否受限制
                if (isLimit(request, accessLimit)) {
                    render(response, "{\"code\":\"30001\",\"message\":\"请求过快\"}");
                    return false;
                }
            }
        }
        return true;
    }

    //判断请求是否受限
    public boolean isLimit(HttpServletRequest request, AccessLimit accessLimit) {
        // 受限的redis 缓存key ,因为这里用浏览器做测试，我就用sessionid 来做唯一key,如果是app ,可以使用 用户ID 之类的唯一标识。
        String limitKey = request.getServletPath() + request.getSession().getId();
        // 从缓存中获取，当前这个请求访问了几次
        Integer redisCount = (Integer) redisCache.getCacheObject(limitKey);
        if (redisCount == null) {
            //初始 次数
            redisCache.setCacheObject(limitKey, 1, accessLimit.seconds(), TimeUnit.SECONDS);
            System.out.println("写入redis --");
        } else {
            System.out.println("intValue-->" + redisCount.intValue());
            if (redisCount.intValue() >= accessLimit.maxCount()) {
                return true;
            }
            // 次数自增
            redisCache.increment(limitKey);
        }
        return false;
    }

    private void render(HttpServletResponse response, String cm) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(cm.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
