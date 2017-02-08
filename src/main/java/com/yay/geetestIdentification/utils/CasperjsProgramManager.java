package com.yay.geetestIdentification.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Semaphore;

/**
 * 管理Casperjs的启动和执行
 *
 */
public class CasperjsProgramManager {
    private static Logger logger = LoggerFactory.getLogger(CasperjsProgramManager.class);
    private static final Semaphore semaphore = new Semaphore(10, true);

    public static String launch(String jsFileName, Object... params) {
        if (StringUtils.isBlank(jsFileName)) {
            logger.error("待执行的js文件名不能为空！");
            return null;
        }
        try {
            semaphore.acquire();
            String path = CasperjsProgramManager.class.getResource("/").getPath();
            path = path.substring(1, path.lastIndexOf("/") + 1);
            String os = System.getProperties().getProperty("os.name");
            String casperJsPath = "";
            String phantomJsPath = "";
            if (StringUtils.startsWithIgnoreCase(os, "win")) {
                casperJsPath = path + "casperjs/bin/casperjs.exe";
                phantomJsPath = path + "phantomjs/window/phantomjs.exe";
            } else {
                casperJsPath = path + "casperjs/bin/casperjs";
                phantomJsPath = path + "phantomjs/linux/phantomjs";
            }
            logger.info("CasperJs程序地址:{}", casperJsPath);
            ProcessBuilder processBuilder = new ProcessBuilder(casperJsPath, jsFileName);
            if (params != null) {
                for (Object param : params) {
                    processBuilder.command().add(String.valueOf(param));
                }
            }
            processBuilder.directory(new File(path + "casperjs/js"));
            processBuilder.environment().put("PHANTOMJS_EXECUTABLE", phantomJsPath);

            Process p = processBuilder.start();
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sbf = new StringBuffer();
            String tmp = "";
            while ((tmp = br.readLine()) != null) {
                sbf.append(tmp).append("\r\n");
            }
            p.destroy();
            semaphore.release();
            return sbf.toString();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }

}
