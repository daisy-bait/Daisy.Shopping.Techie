package top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.persistence.entity.InventoryEntity;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    boolean existsBySkuCode(String skuCode);

    InventoryEntity findBySkuCode(String skuCode);
}
