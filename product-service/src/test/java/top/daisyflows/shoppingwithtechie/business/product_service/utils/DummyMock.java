package top.daisyflows.shoppingwithtechie.business.product_service.utils;

import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInCreateDTO;

import java.math.BigDecimal;

public class DummyMock {

    public static ProductToInCreateDTO getProductRequestPOST() {
        return new ProductToInCreateDTO(
                "Pesa 14LB",
                "Ideal para mejorar tu fuerza física",
                BigDecimal.valueOf(90000)
        );
    }

}
