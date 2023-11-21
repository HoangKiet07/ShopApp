package com.example.shopapp.mappers;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.models.Order;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.responses.OrderResponse;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.web.servlet.filter.OrderedFilter;

@Mapper
public interface OrderMapper {

    OrderMapper instance = Mappers.getMapper(OrderMapper.class);

    OrderDTO orderToOrderDTO(Order order);

    Order orderDTOToOrder(OrderDTO orderDTO);

    OrderResponse orderToOrderResponse(Order order);

}
