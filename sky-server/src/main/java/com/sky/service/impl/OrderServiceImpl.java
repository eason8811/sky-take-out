package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.UserContext;
import com.sky.dto.*;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 用户提交订单
     *
     * @param orders 用于传输订单数据的实体类对象
     * @return 返回 OrderSubmitVO 视图对象
     */
    @Override
    @Transactional
    public OrderSubmitVO submit(Orders orders) {
        // 若地址为空则抛出异常
        // 根据地址 ID addressBookId 查询地址簿信息
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressMapper.listById(addressBookId);
        if (addressBook == null) {
            log.error("传入数据的地址信息不合法, addressBookId 为: {}", addressBookId);
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 若购物车为空则抛出异常
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(UserContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            log.error("购物车详细信息不合法, shoppingCartList 为空");
            throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 若无异常则插入数据
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(UserContext.getCurrentId());
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName()
                + addressBook.getDistrictName() + addressBook.getDetail());
        orders.setOrderTime(LocalDateTime.now());
        orderMapper.insert(orders);

        // 插入订单详细信息数据
        List<OrderDetail> orderDetailList = shoppingCartList.stream()
                .map(item -> {
                    OrderDetail orderDetail = new OrderDetail();
                    BeanUtils.copyProperties(item, orderDetail);
                    orderDetail.setOrderId(orders.getId());
                    return orderDetail;
                })
                .toList();
        orderDetailMapper.insertBatch(orderDetailList);

        // 删除用户的购物车数据
        shoppingCartMapper.delete(List.of(shoppingCart));

        // 返回 OrderSubmitVO 视图对象
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    /**
     * 用户进行订单支付
     *
     * @param ordersPaymentDTO 用于传输用户支付的订单信息的数据传输对象
     * @return 返回 OrderPaymentVO 的视图对象
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        // 将订单 status 改为已付款
        Orders orders = new Orders();
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setPayStatus(Orders.PAID);
        orders.setPayMethod(ordersPaymentDTO.getPayMethod());
        orders.setNumber(ordersPaymentDTO.getOrderNumber());
        orders.setUserId(UserContext.getCurrentId());
        orderMapper.update(orders);

        wsSendToAllClient(1, Long.valueOf(orders.getNumber()), orders.getNumber());

        return OrderPaymentVO.builder()
                .nonceStr("abcdeason0x709394")
                .paySign("s465g3s5b6r4hg35s46h3gsg1sr365g43s651gs56")
                .timeStamp(String.valueOf(System.currentTimeMillis()))
                .signType("RSA")
                .packageStr("siefjosingish")
                .build();
    }

    /**
     * 分页查询订单信息
     *
     * @param ordersPageQueryDTO 用于传输分页查询参数的数据传输对象
     * @return 返回封装了 OrderVO 对象的 Page 集合的 PageResult 对象
     */
    @Override
    public PageResult list(OrdersPageQueryDTO ordersPageQueryDTO) {
        ordersPageQueryDTO.setUserId(UserContext.getCurrentId());
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<OrderVO> orderVOPage = orderMapper.list(ordersPageQueryDTO);
        return new PageResult(orderVOPage.getTotal(), orderVOPage.getResult());
    }

    /**
     * 分页查询订单信息（管理员版）
     *
     * @param ordersPageQueryDTO 用于传输分页查询参数的数据传输对象
     * @param isAdmin            是否是管理员
     * @return 返回封装了 OrderVO 对象的 Page 集合的 PageResult 对象
     */
    @Override
    public PageResult list(OrdersPageQueryDTO ordersPageQueryDTO, boolean isAdmin) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<OrderVO> orderVOPage = orderMapper.list(ordersPageQueryDTO);
        return new PageResult(orderVOPage.getTotal(), orderVOPage.getResult());
    }

    /**
     * 根据 ID 查询订单详细信息
     *
     * @param id 需要查询的订单 ID
     * @return 返回 OrderVO 的视图对象
     */
    @Override
    public OrderVO listById(Long id) {
        return orderMapper.listById(id);
    }

    /**
     * 根据订单 ID 取消订单
     *
     * @param id 需要取消的订单 ID
     */
    @Override
    public void cancel(Long id) {
        OrderVO orderVO = orderMapper.listById(id);
        if (orderVO.getStatus() >= Orders.TO_BE_CONFIRMED){
            // 若在待接单及以后状态下取消订单，需要退款
            Orders orders = Orders.builder()
                    .id(id)
                    .userId(UserContext.getCurrentId())
                    .status(Orders.CANCELLED)
                    .payStatus(Orders.REFUND)
                    .build();
            orderMapper.update(orders);
            return;
        }
        // 待付款情况下不需要退款
        Orders orders = Orders.builder()
                .id(id)
                .userId(UserContext.getCurrentId())
                .status(Orders.CANCELLED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 根据订单 ID 取消订单
     *
     * @param ordersCancelDTO 需要取消的订单 ID
     */
    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        OrderVO orderVO = orderMapper.listById(ordersCancelDTO.getId());
        if (orderVO.getPayStatus().equals(Orders.PAID)){
            // 用户已经付款，需要进行退款
            Orders orders = Orders.builder()
                    .id(ordersCancelDTO.getId())
                    .cancelReason(ordersCancelDTO.getCancelReason())
                    .status(Orders.CANCELLED)
                    .payStatus(Orders.REFUND)
                    .build();
            orderMapper.update(orders);
            return;
        }
        // 用户未进行付款，无需进行退款
        Orders orders = Orders.builder()
                .id(ordersCancelDTO.getId())
                .cancelReason(ordersCancelDTO.getCancelReason())
                .status(Orders.CANCELLED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 根据订单 ID 进行再来一单
     *
     * @param id 需要再来一单的订单 ID
     */
    @Override
    @Transactional
    public void repetition(Long id) {
        // 获取 ordersDetail 集合对象
        OrderVO orderVO = orderMapper.listById(id);
        Orders orders = new Orders();
        BeanUtils.copyProperties(orderVO, orders);
        List<OrderDetail> orderDetailList = orderVO.getOrderDetailList();
        Long userId = UserContext.getCurrentId();

        // 将 orderDetailList 集合对象中的 ordersDetail 对象转换为 ShoppingCart 对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream()
                .map(orderDetail -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    BeanUtils.copyProperties(orderDetail, shoppingCart);
                    shoppingCart.setUserId(userId);
                    return shoppingCart;
                })
                .toList();

        // 将购物车集合对象 shoppingCartList 插入数据库中
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 进行订单信息统计
     *
     * @return 返回 OrderStatisticsVO 的订单统计的视图对象
     */
    @Override
    public OrderStatisticsVO statistics() {
        List<Map<String, Object>> list = orderMapper.statistics();
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        list.forEach(map -> {
            if (map.get("status").equals("toBeConfirmed"))
                orderStatisticsVO.setToBeConfirmed((Long) map.get("count"));
            else if (map.get("status").equals("confirmed"))
                orderStatisticsVO.setConfirmed((Long) map.get("count"));
            else if (map.get("status").equals("deliveryInProgress"))
                orderStatisticsVO.setDeliveryInProgress((Long) map.get("count"));
        });
        return orderStatisticsVO;
    }

    /**
     * 根据订单 ID 进行接单
     *
     * @param ordersDTO 接收需要接单的订单 ID 的数据传输对象
     */
    @Override
    public void accept(OrdersDTO ordersDTO) {
        Orders orders = Orders.builder()
                .id(ordersDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 根据订单 ID 进行拒单
     *
     * @param ordersRejectionDTO 用于接收拒单信息的数据传输对象
     */
    @Override
    public void reject(OrdersRejectionDTO ordersRejectionDTO) {
        OrderVO orderVO = orderMapper.listById(ordersRejectionDTO.getId());
        if (!orderVO.getStatus().equals(Orders.TO_BE_CONFIRMED)){
            // 如果当前订单状态不是 待接单 则无法拒单
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 若订单的支付状态为已支付，则需要退款
        if (orderVO.getPayStatus().equals(Orders.PAID)){
            Orders orders = Orders.builder()
                    .id(ordersRejectionDTO.getId())
                    .status(Orders.CANCELLED)
                    .payStatus(Orders.REFUND)
                    .rejectionReason(ordersRejectionDTO.getRejectionReason())
                    .build();
            orderMapper.update(orders);
            return;
        }
        Orders orders = Orders.builder()
                .id(ordersRejectionDTO.getId())
                .status(Orders.CANCELLED)
                .rejectionReason(ordersRejectionDTO.getRejectionReason())
                .build();
        orderMapper.update(orders);
    }

    /**
     * 派送订单
     *
     * @param id 需要派送的订单 ID
     */
    @Override
    public void delivery(Long id) {
        OrderVO orderVO = orderMapper.listById(id);
        if (orderVO.getStatus() >= Orders.DELIVERY_IN_PROGRESS) {
            // 若订单状态处于派送中及之后的状态，则无法进行派送，抛出异常
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 否则修改订单状态为派送中
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.DELIVERY_IN_PROGRESS)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 完成订单
     *
     * @param id 需要完成的订单的 ID
     */
    @Override
    public void complete(Long id) {
        OrderVO orderVO = orderMapper.listById(id);
        if (!orderVO.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            // 只有派送中的订单可以进行 完成订单 操作，否则抛出异常
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.COMPLETED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 根据 ID 进行催单
     *
     * @param id 需要进行催单的订单 ID
     */
    @Override
    public void reminder(Long id) {
        OrderVO orderVO = orderMapper.listById(id);

        // 通过 websocket 群发到所有客户端
        wsSendToAllClient(2, orderVO.getId(), orderVO.getNumber());
    }

    private void wsSendToAllClient(Integer type, Long orderId, String number){
        // 使用 websocket 向所有客户端推送来单信息
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("orderId", orderId);
        map.put("content", number);

        // 将 map 对象转换为 JSON 字符串 并通过 websocket 群发到所有客户端
        String jsonString = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(jsonString);
    }
}
