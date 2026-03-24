package top.daisyflows.shoppingwithtechie.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ProductToCreateOutDTO {

    private Long inventoryId;
    private String skuCode;

}
