package top.daisyflows.shoppingwithtechie.orders.business.order_service.service.contracts;

import top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.dto.OrderToInCreateDTO;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.rest.dto.OrderToOutCreateDTO;

public interface OrderServiceContract {

    OrderToOutCreateDTO placeOrder(OrderToInCreateDTO orderRequest);

}
