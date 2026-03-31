package inventory_service.service.contracts;

import top.daisyflows.shoppingwithtechie.inventory.dto.InventoryToVerifyInDTO;
import top.daisyflows.shoppingwithtechie.inventory.dto.InventoryToVerifyOutDTO;
import top.daisyflows.shoppingwithtechie.inventory.dto.ProductToCreateOutDTO;
import top.daisyflows.shoppingwithtechie.inventory.dto.ProductToCreateVerifyInDTO;

public interface InventoryServiceContract {

    InventoryToVerifyOutDTO verifyProductStock(InventoryToVerifyInDTO verifyRequest);

    ProductToCreateOutDTO createProductInventory(ProductToCreateVerifyInDTO productToCreateVerifyInDTO);

}
