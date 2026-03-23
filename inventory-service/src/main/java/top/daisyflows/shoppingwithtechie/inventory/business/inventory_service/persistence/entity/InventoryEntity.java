package top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "T_INVENTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;
    private String skuCode;
    private Integer quantity;

}
