package com.example.shopapp.services;

import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Category;
import com.example.shopapp.models.Order;
import com.example.shopapp.responses.OrderResponse;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;

    Order getOrderById(long id);

    List<Order> getAllOrderByUserId(Long userId);

    Order updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deteleOrder(long id);

}
