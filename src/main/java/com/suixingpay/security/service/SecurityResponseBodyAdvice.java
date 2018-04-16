package com.suixingpay.security.service;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.security.utils.EncryptUtil;
import com.suixingpay.security.utils.RSAEncrypt;
import com.suixingpay.security.utils.RSASignature;
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
@ControllerAdvice(basePackages = "com.suixingpay")
public class SecurityResponseBodyAdvice implements ResponseBodyAdvice, ConstantUtil {
    private final static Logger logger = LoggerFactory.getLogger(SecurityResponseBodyAdvice.class);

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
        if (returnType.getMethod().isAnnotationPresent(SerializedField.class)) {
            //获取注解配置的包含和去除字段
            SerializedField serializedField = returnType.getMethodAnnotation(SerializedField.class);
            //是否加密
            encode = serializedField.encode();
        }
        if (encode) {
            logger.info("对方法method :" + returnType.getMethod().getName() + "返回数据进行加密");
            //            ObjectMapper objectMapper = new ObjectMapper();
            try {
                //                String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                //返回数据
                JSONObject obj = JSONObject.parseObject(body.toString());
                //respData
                String retRespData = obj.getString("respData");
                //签名
                String retSign = RSASignature.encryptBASE64(RSASignature.sign(retRespData, RSAEncrypt.loadPrivateKeyByFile(FILEPATH)));
                //加密
                retRespData = EncryptUtil.encryptBASE64(EncryptUtil.encrypt(retRespData.toString().getBytes(), DES_KEY.getBytes()));

                obj.put("sign", retSign);
                obj.put("respData", retRespData);

                return obj.toJSONString();
            } catch (Exception e) {
                logger.error("返回数据出现异常：{}", e.getMessage());
            }
        }
        return body;
    }
}
