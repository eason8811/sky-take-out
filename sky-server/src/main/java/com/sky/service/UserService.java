package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

public interface UserService {

    /**
     * 用户登录
     *
     * @param userLoginDTO 传输用户登录信息的数据传输对象
     * @return 返回 UserLoginVO 的视图对象
     */
    UserLoginVO wxlogin(UserLoginDTO userLoginDTO);
}
