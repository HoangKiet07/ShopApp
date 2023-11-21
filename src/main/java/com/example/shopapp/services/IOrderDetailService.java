package com.example.shopapp.services;

import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;

    OrderDetailResponse getOrderDetailById(Long id) throws DataNotFoundException;

    List<OrderDetail> getAllOrderDettail();

    OrderDetailResponse updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    void deteleOrderDetail(long id);

    List<OrderDetailResponse> getOrderDetailByOrderId(Long orderId);
}
