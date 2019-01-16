package com.ichat4j.wrj.robot.ichat4j.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ichat4j.wrj.robot.tuling.entity.Perception;
import com.ichat4j.wrj.robot.tuling.entity.TuLingReq;
import com.ichat4j.wrj.robot.tuling.entity.UserInfo;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * HTTP访问类，对Apache HttpClient进行简单封装，适配器模式
 *
 * @author
 * @version 1.0
 * @date 创建时间：2017年4月9日 下午7:05:04
 */
public class MyHttpClient {
    private static Logger LOG = LoggerFactory.getLogger(MyHttpClient.class);

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    private static MyHttpClient instance = null;

    private static CookieStore cookieStore;

    static {
        cookieStore = new BasicCookieStore();

        // 将CookieStore设置到httpClient中
        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }

    public static String getCookie(String name) {
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                return cookie.getValue();
            }
        }
        return null;

    }

    private MyHttpClient() {

    }

    /**
     * 获取cookies
     *
     * @return
     * @author
     * @date 2017年5月7日 下午8:37:17
     */
    public static MyHttpClient getInstance() {
        if (instance == null) {
            synchronized (MyHttpClient.class) {
                if (instance == null) {
                    instance = new MyHttpClient();
                }
            }
        }
        return instance;
    }

    /**
     * 处理GET请求
     *
     * @param url
     * @param params
     * @return
     * @author
     * @date 2017年4月9日 下午7:06:19
     */
    public HttpEntity doGet(String url, List<BasicNameValuePair> params, boolean redirect,
                            Map<String, String> headerMap) {
        HttpEntity entity = null;
        HttpGet httpGet = new HttpGet();

        try {
            if (params != null) {
                String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
                httpGet = new HttpGet(url + "?" + paramStr);
            } else {
                httpGet = new HttpGet(url);
            }
            if (!redirect) {
                httpGet.setConfig(RequestConfig.custom().setRedirectsEnabled(false).build()); // 禁止重定向
            }
            httpGet.setHeader("User-Agent", Config.USER_AGENT);
            if (headerMap != null) {
                Set<Entry<String, String>> entries = headerMap.entrySet();
                for (Entry<String, String> entry : entries) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            CloseableHttpResponse response = httpClient.execute(httpGet);
            entity = response.getEntity();
        } catch (ClientProtocolException e) {
            LOG.info(e.getMessage());
        } catch (IOException e) {
            LOG.info(e.getMessage());
        }

        return entity;
    }

    /**
     * 处理POST请求
     *
     * @param url
     * @param paramsStr
     * @return
     * @author
     * @date 2017年4月9日 下午7:06:35
     */
    public HttpEntity doPost(String url, String paramsStr) {
        HttpEntity entity = null;
        HttpPost httpPost;
        try {
            StringEntity params = new StringEntity(paramsStr, Consts.UTF_8);
            httpPost = new HttpPost(url);
            httpPost.setEntity(params);
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            //httpPost.setHeader("User-Agent", Config.USER_AGENT);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            LOG.info("response = " + JSON.toJSONString(response));
            entity = response.getEntity();
        } catch (ClientProtocolException e) {
            LOG.info(e.getMessage());
        } catch (IOException e) {
            LOG.info(e.getMessage());
        }

        return entity;
    }

    /**
     * 上传文件到服务器
     *
     * @param url
     * @param reqEntity
     * @return
     * @author
     * @date 2017年5月7日 下午9:19:23
     */
    public HttpEntity doPostFile(String url, HttpEntity reqEntity) {
        HttpEntity entity = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", Config.USER_AGENT);
        httpPost.setEntity(reqEntity);
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            entity = response.getEntity();
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return entity;
    }

    public static CloseableHttpClient getHttpClient() {
        return httpClient;
    }


    public static void main(String[] args) {
        MyHttpClient myHttpClient = MyHttpClient.getInstance();
        String apiKey = "fecafa6e48cb4b50b69ee8e098d66e64"; //
        String userId = "12345";
        String result;
        String text = "123";
        String url = "http://www.tuling123.com/openapi/api/v2";
        TuLingReq tuLingReq = new TuLingReq();
        tuLingReq.setReqType("0");
        tuLingReq.setPerception(new Perception().addInputText(text));
        tuLingReq.setUserInfo(new UserInfo().key(apiKey).userId(userId));
        try {
            String params = JSON.toJSONString(tuLingReq);
            LOG.info("paramStr = " + params);
            HttpEntity entity = myHttpClient.doPost(url, params);
            result = JSON.toJSONString(entity);
            LOG.info("result = " + result);
            JSONObject obj = JSON.parseObject(result);
            if (obj != null
                    && obj.getJSONArray("results") != null
                    && !obj.getJSONArray("results").isEmpty()) {
                JSONObject e = obj.getJSONArray("results").getJSONObject(0);
                result = e.getJSONObject("values").getString("text");
            } else {
                result = "小纪今天休息了，请明天再提问!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }
    }

}