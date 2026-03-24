package top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.service.contracts;

import top.daisyflows.shoppingwithtechie.inventory.dto.InventoryToVerifyInDTO;
import top.daisyflows.shoppingwithtechie.inventory.dto.InventoryToVerifyOutDTO;

public interface InventoryServiceContract {

    InventoryToVerifyOutDTO verifyProductStock(InventoryToVerifyInDTO verifyRequest);

}
