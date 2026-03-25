package top.daisyflows.shoppingwithtechie.orders.business.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.daisyflows.shoppingwithtechie.inventory.dto.InventoryToVerifyInDTO;
import top.daisyflows.shoppingwithtechie.inventory.dto.InventoryToVerifyOutDTO;

@FeignClient(name = "inventory-service", url = "http://localhost:8084/api/products/v0/inventory")
public interface InventoryClient {

    @PostMapping("/verify")
    InventoryToVerifyOutDTO verifyInventory(@RequestBody InventoryToVerifyInDTO inventoryToVerifyInDTO);

}