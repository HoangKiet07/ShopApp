package com.example.shopapp.mappers;

import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.responses.OrderDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderDetailMapper {

    OrderDetailMapper instance = Mappers.getMapper(OrderDetailMapper.class);

    OrderDetail orderDetailDTOToOrderDetail(OrderDetailDTO orderDetailDTO);

    OrderDetailResponse orderDetailToOrderDetailResponse(OrderDetail orderDetail);
}
