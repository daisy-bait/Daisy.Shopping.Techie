package top.daisyflows.shoppingwithtechie.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryToVerifyOutDTO {

    private List<ProductToVerifyOutDTO> productToVerifyOutDTOList;

}