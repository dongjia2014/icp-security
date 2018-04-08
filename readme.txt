接入手册

1、	将 jar 包导入
2、	在自己的 controller 方法上，加入@SerializedField 注解即可

注：
    如需运行、测试，可直接 springboot 启动，端口为1212
    HelloController.java 是参考入口类

    路径为 com.suixingpay.security.DemoApplicationTests 的测试类，提供了模拟 http post 请求的方法，可参考

其中 必填项且名称必须为如下：
请求参数：sign reqData

响应参数：sign respData