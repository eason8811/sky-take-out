package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 用户登录
     *
     * @param userLoginDTO 传输用户登录信息的数据传输对象
     * @return 返回 UserLoginVO 的视图对象
     */
    @Override
    public UserLoginVO wxlogin(UserLoginDTO userLoginDTO) {
        // 构建发送到微信API的请求
        String openid = getOpenid(userLoginDTO.getCode());

        // 在数据库中查询是否有该用户的 openid
        User user = userMapper.listByOpenId(openid);

        // 判断是否存在该用户
        if (user == null) {
            // 如果不存在，则注册用户
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        // 生成 jwt 令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        return UserLoginVO.builder()
                .id(user.getId())
                .token(token)
                .openid(user.getOpenid())
                .build();
    }

    /**
     * 通过微信API获取openId
     *
     * @param code js_code
     * @return 返回 openId
     */
    private String getOpenid(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session", map);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
