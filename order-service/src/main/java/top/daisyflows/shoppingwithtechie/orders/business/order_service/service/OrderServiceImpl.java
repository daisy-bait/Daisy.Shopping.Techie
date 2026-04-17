package top.daisyflows.shoppingwithtechie.orders.business.order_service.service;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.daisyflows.shoppingwithtechie.inventory.dto.InventoryToVerifyInDTO;
import top.daisyflows.shoppingwithtechie.inventory.dto.InventoryToVerifyOutDTO;
import top.daisyflows.shoppingwithtechie.inventory.dto.ProductToCreateVerifyInDTO;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.client.InventoryClient;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.persistence.entity.OrderEntity;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.persistence.entity.OrderLineItemsEntity;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.persistence.repository.OrderRepository;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.publisher.MessageProducer;
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
    private final InventoryClient inventoryClient;
    private final Tracer tracer;

    private final MessageProducer messageProducer;

    public OrderToOutCreateDTO placeOrder(OrderToInCreateDTO orderRequest) {
        OrderEntity order = new OrderEntity();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItemsEntity> orderLineItemsEntities = orderRequest.getOrderItems().stream().map(this::mapOrderLineItemsToEntity).toList();

        order.setOrderLineItemsList(orderLineItemsEntities);
        log.info("=====[ORDER_SERVICE] START PRODUCTS STOCK VERIFICATION ====");

        InventoryToVerifyInDTO inventoryToVerifyInDTO = new InventoryToVerifyInDTO(
                order.getOrderLineItemsList().stream().map(
                        orderLineItem -> new ProductToCreateVerifyInDTO(
                                orderLineItem.getSkuCode(), orderLineItem.getQuantity()
                        )
                ).toList()
        );

        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");
        try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {
            InventoryToVerifyOutDTO inventoryResponse = inventoryClient.verifyInventory(inventoryToVerifyInDTO);

            log.info("=====[ORDER_SERVICE] PRODUCT VERIFICATION ====");
            inventoryResponse.getProductToVerifyOutDTOList().forEach(
                    verification -> {
                        log.info("=====[ORDER_SERVICE] PRODUCT UNAVAILABLE -----> SKU CODE:{} | WANTED QUANTITY:{} | ACTUAL QUANTITY: {} ====",
                                verification.getSkuCode(), verification.getIntroducedQuantity(), verification.getActualQuantity());
                        if (!verification.isAvailable()) throw new RuntimeException("PRODUCT NOT VALID");
                    }
            );
        } finally {
            inventoryServiceLookup.end();
        }

        log.info("=====[ORDER_SERVICE] VALID PRODUCTS IN ORDER ====");

        Long orderId = orderRepository.save(order).getOrderId();
        log.info("=====[ORDER_SERVICE] ORDER PLACED | WITH ID -------> {}====", orderId);
        messageProducer.publishMessage("Order Placed with ID → " + orderId);

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