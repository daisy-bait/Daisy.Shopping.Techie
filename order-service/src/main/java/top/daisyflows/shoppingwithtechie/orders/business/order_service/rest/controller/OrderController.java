package top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.daisyflows.shoppingwithtechie.error.ErrorMessage;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.dto.OrderToInCreateDTO;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.dto.OrderToOutCreateDTO;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.service.contracts.OrderServiceContract;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products/v0/orders")
public class OrderController {

    private final OrderServiceContract orderService;

    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory", fallbackMethod = "fallbackMethod")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompletableFuture<ResponseEntity<OrderToOutCreateDTO>> placeOrder(@RequestBody OrderToInCreateDTO orderRequest) {
        return CompletableFuture.supplyAsync(() ->
                ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(orderRequest))
        );
    }

    public CompletableFuture<ResponseEntity<ErrorMessage>> fallbackMethod(OrderToInCreateDTO orderRequest, RuntimeException error) {
        return CompletableFuture.supplyAsync(() ->
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorMessage(
                        "Service Unavailable An Error Ocurred: \"" + error.getMessage() + "\", " +
                                "Please try again in a few minutes", HttpStatus.SERVICE_UNAVAILABLE.value()))
        );
    }

}
