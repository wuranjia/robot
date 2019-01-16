package com.ichat4j.wrj.robot.ichat4j.thread;

import com.ichat4j.wrj.robot.ichat4j.api.WechatTools;
import com.ichat4j.wrj.robot.ichat4j.core.Core;
import com.ichat4j.wrj.robot.ichat4j.service.ILoginService;
import com.ichat4j.wrj.robot.ichat4j.utils.tools.CommonTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginConsumerThread implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(LoginConsumerThread.class);
    private Core core = Core.getInstance();
    private ILoginService loginService;

    public LoginConsumerThread(ILoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void run() {
        while (true) {
            if (!core.isAlive()) {
                loginService.login();
                core.setAlive(true);
                LOG.info(("登陆成功"));
                break;
            }
            LOG.info("4. 登陆超时，请重新扫描二维码图片");
        }

        LOG.info("5. 登陆成功，微信初始化");
        if (!loginService.webWxInit()) {
            LOG.info("6. 微信初始化异常");
            System.exit(0);
        }

        LOG.info("6. 开启微信状态通知");
        loginService.wxStatusNotify();

        LOG.info("7. 清除。。。。");
        CommonTools.clearScreen();
        LOG.info(String.format("欢迎回来， %s", core.getNickName()));

        LOG.info("8. 开始接收消息");
        loginService.startReceiving();

        LOG.info("9. 获取联系人信息");
        loginService.webWxGetContact();



        LOG.info("10. 获取群好友及群好友列表");
        loginService.WebWxBatchGetContact();

        LOG.info("11. 缓存本次登陆好友相关消息");
        WechatTools.setUserInfo(); // 登陆成功后缓存本次登陆好友相关消息（NickName, UserName）

        LOG.info("11.1 下载头像图片");
        loginService.getHeadImage();

        LOG.info("12.开启微信状态检测线程");
        core.setLoaded(true);
        new Thread(new CheckLoginStatusThread()).start();
    }
}
