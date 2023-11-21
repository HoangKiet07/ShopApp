package com.example.shopapp.controller;

import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.responses.OrderDetailResponse;
import com.example.shopapp.services.Impl.OrderDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${spring.api.prefix}/order_details")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO
    ){
        try{
            OrderDetailResponse orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(orderDetail);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(
            @Valid @PathVariable Long id
    ) throws DataNotFoundException {
        OrderDetailResponse orderDetail = orderDetailService.getOrderDetailById(id);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOderDetailByOderId(
            @Valid @PathVariable Long orderId
    ){
        List<OrderDetailResponse> orderDetails = orderDetailService.getOrderDetailByOrderId(orderId);
        return ResponseEntity.ok(orderDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
        @Valid @PathVariable Long id,
        @Valid @RequestBody OrderDetailDTO orderDetailDTO
    ) throws DataNotFoundException {
        try{
            OrderDetailResponse orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok(orderDetail);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
        @Valid @PathVariable Long id
    ){
        orderDetailService.deteleOrderDetail(id);
        return ResponseEntity.ok("Deleted");
    }
}
