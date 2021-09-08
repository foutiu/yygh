package com.z.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.z.yygh.common.exception.YyghException;
import com.z.yygh.common.helper.JwtHelper;
import com.z.yygh.common.result.Result;
import com.z.yygh.common.result.ResultCodeEnum;
import com.z.yygh.model.user.UserInfo;
import com.z.yygh.user.service.UserInfoService;
import com.z.yygh.user.utils.ConstantWxPropertiesUtil;
import com.z.yygh.user.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

//微信操作接口
@Controller
@Slf4j
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    @Autowired
    private UserInfoService userInfoService;

    //微信扫描后回调的方法
    @GetMapping("callback")
    public String callback(String code,String state){
        //获取授权临时票据
        System.out.println("微信授权服务器回调。。。。。。");
        System.out.println("state = " + state);
        System.out.println("code = " + code);

        if (StringUtils.isEmpty(state) || StringUtils.isEmpty(code)) {
            log.error("非法回调请求");
            throw new YyghException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //使用code和appid以及appscrect换取access_token
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantWxPropertiesUtil.WX_OPEN_APP_ID,
                ConstantWxPropertiesUtil.WX_OPEN_APP_SECRET,
                code);
        //使用httpclient请求这个地址
        try {
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo:"+accessTokenInfo);
            //从返回字符串中获取两个值
            JSONObject jsonObject = JSONObject.parseObject(accessTokenInfo);
            String accessToken = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");

            //判断数据库是否存在维修的扫描人信息
            //根据openid判断
            UserInfo userInfo = userInfoService.selectWxInfoOpendId(openid);
            if (null == userInfo){
                //拿着openid和access_token请求微信地址，得到扫码入信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
                String resultInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultInfo:"+resultInfo);
                JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);
                //解析用户信息
                //昵称
                String nickname = resultUserInfoJson.getString("nickname");
                //头像
                String headimgurl = resultUserInfoJson.getString("headimgurl");

                //获取扫码人信息添加数据库
                userInfo = new UserInfo();
                userInfo.setNickName(nickname);
                userInfo.setOpenid(openid);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }

            //返回name和token字符串
            Map<String, Object> map = new HashMap<>();
            String name = userInfo.getName();
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);
            //判断userInfo中是否有手机号，如果手机号为空，返回openid
            //如果手机号不为空，返回openid值是空字符串
            //前端判断：如果openid不为空，绑定手机号，为空不绑定手机号
            if(StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }
            //使用jwp生成token
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            return "redirect:" + ConstantWxPropertiesUtil.YYGH_BASE_URL + "/weixin/callback?token="+map.get("token")+"&openid="+map.get("openid")+"&name="+URLEncoder.encode((String)map.get("name"),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //1 生成微信扫描二维码
    //返回生成二维码需要参数
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect(){
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appid", ConstantWxPropertiesUtil.WX_OPEN_APP_ID);
            map.put("scope","snsapi_login");
            String wxOpenRedirectUrl = ConstantWxPropertiesUtil.WX_OPEN_REDIRECT_URL;
            wxOpenRedirectUrl = URLEncoder.encode(wxOpenRedirectUrl, "utf-8");
            map.put("redirect_uri",wxOpenRedirectUrl);
            map.put("state",System.currentTimeMillis()+"");
            return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }




    //2 获取扫码人信息




}
