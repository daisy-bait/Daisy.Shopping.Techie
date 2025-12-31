package top.daisyflows.shoppingwithtechie.business.product_service.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductToInCreateDTO {

    private String name;
    private String description;
    private BigDecimal price;

}
