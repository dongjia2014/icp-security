package com.suixingpay.security.controller;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.security.annotations.SignEncryField;
import com.suixingpay.sign.annotations.SignatureField;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.suixingpay.sign.controller.icp-security
 * Created by cxd on 2018/4/28.
 */
@RestController
public class HelloController {

    @RequestMapping("/encry")
    @SignEncryField
    public String encry(@RequestBody String json){
        JSONObject obj = JSONObject.parseObject(json);
        System.out.println(obj.getString("reqData"));
        return "{\"channelNo\":\"SXF0001\",\"msgId\":\"201804021409192968\",\"respData\":\"ihep_这是用于签名的原始数据\",\"retCode\":\"000000\",\"retMsg\":\"SUCCESS\"}";
    }

}
