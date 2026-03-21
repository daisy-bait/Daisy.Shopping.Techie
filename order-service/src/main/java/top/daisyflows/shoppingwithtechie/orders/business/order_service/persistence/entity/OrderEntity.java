package top.daisyflows.shoppingwithtechie.orders.business.order_service.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "T_ORDERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private String orderNumber;
    @OneToMany
    @JoinColumn(name = "ORDER_ID")
    List<OrderLineItemsEntity> orderLineItemsList;

}
