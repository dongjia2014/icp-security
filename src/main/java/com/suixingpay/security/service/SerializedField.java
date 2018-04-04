package com.suixingpay.security.service;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * 加解密组件
 *
 * 自定义注解：
 *      基于 http 请求
 *      在 controller 每个方法上使用 @SerializedField 即可开启加解密组件
 *      encode 默认 true，即开启加解密组件
 *      encode 设为 false，与不适用 @SerializedField 注解同理，即关闭加解密组件
 * @date 2018-04-03
 * @author cxd
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface SerializedField {
    /**
     * 是否加密
     * @return
     */
    boolean encode() default true;
}
