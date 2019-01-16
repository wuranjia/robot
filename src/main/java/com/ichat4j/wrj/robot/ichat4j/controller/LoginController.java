package com.ichat4j.wrj.robot.ichat4j.controller;

import com.ichat4j.wrj.robot.ichat4j.core.Core;
import com.ichat4j.wrj.robot.ichat4j.service.ILoginService;
import com.ichat4j.wrj.robot.ichat4j.service.impl.LoginServiceImpl;
import com.ichat4j.wrj.robot.ichat4j.thread.LoginConsumerThread;
import com.ichat4j.wrj.robot.ichat4j.utils.SleepUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登陆控制器
 *
 * @author
 * @version 1.0
 * @date 创建时间：2017年5月13日 下午12:56:07
 */
public class LoginController {
    private static Logger LOG = LoggerFactory.getLogger(LoginController.class);
    private ILoginService loginService = new LoginServiceImpl();
    private static Core core = Core.getInstance();

    public void login(String qrPath, String filePath) {
        if (core.isAlive()) { // 已登陆
            LOG.info("itchat4j已登陆");
            return;
        }
        for (int count = 0; count < 10; count++) {
            LOG.info("获取UUID");
            while (loginService.getUuid() == null) {
                LOG.info("1. 获取微信UUID");
                while (loginService.getUuid() == null) {
                    LOG.warn("1.1. 获取微信UUID失败，两秒后重新获取");
                    SleepUtils.sleep(2000);
                }
            }
            LOG.info("2. 获取登陆二维码图片");
            if (loginService.getQR(qrPath)) {
                break;
            } else if (count == 10) {
                LOG.error("2.2. 获取登陆二维码图片失败，系统退出");
                System.exit(0);
            }
        }
        core.setPath(filePath);
        LOG.info("3. 请扫描二维码图片，并在手机上确认");
        new Thread(new LoginConsumerThread(loginService)).start();

    }
}