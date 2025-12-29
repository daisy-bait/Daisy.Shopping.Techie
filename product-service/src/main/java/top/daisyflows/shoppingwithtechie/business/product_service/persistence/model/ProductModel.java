package top.daisyflows.shoppingwithtechie.business.product_service.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document("Product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductModel {

    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;

}
