package top.daisyflows.shoppingwithtechie.business.product_service.rest.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInCreateDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInListDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToOutCreateDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToOutListDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.service.ProductServiceImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products/v0/products")
public class ProductController {

    private final ProductServiceImpl productService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<ProductToOutCreateDTO> createProduct(@RequestBody ProductToInCreateDTO productToInCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productToInCreateDTO));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Page<ProductToOutListDTO>> listProducts(
            @RequestBody ProductToInListDTO productToInListDTO,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
            ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(productService.listPageableProductsByCustomSearch(
                productToInListDTO, pageable
        ));
    }

}