package com.suixingpay.sign.annotations;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * 签名组件
 * 只签名，不负责加解密
 * 自定义注解：
 *      基于 http 请求
 *      在 controller 每个方法上使用 @SignatureField 即可开启签名组件
 *      encode 默认 true，即开启加解密组件
 *      encode 设为 false，与不适用 @SignatureField 注解同理，即关闭签名组件
 * @date 2018-04-27
 * @author cxd
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface SignatureField {
    /**
     * 是否加密
     * @return
     */
    boolean encode() default true;
}
