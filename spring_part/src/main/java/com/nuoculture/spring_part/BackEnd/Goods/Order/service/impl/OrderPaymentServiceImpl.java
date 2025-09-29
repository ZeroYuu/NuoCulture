package com.nuoculture.spring_part.BackEnd.Goods.Order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.entity.OrderPaymentInfo;
import com.nuoculture.spring_part.BackEnd.Goods.Order.mapper.OrderMapper;
import com.nuoculture.spring_part.BackEnd.Goods.Order.mapper.OrderPaymentMapper;
import com.nuoculture.spring_part.BackEnd.Goods.Order.service.OrderPaymentService;
import com.nuoculture.spring_part.BackEnd.Goods.Order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderPaymentServiceImpl extends ServiceImpl<OrderPaymentMapper, OrderPaymentInfo> implements OrderPaymentService {

    @Autowired
    OrderPaymentMapper orderPaymentMapper;








}
