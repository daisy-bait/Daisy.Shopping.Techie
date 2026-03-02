package top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderToInCreateDTO {

    private List<OrderLineItemsToInCreateDTO> orderItems;

}
