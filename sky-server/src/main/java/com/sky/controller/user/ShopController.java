package com.sky.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private static final String KEY = "SHOP_STATUS";

    /**
     * 查询店铺状态
     *
     * @return 返回Result格式的对象
     */
    @GetMapping("/status")
    public Result<Integer> get(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        if (status != null) {
            log.info("获取店铺营业状态, 状态为: {}", status.equals(1) ? "营业中" : "打烊");
        }
        return Result.success(status);
    }
}
