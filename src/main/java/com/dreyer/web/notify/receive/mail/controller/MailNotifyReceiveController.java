package com.dreyer.web.notify.receive.mail.controller;

import com.dreyer.common.params.MailParam;
import com.dreyer.common.util.JacksonUtil;
import com.dreyer.web.notify.receive.common.BaseController;
import com.dreyer.web.notify.receive.mail.biz.MailBiz;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: Dreyer
 * @date: 16/6/14 上午10:42
 * @description 邮件消息通知接收
 */
@Controller
@RequestMapping("/mail")
public class MailNotifyReceiveController extends BaseController {
    @Autowired
    private MailBiz mailBiz;

    private Logger logger = Logger.getLogger(MailNotifyReceiveController.class);


    /**
     * 邮件消息通知接收
     *
     * @param request
     * @param response
     */
    @RequestMapping("/receive")
    public String mailMessageReceive(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String msgBase64 = request.getParameter("msg");
        if (StringUtils.isBlank(msgBase64)) {
            throw new RuntimeException("通知请求参数[msg]为空");
        }
        String json = new String(Base64.decodeBase64(msgBase64));
        logger.info("mail msg receive:" + json);
        MailParam mailParam = (MailParam) JacksonUtil.jsonToObject(json, MailParam.class);
        if (mailParam == null) {
            throw new RuntimeException("通知请求参数[msg]转换的对象为空");
        }
        try {
            mailBiz.mailSend(mailParam);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送邮件出错", e);
            this.write(response, this.FAIL);
            return null;
        }
        // 回写成功字符串
        this.write(response, this.SUCCESS);
        return null;
    }


}
