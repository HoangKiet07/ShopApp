package com.example.shopapp.services.Impl;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.mappers.OrderMapper;
import com.example.shopapp.models.Base;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderStatus;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.responses.BaseResponse;
import com.example.shopapp.responses.OrderResponse;
import com.example.shopapp.services.IOrderService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception{
        // xem userId co ton tai k
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()->new DataNotFoundException("cannot dine user with id: " + orderDTO.getUserId()));
        Order order = OrderMapper.instance.orderDTOToOrder(orderDTO);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);

        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw  new DataNotFoundException("date must be at least today");
        }
        order.setActive(true);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrderById(long id) {

        return orderRepository.findById(id).get();
    }

    @Override
    public List<Order> getAllOrderByUserId(Long userId) {

        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public Order updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot find order with id:" + id));
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(()->
                new DataNotFoundException("Cannot find user with id:" + orderDTO.getUserId()));
        order.setUser(user);
        order.setEmail(orderDTO.getEmail());
        order.setNote(orderDTO.getNote());
        order.setAddress(orderDTO.getAddress());
        order.setActive(true);
        order.setShippingDate(orderDTO.getShippingDate());
        order.setTotalMoney(orderDTO.getTotalMoney());
        order.setPhoneNumber(orderDTO.getPhoneNumber());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setShippingMethod(orderDTO.getShippingMethod());
        order.setShippingAddress(orderDTO.getShippingAddress());
        return orderRepository.save(order);
    }

    @Override
    public void deteleOrder(long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null){
            order.setActive(false);
            orderRepository.save(order);
        }
    }
}
