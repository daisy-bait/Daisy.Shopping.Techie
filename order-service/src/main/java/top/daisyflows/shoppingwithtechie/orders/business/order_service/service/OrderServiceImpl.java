package top.daisyflows.shoppingwithtechie.orders.business.order_service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.persistence.entity.OrderEntity;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.persistence.entity.OrderLineItemsEntity;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.persistence.repository.OrderRepository;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.dto.OrderLineItemsToInCreateDTO;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.dto.OrderToInCreateDTO;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.dto.OrderToOutCreateDTO;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.service.contracts.OrderServiceContract;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderServiceContract {

    private final OrderRepository orderRepository;

    public OrderToOutCreateDTO placeOrder(OrderToInCreateDTO orderRequest) {
        OrderEntity order = new OrderEntity();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItemsEntity> orderLineItemsEntities = orderRequest.getOrderItems().stream().map(this::mapOrderLineItemsToEntity).toList();

        order.setOrderLineItemsList(orderLineItemsEntities);

        Long orderId = orderRepository.save(order).getOrderId();
        log.info("=====[ORDER_SERVICE] ORDER PLACED | WITH ID -------> {}====", orderId);

        return new OrderToOutCreateDTO(orderId.toString());

    }

    private OrderLineItemsEntity mapOrderLineItemsToEntity(OrderLineItemsToInCreateDTO orderLineItemsToInCreateDTO) {
        return OrderLineItemsEntity.builder()
                .skuCode(orderLineItemsToInCreateDTO.getSkuCode())
                .price(orderLineItemsToInCreateDTO.getPrice())
                .quantity(orderLineItemsToInCreateDTO.getQuantity())
                .build();
    }

}
