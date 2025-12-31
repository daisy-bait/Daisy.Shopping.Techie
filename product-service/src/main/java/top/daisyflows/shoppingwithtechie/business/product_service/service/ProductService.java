package top.daisyflows.shoppingwithtechie.business.product_service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.daisyflows.shoppingwithtechie.business.product_service.persistence.document.ProductDocument;
import top.daisyflows.shoppingwithtechie.business.product_service.persistence.repository.ProductRepository;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInCreateDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToOutCreateDTO;

@AllArgsConstructor
@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductToOutCreateDTO createProduct(ProductToInCreateDTO productRequest) {
        ProductDocument productDocument = ProductDocument.builder()
                .price(productRequest.getPrice())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .build();

        String productId = productRepository.save(productDocument).getId();
        log.info("product saved");
        log.info("product id is {}", productId);

        return new ProductToOutCreateDTO(productId);
    }

}