package top.daisyflows.shoppingwithtechie.orders.business.order_service.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "T_ORDER_LINE_ITEMS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderLineItemsId;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}
