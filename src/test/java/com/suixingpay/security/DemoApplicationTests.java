package com.suixingpay.security;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * 测试请求 controller 方法
 * 基于 RestTemplate ，发起 url 请求
 * postForObject 为 post 请求
 * 配合 HelloController 中 sayHi 方法，目的是可以看到解密后明文数据，
 * 业务方用来处理业务逻辑
 * @date 2018-04-03
 * @author cxd
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    private RestTemplate template = new RestTemplate();

    @Test
    public void testMasterDataControllerCreateMasterData() {
        try {
            String url = "http://localhost:" + 1212 + "/";
            //            String req = "{\"channelNo\":\"SXF0001\",\"msgId\":\"201804021409192968\",\"reqData\":\"KTUifQ6c9/qwW8YkWRc2f4osnYuNlyPm+NNaXQj6BUVa1Nd4KrXNBg==\",\"sign\":\"gvzxlwbzBNzK8zwMdkOQZhtprGB3uKZWheWaAlGqFpF0pMrk+kud9R0AncxQe5JuPXCKNBno/nPxaPeO/+F1d/gs/ERAP6ZMCEW+sYVtMMvbrstp3QMk+okhOBV17vT2+cHeVgwqROqBNx4yjnx0+AiIN/fgiccfqb5NlrIJU0c=\"}";
            String req = "{\"sign\":\"gvzxlwbzBNzK8zwMdkOQZhtprGB3uKZWheWaAlGqFpF0pMrk+kud9R0AncxQe5JuPXCKNBno/nPx\naPeO/+F1d/gs/ERAP6ZMCEW+sYVtMMvbrstp3QMk+okhOBV17vT2+cHeVgwqROqBNx4yjnx0+AiI\nN/fgiccfqb5NlrIJU0c=\n\",\"reqData\":\"KTUifQ6c9/qwW8YkWRc2f4osnYuNlyPm+NNaXQj6BUVa1Nd4KrXNBg==\n\",\"retCode\":\"000000\",\"retMsg\":\"SUCCESS\",\"msgId\":\"201804021409192968\",\"channelNo\":\"SXF0001\"}";
            JSONObject retJsonObj = template.postForObject(url, req, JSONObject.class);
            System.out.println(retJsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
