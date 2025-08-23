package com.userservice.user.config;

import com.fabrikka.common.CreateOrderRequest;
import com.fabrikka.common.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderClient {

    @PostMapping("/orders/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request);

    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable Long userId);

    @DeleteMapping("/orders/remove/{userId}")
    public ResponseEntity<String> removeOrder(@PathVariable Long userId);
}
