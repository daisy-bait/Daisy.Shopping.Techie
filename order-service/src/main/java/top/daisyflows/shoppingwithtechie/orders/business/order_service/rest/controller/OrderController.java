package top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.dto.OrderToInCreateDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products/v0/orders")
public class OrderController {

    @PostMapping
    public String placeOrder(@RequestBody OrderToInCreateDTO orderRequest) {
        return "Order Placed Successfully";
    }

}
