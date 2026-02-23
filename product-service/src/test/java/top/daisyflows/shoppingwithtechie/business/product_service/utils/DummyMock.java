package top.daisyflows.shoppingwithtechie.business.product_service.utils;

import top.daisyflows.shoppingwithtechie.business.product_service.persistence.document.ProductDocument;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInCreateDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInListDTO;

import java.math.BigDecimal;

public class DummyMock {

    public static ProductToInCreateDTO getProductRequestPOST() {
        return new ProductToInCreateDTO(
                "Pesa 14LB",
                "Ideal para mejorar tu fuerza física",
                BigDecimal.valueOf(90000)
        );
    }

    public static ProductToInListDTO getListProductRequestGET() {
        ProductDocument productDocumentDummy = getProductDocumentDummy();
        return new ProductToInListDTO(
                productDocumentDummy.getName(),
                productDocumentDummy.getPrice().subtract(productDocumentDummy.getPrice()),
                productDocumentDummy.getPrice()
        );
    }

    public static ProductDocument getProductDocumentDummy() {
        return new ProductDocument(
                null,
                "Laptop",
                "Technology Tool",
                BigDecimal.valueOf(3400000)
        );
    }

}
