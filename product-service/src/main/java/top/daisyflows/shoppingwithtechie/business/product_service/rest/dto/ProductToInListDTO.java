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
public class ProductToInListDTO {

    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    @Override
    public String toString() {
        return "List Request → [" +
                "name='" + name + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ']';
    }
}