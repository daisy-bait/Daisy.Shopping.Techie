package top.daisyflows.shoppingwithtechie.orders.business.order_service.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "T_ORDER_LINE_ITEMS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineItemsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderLineItemsId;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}
