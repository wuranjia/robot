package com.ichat4j.wrj.robot.ichat4j;

import com.ichat4j.wrj.robot.ichat4j.controller.LoginController;
import com.ichat4j.wrj.robot.ichat4j.core.MsgCenter;
import com.ichat4j.wrj.robot.ichat4j.face.IMsgHandlerFace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Wechat {
    private static final Logger LOG = LoggerFactory.getLogger(Wechat.class);
    @Autowired
    private IMsgHandlerFace msgHandler;


    public Wechat login(String qrPath, String filePath) {
        System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
        LoginController login = new LoginController();
        // 登陆
        login.login(qrPath,filePath);
        return this;
    }

    public void start() {
        LOG.info("+++++++++++++++++++开始消息处理+++++++++++++++++++++");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MsgCenter.handleMsg(msgHandler);
            }
        }).start();
    }

}
