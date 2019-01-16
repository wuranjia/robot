package com.ichat4j.wrj.robot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ichat4j.wrj.robot.domain.*;
import com.ichat4j.wrj.robot.ichat4j.Wechat;
import com.ichat4j.wrj.robot.ichat4j.api.WechatTools;
import com.ichat4j.wrj.robot.ichat4j.core.Core;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/wechat")
public class WechatController {
    @Autowired
    private Wechat wechat;
    @Value("${qr.path:/Users/wuranjia/IdeaProjects/robot/src/main/resources/static/}")
    private String filePath;

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public String start(@RequestParam("fileName") String fileName) {
        //Boolean result = this.service.sync(request);

        Core core = Core.getInstance();
        if(core.isAlive()){
            return "alive";
        }else{
            String path = filePath + fileName + ".jpg";
            wechat.login(path,filePath).start();
        }
        return "success";
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String check() {
        Core core = Core.getInstance();
        if(core.isAlive()){
            return "success";
        }else{
            return "failed";
        }
    }

    @RequestMapping(value = "/head", method = RequestMethod.GET)
    public String getHead() {
        File file = new File(filePath+"head.jpg");
        if(file.exists()){
            return "success";
        }else{
            return "failed";
        }
    }

    @RequestMapping(value = "/contactList", method = RequestMethod.GET)
    public String contactList() {
        //Boolean result = this.service.sync(request);
        Core core = Core.getInstance();
        List<JSONObject> list = core.getContactList();
        List<String> filterList = core.getFilterList();
        List<ContactDto> result = new ArrayList<ContactDto>();
        for (JSONObject jsonObject : list) {
            String nickName = jsonObject.getString("NickName");
            String remarkName = jsonObject.getString("RemarkName");
            if (Common.nickNameSet.contains(nickName)) {
                String checked = "unchecked";
                if (StringUtils.isBlank(remarkName)) remarkName = nickName;
                if (filterList.contains(nickName)) checked = "checked";
                ContactDto contactDto = new ContactDto().nickName(nickName).remarkName(remarkName).check(checked);
                result.add(contactDto);
            }
        }
        ContactResp resp = new ContactResp();
        if (result.isEmpty()) {
            resp.setCode("error");
        } else {
            resp.setCode("success");
        }
        resp.setData(result);
        return JSON.toJSONString(resp);
    }

    @RequestMapping(value = "/addAll", method = RequestMethod.GET)
    public String memberList() {
        //Boolean result = this.service.sync(request);
        Core core = Core.getInstance();
        List<JSONObject> list = core.getContactList();
        List<String> result = new ArrayList<String>();
        for (JSONObject jsonObject : list) {
            String nickName = jsonObject.getString("NickName");
            if (Common.nickNameSet.contains(nickName)) {
                result.add(nickName);
            }
        }
        core.setFilterList(result);
        return "update all true";
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String queryStatus() {
        //Boolean result = this.service.sync(request);
        Core core = Core.getInstance();
        StatusResp statusResp = new StatusResp();
        if (core.isAlive()) {
            statusResp.setLogin("online");
        } else {
            statusResp.setLogin("offline");
        }
        if (core.isAlive()) {
            statusResp.setLoad("ready");
        } else {
            statusResp.setLoad("unready");
        }
        return JSON.toJSONString(statusResp);
    }

    @RequestMapping(value = "/delAll", method = RequestMethod.GET)
    public String delAllList() {
        //Boolean result = this.service.sync(request);
        Core core = Core.getInstance();
        core.setFilterList(new ArrayList<String>());
        return "update all true";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String robotList(@ModelAttribute String nickName) {
        Core core = Core.getInstance();
        return JSON.toJSONString(core.getMemberList());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateRobot(@RequestBody UpdateReq updateReq) {
        String[] array = updateReq.getNickNames().split(",");
        List<String> list = Arrays.asList(array);
        Core core = Core.getInstance();
        core.setFilterList(list);
        return "success";
    }

}


