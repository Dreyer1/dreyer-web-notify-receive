package com.dreyer.web.notify.receive.mail.biz;

import com.dreyer.common.params.MailParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author: Dreyer
 * @date: 16/6/14 上午11:21
 * @description 邮件服务
 */
@Component
public class MailBiz {
    /**
     * 邮件发送,spring配置中定义
     */
    @Autowired
    private JavaMailSender mailSender;
    /**
     * 邮件发送,spring配置中定义
     */
    @Autowired
    private SimpleMailMessage simpleMailMessage;
    /**
     * Spring线程池,spring配置中定义
     */
    @Autowired
    private ThreadPoolTaskExecutor threadPool;

    private Logger logger = Logger.getLogger(MailBiz.class);


    /**
     * 发送邮件,使用线程池执行具体的业务逻辑,要考虑对异常情况的处理
     *
     * @param mailParam
     */
    public void mailSend(final MailParam mailParam) {
        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                simpleMailMessage.setFrom(simpleMailMessage.getFrom()); // 发送人,从配置文件中取得
                simpleMailMessage.setTo(mailParam.getTo()); // 接收人
                simpleMailMessage.setSubject(mailParam.getSubject());
                simpleMailMessage.setText(mailParam.getContent());
//                mailSender.send(simpleMailMessage);
                return "ok";
            }
        });
        threadPool.execute(futureTask);
        try {
            String result = futureTask.get();
        } catch (InterruptedException e) {
            logger.error("发送邮件异常!", e);
            throw new RuntimeException();
        } catch (ExecutionException e) {
            logger.error("发送邮件异常!", e);
            throw new RuntimeException();
        }
    }

}
