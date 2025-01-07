package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.UserContext;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
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
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
}
