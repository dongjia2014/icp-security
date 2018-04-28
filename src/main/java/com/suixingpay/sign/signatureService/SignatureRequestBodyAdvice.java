package com.suixingpay.sign.signatureService;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.commons.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * 请求数据解密
 * @date 2018-04-03
 * @author cxd
 */
@ControllerAdvice(basePackages = "com.suixingpay.sign")
public class SignatureRequestBodyAdvice implements RequestBodyAdvice,ConstantUtil {
    private final static Logger logger = LoggerFactory.getLogger(SignatureRequestBodyAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * 请求数据到达 controller 前处理
     * 注：sign 为请求签名
     *    reqData 为请求密文数据
     *  字段名称不可变更
     * 注：将请求数据解签解密，然后将明文数据保存至 reqData 字段，业务方直接解析此字段业务处理即可
     * @param inputMessage http 请求数据
     * @param parameter
     * @param targetType
     * @param converterType
     * @return
     * @throws IOException
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        try {
            return new SignatureMessage(inputMessage);
        } catch (Exception e) {
            logger.error("针对方法 {}，解签解密异常：{}",parameter.getMethod().getName(),e.getMessage());
            return inputMessage;
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    class SignatureMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;

        public SignatureMessage(HttpInputMessage inputMessage) throws Exception {
            inputMessage.getHeaders().setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));
            this.headers = inputMessage.getHeaders();
            String data = IOUtils.toString(inputMessage.getBody(), ENCODING_UTF_8);
            JSONObject obj = JSONObject.parseObject(data);
            //sign
            String reqSign = obj.getString("sign");

            if(StringUtils.isEmpty(reqSign)){
                throw new Exception("sign can not be empty！");
            }
            //私钥解签
            String decEnglish = new String(RSA.decryptByPrivateKey(reqSign.getBytes(), RSAEncrypt.loadPublicKeyByFile(FILEPATH)));
            obj.put("reqData",decEnglish);
            this.body = IOUtils.toInputStream(obj.toString(), ENCODING_UTF_8);
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}
