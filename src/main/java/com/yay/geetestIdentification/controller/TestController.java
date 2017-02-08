package com.yay.geetestIdentification.controller;

import com.yay.geetestIdentification.utils.CasperjsProgramManager;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuananyun on 2016/7/8.
 */
@RestController
public class TestController {
    @Value("${server.address}")
    private String ip;
    @Value("${server.port}")
    private String port;
    @Value("${server.context-path}")
    private String contextPath;

    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "testIdentification", method = RequestMethod.GET)
    public Object test(@RequestParam int tryCount) {
        int totalCount = tryCount;//测试的次数
        int successCount = 0;
        int retryCount = 0;
        String pageUrl = "http://user.geetest.com/login?url=http:%2F%2Faccount.geetest.com%2Freport";
        String deltaResolveAddress = String.format("http://%s:%s/%s/resolveGeetestSlicePosition", ip, port, contextPath);
        StopWatch stopWatch = new StopWatch();
        for (int i = 0; i < totalCount; i++) {
            stopWatch.reset();
            stopWatch.start();
            if (startIdentification(pageUrl, "geetest_refresh.js", deltaResolveAddress))
                successCount++;
            else {
                int t = retryCount;
                while (t > 0) {
                    System.out.println("重试一次");
                    if (startIdentification(pageUrl, "geetest_refresh.js", deltaResolveAddress)) {
                        successCount++;
                        break;
                    }
                    t--;
                }
            }
            stopWatch.stop();
            logger.debug("本次调用耗时：(毫秒)" + stopWatch.getTime());
        }
        String result = "调用" + totalCount + "次，失败重试" + retryCount + "次的情况下，共成功" + successCount + "次";
        logger.debug(result);
        return result;
    }

    /**
     * 验证码自动识别     *
     *
     * @param pageUrl             包含验证码的页面url
     * @param jsFileName          要执行的js文件的名称
     * @param deltaResolveAddress 能够解析验证码移动位移的服务地址
     * @return
     */
    private static boolean startIdentification(String pageUrl, String jsFileName, String deltaResolveAddress) {
        String result = CasperjsProgramManager.launch(jsFileName, pageUrl, deltaResolveAddress, " web-security=no", "ignore-ssl-errors=true");
        logger.info("验证码识别结果：\r\n" + result);
        return result != null && (result.contains("验证通过") || result.contains("不存在极验验证码"));
    }

    public static void main(String[] args) {
        int totalCount = 20;//测试的次数
        int successCount = 0;
        int retryCount = 0;
        String pageUrl = "http://user.geetest.com/login?url=http:%2F%2Faccount.geetest.com%2Freport";
        String deltaResolveAddress = String.format("http://%s:%s/%s/resolveGeetestSlicePosition", "127.0.0.1", 8068, "/geetest");
        StopWatch stopWatch = new StopWatch();
        for (int i = 0; i < totalCount; i++) {
            stopWatch.reset();
            stopWatch.start();
            if (startIdentification(pageUrl, "geetest_refresh.js", deltaResolveAddress))
                successCount++;
            else {
                int t = retryCount;
                while (t > 0) {
                    System.out.println("重试一次");
                    if (startIdentification(pageUrl, "geetest_refresh.js", deltaResolveAddress)) {
                        successCount++;
                        break;
                    }
                    t--;
                }
            }
            stopWatch.stop();
            logger.debug("本次调用耗时：(毫秒)" + stopWatch.getTime());
        }
        String result = "调用" + totalCount + "次，失败重试" + retryCount + "次的情况下，共成功" + successCount + "次";
        logger.debug(result);
    }
}
