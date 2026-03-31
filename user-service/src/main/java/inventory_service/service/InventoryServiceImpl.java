package inventory_service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.persistence.entity.InventoryEntity;
import top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.persistence.repository.InventoryRepository;
import top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.service.contracts.InventoryServiceContract;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryServiceContract {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public InventoryToVerifyOutDTO verifyProductStock(InventoryToVerifyInDTO verifyRequest) {
        List<ProductToVerifyOutDTO> verifyListResponse = new ArrayList<>();
        verifyRequest.getProductsToVerifyStock().forEach(product -> {
            String skuCode = product.getSkuCode();
            if (!inventoryRepository.existsBySkuCode(skuCode)) {
                log.info("=====[INVENTORY_SERVICE] DOES NOT EXIST PRODUCT | WITH SKU_CODE -------> {}====", skuCode);
                throw new IllegalArgumentException("SKU CODE DOES NOT EXIST");
            } else {
                log.info("=====[INVENTORY_SERVICE] PRODUCT EXISTS | WITH SKU_CODE -------> {}====", skuCode);
                Integer entityStock = inventoryRepository.findBySkuCode(skuCode).getQuantity();

                verifyListResponse.add(entityStock < product.getStockQuantity() ?
                        handleVerifyResponse(product, entityStock,false) :
                        handleVerifyResponse(product, entityStock, true));
            }
        });
        return new InventoryToVerifyOutDTO(verifyListResponse);
    }

    @Override
    public ProductToCreateOutDTO createProductInventory(ProductToCreateVerifyInDTO productRequest) {
        String skuCode = productRequest.getSkuCode();
        if (inventoryRepository.existsBySkuCode(skuCode)) throw new IllegalArgumentException("INVENTORY FOR PRODUCT WITH THAT SKU CODE ALREADY EXISTS");

        InventoryEntity inventoryEntity = new InventoryEntity(null, productRequest.getSkuCode(), productRequest.getStockQuantity());
        InventoryEntity savedInventory = inventoryRepository.save(inventoryEntity);

        return new ProductToCreateOutDTO(savedInventory.getInventoryId(), savedInventory.getSkuCode());
    }

    private ProductToVerifyOutDTO handleVerifyResponse(ProductToCreateVerifyInDTO product, Integer actualStock, boolean value) {
        return new ProductToVerifyOutDTO(product.getSkuCode(), product.getStockQuantity(), actualStock, value);
    }

}
