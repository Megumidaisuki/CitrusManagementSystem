package com.feidian.sidebarTree.controller;

import com.feidian.sidebarTree.anno.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author jia wei
 * Date 2022/8/21 17:33
 */


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 资源映射路径
         * addResourceHandler：访问映射路径
         * addResourceLocations：资源绝对路径
         */
        registry.addResourceHandler("/image/**").addResourceLocations("file://");
        registry.addResourceHandler("/csv/**").addResourceLocations("file://");
    }

    @Autowired
    private TimeInterceptor interceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
//
//    /**
//     * 解决 redis 的编码统一方面的问题
//     *
//     * @param connectionFactory
//     * @return
//     */
//    @Bean
//    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory) {
//        //创建 redisTemplate 模版
//        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
//        //设置 value 的转化格式和 key 的转化格式
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        //关联 redisConnectionFactory
//        redisTemplate.setConnectionFactory(connectionFactory);
//        return redisTemplate;
//    }
}
