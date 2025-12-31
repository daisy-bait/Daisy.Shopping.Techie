package top.daisyflows.shoppingwithtechie.business.product_service.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInCreateDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToOutCreateDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.service.ProductService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/products/v0/")
public class ProductController {

    private final ProductService productService;

    public ResponseEntity<ProductToOutCreateDTO> createProduct(@RequestBody ProductToInCreateDTO productToInCreateDTO) {
        return ResponseEntity.ok(productService.createProduct(productToInCreateDTO));
    }

}
