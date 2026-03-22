package top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.dto.OrderToInCreateDTO;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.dto.OrderToOutCreateDTO;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.service.contracts.OrderServiceContract;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products/v0/orders")
public class OrderController {

    private final OrderServiceContract orderService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<OrderToOutCreateDTO> placeOrder(@RequestBody OrderToInCreateDTO orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(orderRequest));
    }

}
