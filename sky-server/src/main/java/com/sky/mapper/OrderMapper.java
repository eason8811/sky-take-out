package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 用户提交订单
     *
     * @param orders 用于新增订单的 Orders 实体类对象
     */
    void insert(Orders orders);

    /**
     * 分页查询订单信息
     *
     * @param ordersPageQueryDTO 用于传输分页查询参数的数据传输对象
     * @return 返回封装了 OrderVO 对象的 Page 集合
     */
    Page<OrderVO> list(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 修改订单信息
     *
     * @param orders 用于修改订单信息的实体类对象
     */
    void update(Orders orders);

    /**
     * 根据 ID 查询订单详细信息
     *
     * @param id 需要查询的订单 ID
     * @return 返回 OrderVO 的视图对象
     */
    OrderVO listById(Long id);

    /**
     * 获取不同订单状态的订单数量
     *
     * @return 返回封装了 Map 映射表对象的 List 对象
     */
    List<Map<String, Object>> statistics();

    /**
     * 根据订单 状态 和 检查时间 查询符合条件的超时订单
     *
     * @param status 查询的订单的状态
     * @param checkTime 查询的订单的检查时间
     */
    @Select("select * from orders where status = #{status} and order_time < #{checkTime};")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime checkTime);

    /**
     * 获取日期区间之内状态为 完成 的订单营业额
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @param status 状态为完成的订单
     * @return 返回日期区间内的营业额
     */
    Double getTurnover(LocalDateTime begin, LocalDateTime end, Integer status);

    /**
     * 获取日期区间之内的订单数目
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回日期区间内的订单数量
     */
    Integer getCount(LocalDateTime begin, LocalDateTime end);

    /**
     * 获取日期之内的有效订单数目
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @param status 订单状态
     * @return 返回日期区间内的有效订单数量
     */
    Integer getValidCount(LocalDateTime begin, LocalDateTime end, Integer status);

    /**
     * 获取日期区间之内的销量前 10 菜品信息
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回封装了 Map 集合对象的 List 集合
     */
    List<Map<String, Object>> getTop10(LocalDateTime begin, LocalDateTime end);
}
