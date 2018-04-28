package com.suixingpay.sign.signatureService;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.commons.*;
import com.suixingpay.sign.annotations.SignatureField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 返回数据加密
 *
 * @author cxd
 * @date 2018-04-03
 */
@ControllerAdvice(basePackages = "com.suixingpay.sign")
public class SignatureResponseBodyAdvice implements ResponseBodyAdvice, ConstantUtil {
    private final static Logger logger = LoggerFactory.getLogger(SignatureResponseBodyAdvice.class);

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     *
     * 针对返回数据 respData 进行加密处理
     * 最终将加密数据保存至名 respData
     * 注：respData 字段名不可变更
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return 解密后明文数据
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        boolean encode = false;
        if (returnType.getMethod().isAnnotationPresent(SignatureField.class)) {
            //获取注解配置的包含和去除字段
            SignatureField signatureField = returnType.getMethodAnnotation(SignatureField.class);
            //是否加签
            encode = signatureField.encode();
        }
        if (encode) {
            logger.info("对方法method :" + returnType.getMethod().getName() + "返回数据进行加签");
            try {
                //返回数据
                JSONObject obj = JSONObject.parseObject(body.toString());
                //respData
                String retRespData = obj.getString("respData");
                //签名
                byte[] retSign = RSA.encryptByPrivateKey(retRespData.getBytes(), RSAEncrypt.loadPrivateKeyByFile(FILEPATH));

                obj.put("sign", retSign);

                return obj.toJSONString();
            } catch (Exception e) {
                logger.error("返回数据出现异常：{}", e.getMessage());
            }
        }
        return body;
    }
}
