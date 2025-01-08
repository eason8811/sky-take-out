package com.sky.controller.admin;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单信息
     *
     * @param ordersPageQueryDTO 用于传输查询订单信息的参数的数据传输对象
     * @return 返回Result格式的对象
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> list(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("正在分页查询订单信息, 参数为: {}", ordersPageQueryDTO);
        PageResult orderList = orderService.list(ordersPageQueryDTO, true);
        return Result.success(orderList);
    }

    /**
     * 进行订单信息统计
     *
     * @return 返回Result格式的对象
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        log.info("正在进行订单统计");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 根据 ID 查询订单信息
     *
     * @param id 需要查询的订单 ID
     * @return 返回Result格式的对象
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> listById(@PathVariable Long id) {
        log.info("正在根据 ID 查询订单详细信息, ID 为: {}", id);
        OrderVO orderVO = orderService.listById(id);
        return Result.success(orderVO);
    }

    /**
     * 根据订单 ID 进行接单
     *
     * @param ordersDTO 接收需要接单的订单 ID 的数据传输对象
     * @return 返回Result格式的对象
     */
    @PutMapping("/confirm")
    public Result<Object> accept(@RequestBody OrdersDTO ordersDTO) {
        log.info("正在接单, 参数为: {}", ordersDTO);
        orderService.accept(ordersDTO);
        return Result.success();
    }

    /**
     * 根据订单 ID 进行拒单
     *
     * @param ordersRejectionDTO 用于接收拒单信息的数据传输对象
     * @return 返回Result格式的对象
     */
    @PutMapping("/rejection")
    public Result<Object> reject(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("正在拒单, 参数为: {}", ordersRejectionDTO);
        orderService.reject(ordersRejectionDTO);
        return Result.success();
    }

}
