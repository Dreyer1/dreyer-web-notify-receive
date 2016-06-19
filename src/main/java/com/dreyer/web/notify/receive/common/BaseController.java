package com.dreyer.web.notify.receive.common;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: Dreyer
 * @date: 16/6/14 上午11:27
 * @description 控制器基类
 */
public class BaseController {

    private Logger logger = Logger.getLogger(BaseController.class);
    /**
     * 返回成功的值
     */
    public final String SUCCESS = "success";

    /**
     * 返回失败的值
     */
    public final String FAIL = "fail";

    /**
     * 输出
     *
     * @param response
     * @param msg
     */
    public void write(HttpServletResponse response, String msg) {
        PrintWriter write = null;
        try {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            write = response.getWriter();
            write.write(msg);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            write.close();
        }
    }
}
