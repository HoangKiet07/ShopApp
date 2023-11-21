package com.example.shopapp.controller;


import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.models.Order;
import com.example.shopapp.responses.OrderResponse;
import com.example.shopapp.services.Impl.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${spring.api.prefix}/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ){
        try{
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError -> FieldError.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Order orderResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrderByUserId(
            @Valid @PathVariable("user_id") Long userId
    ){
        try {
            List<Order> orders = orderService.getAllOrderByUserId(userId);
            return ResponseEntity.ok().body(orders);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(
            @Valid @PathVariable("id") Long orderId
    ){
        try {
            Order existingOrder = orderService.getOrderById(orderId);
            return ResponseEntity.ok().body(existingOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
        @Valid @PathVariable Long id,
        @Valid @RequestBody OrderDTO orderDTO
    ){
        try {
            Order order = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(order);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id){
        // xoa mem
        orderService.deteleOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}
