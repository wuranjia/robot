package com.ichat4j.wrj.robot.tuling;

import com.alibaba.fastjson.JSON;
import com.ichat4j.wrj.robot.ichat4j.Wechat;
import com.ichat4j.wrj.robot.ichat4j.beans.BaseMsg;
import com.ichat4j.wrj.robot.ichat4j.face.IMsgHandlerFace;
import com.ichat4j.wrj.robot.ichat4j.utils.HttpService;
import com.ichat4j.wrj.robot.tuling.entity.Perception;
import com.ichat4j.wrj.robot.tuling.entity.TuLingReq;
import com.ichat4j.wrj.robot.tuling.entity.TuLingResp;
import com.ichat4j.wrj.robot.tuling.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @version 1.0
 */
@Component
public class TulingRobot implements IMsgHandlerFace {

    @Autowired
    private HttpService httpService;
    String apiKey = "fecafa6e48cb4b50b69ee8e098d66e64"; //
    String userId = "12345";
    private static Logger LOG = LoggerFactory.getLogger(TulingRobot.class);
/*
    public static void main(String[] args) {
        IMsgHandlerFace msgHandler = new TulingRobot();
        Wechat wechat = new Wechat(msgHandler, "/Users/wuranjia/IdeaProjects/robot/src/main/resources/static");
        wechat.start();

    }*/

    @Override
    public String textMsgHandle(BaseMsg msg) {
        String text = msg.getText();
        String url = "http://www.tuling123.com/openapi/api/v2";
        TuLingReq tuLingReq = new TuLingReq();
        tuLingReq.setReqType("0");
        tuLingReq.setPerception(new Perception().addInputText(text));
        tuLingReq.setUserInfo(new UserInfo().key(apiKey).userId(userId));
        try {
            String params = JSON.toJSONString(tuLingReq);
            LOG.info("paramStr = " + params);
            ResponseEntity<TuLingResp> responseEntity = httpService.exchangeForPost(url, new HashMap<String, String>(), tuLingReq, new ParameterizedTypeReference<TuLingResp>() {
            });
            //HttpEntity entity = myHttpClient.doPost(url, params);
            String result = JSON.toJSONString(responseEntity);
            LOG.info("result = " + result);
            //JSONObject obj = JSON.parseObject(result);
            /*if (obj != null
                    && obj.getJSONArray("results") != null
                    && !obj.getJSONArray("results").isEmpty()) {
                JSONObject e = obj.getJSONArray("results").getJSONObject(0);
                result = e.getJSONObject("values").getString("text");
            } else {
                result = "小纪今天休息了，请明天再提问!";
            }*/
            if (responseEntity.getStatusCodeValue() == 200) {
                return responseEntity.getBody().getResults().get(0).getValues().getText();
            } else {
                return "小纪今天休息了，请明天再提问!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
            return "小纪正忙，请待会儿提问!";
        }
    }

    @Override
    public String picMsgHandle(BaseMsg msg) {
        return "收到图片";
    }

    @Override
    public String voiceMsgHandle(BaseMsg msg) {
        return "收到语音";
    }

    @Override
    public String videoMsgHandle(BaseMsg msg) {
        return "收到视频";
    }

    @Override
    public String nameCardMsgHandle(BaseMsg msg) {
        return "收到名片";
    }

    @Override
    public void sysMsgHandle(BaseMsg msg) {

    }

    @Override
    public String verifyAddFriendMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public String mediaMsgHandle(BaseMsg msg) {
        return null;
    }
}