package top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.service.contracts;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.persistence.repository.InventoryRepository;
import top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.rest.dto.InventoryToVerifyInDTO;
import top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.rest.dto.InventoryToVerifyOutDTO;

@AllArgsConstructor
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryServiceContract {

    private final InventoryRepository inventoryRepository;

    @Override
    public InventoryToVerifyOutDTO verifyProductStock(InventoryToVerifyInDTO verifyRequest) {
        String skuCode = verifyRequest.getSkuCode();
        if (!inventoryRepository.existsBySkuCode(skuCode)) {
            log.info("=====[INVENTORY_SERVICE] DOES NOT EXIST PRODUCT | WITH SKU_CODE -------> {}====", skuCode);
            return handleVerifyResponse(false);
        } else {
            log.info("=====[INVENTORY_SERVICE] PRODUCT EXISTS | WITH SKU_CODE -------> {}====", skuCode);
            return inventoryRepository
                    .findBySkuCode(skuCode)
                    .get().getQuantity() < verifyRequest.getStockQuantity() ? handleVerifyResponse(false) : handleVerifyResponse(true);
        }
    }

    private InventoryToVerifyOutDTO handleVerifyResponse(boolean value) {
        return new  InventoryToVerifyOutDTO(value);
    }

}
