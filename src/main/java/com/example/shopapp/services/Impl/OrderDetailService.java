package com.example.shopapp.services.Impl;

import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.mappers.OrderDetailMapper;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.models.Product;
import com.example.shopapp.repositories.OrderDetailRepository;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.responses.OrderDetailResponse;
import com.example.shopapp.services.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderDetailService implements IOrderDetailService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                ()-> new DataNotFoundException("cannot find Order with id:" + orderDetailDTO.getOrderId())
        );
        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                ()-> new DataNotFoundException("cannot find Product with id:" + orderDetailDTO.getProductId())
        );
        OrderDetail orderDetail = OrderDetailMapper.instance.orderDetailDTOToOrderDetail(orderDetailDTO);
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        OrderDetail orderDetail1 =  orderDetailRepository.save(orderDetail);
        OrderDetailResponse orderDetailResponse =OrderDetailMapper.instance.orderDetailToOrderDetailResponse(orderDetail1);
        orderDetailResponse.setOrderId(order.getId());
        orderDetailResponse.setProductId(product.getId());
        return orderDetailResponse;
    }

    @Override
    public OrderDetailResponse getOrderDetailById(Long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(
                ()-> new DataNotFoundException("Cannot find order detail with id:"+ id));
        OrderDetailResponse orderDetailResponse =OrderDetailMapper.instance.orderDetailToOrderDetailResponse(orderDetail);
        orderDetailResponse.setOrderId(orderDetail.getOrder().getId());
        orderDetailResponse.setProductId(orderDetail.getProduct().getId());
        return orderDetailResponse;
    }

    @Override
    public List<OrderDetail> getAllOrderDettail() {
        return orderDetailRepository.findAll();
    }

    @Override
    public OrderDetailResponse updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("OrderDetail cannot find with id:"+ id));
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                ()-> new DataNotFoundException("cannot find Order with id:" + orderDetailDTO.getOrderId())
        );
        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                ()-> new DataNotFoundException("cannot find Product with id:" + orderDetailDTO.getProductId())
        );
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setColor(orderDetailDTO.getColor());
        orderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        orderDetail.setProduct(product);
        orderDetail.setOrder(order);
        orderDetailRepository.save(orderDetail);

        OrderDetailResponse orderDetailResponse =OrderDetailMapper.instance.orderDetailToOrderDetailResponse(orderDetail);
        orderDetailResponse.setOrderId(orderDetail.getOrder().getId());
        orderDetailResponse.setProductId(orderDetail.getProduct().getId());
        return orderDetailResponse;
    }

    @Override
    public void deteleOrderDetail(long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailByOrderId(Long orderId) {
        List<OrderDetail> orderDetail= orderDetailRepository.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetail.stream().map(
                orderDetail1 -> {
                    OrderDetailResponse orderDetailResponse = OrderDetailMapper.instance.orderDetailToOrderDetailResponse(orderDetail1);
                    orderDetailResponse.setOrderId(orderDetail1.getOrder().getId());
                    orderDetailResponse.setProductId(orderDetail1.getProduct().getId());
                    return orderDetailResponse;
                }
        ).collect(Collectors.toList());
        return orderDetailResponses;
    }
}
