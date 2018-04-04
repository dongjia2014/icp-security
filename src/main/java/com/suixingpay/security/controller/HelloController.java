package com.suixingpay.security.controller;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.security.service.SerializedField;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请求 controller
 * Created by cxd on 2018-04-03
 */
@RestController
public class HelloController {

    /**
     * 受理请求方的请求数据
     * 通过注解 @SerializedField 对 sign 和 reqData 进行解签和解密
     * 注：请求数据中 sign 与 reqData 为必填项
     * 注：返回数据中，sign 与 respData 为必填项，且 respData 为数据密文
     * 注：respData 为统一加密数据，字段名称不可变更，接收方针对此字段解密即可
     * @param json 请求数据
     * @return 返回带密文数据
     */
    @RequestMapping("/")
    @SerializedField
    public String sayHi(@RequestBody String json) {

        JSONObject obj = JSONObject.parseObject(json);
        //请求铭文数据，后续业务处理
        System.out.println(obj.getString("reqData"));

        // response respData 为必填字段，即返回数据体
        return "{\"channelNo\":\"SXF0001\",\"msgId\":\"201804021409192968\",\"respData\":\"ihep_这是用于签名的原始数据\",\"retCode\":\"000000\",\"retMsg\":\"SUCCESS\"}";
    }


    /**
     * 受理请求方的请求数据
     * 未使用注解执行加解密
     * 注：encode=false 意为禁止使用注解组件进行加解密，默认 true
     * @return 返回明文数据
     */
    @RequestMapping("/show")
    @SerializedField(encode=false)
    public String show() {
        return "{\"channelNo\":\"SXF0001\",\"msgId\":\"201804021409192968\",\"respData\":\"ihep_这是用于签名的原始数据\",\"retCode\":\"000000\",\"retMsg\":\"SUCCESS\"}";
    }

    /**
     * 受理请求方的请求数据
     * 使用注解执行加解密
     * 注：此处 @SerializedField 与 @SerializedField（encode=true）同义，意为使用注解组件进行加解密
     * 注：respData 为统一加密数据，字段名称不可变更，接收方针对此字段解密即可
     * @return 返回结构中，respData 为密文数据
     */
    @RequestMapping("/showSec")
    @SerializedField
    public String showSec() {
        return "{\"channelNo\":\"SXF0001\",\"msgId\":\"201804021409192968\",\"respData\":\"ihep_这是用于签名的原始数据\",\"retCode\":\"000000\",\"retMsg\":\"SUCCESS\"}";
    }
}
