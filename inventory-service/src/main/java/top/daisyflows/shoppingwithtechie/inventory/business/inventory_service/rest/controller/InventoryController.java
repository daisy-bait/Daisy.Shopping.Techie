package top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.service.contracts.InventoryServiceContract;
import top.daisyflows.shoppingwithtechie.inventory.dto.InventoryToVerifyInDTO;
import top.daisyflows.shoppingwithtechie.inventory.dto.InventoryToVerifyOutDTO;
import top.daisyflows.shoppingwithtechie.inventory.dto.ProductToCreateOutDTO;
import top.daisyflows.shoppingwithtechie.inventory.dto.ProductToCreateVerifyInDTO;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/products/v0/inventory")
public class InventoryController {

    private final InventoryServiceContract inventoryService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify")
    public ResponseEntity<InventoryToVerifyOutDTO> verifyInventory(@RequestBody InventoryToVerifyInDTO inventoryToVerifyInDTO) {
        return ResponseEntity.ok(inventoryService.verifyProductStock(inventoryToVerifyInDTO));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<ProductToCreateOutDTO> createInventoryProduct(@RequestBody ProductToCreateVerifyInDTO productToCreateVerifyInDTO) {
        return ResponseEntity.ok(inventoryService.createProductInventory(productToCreateVerifyInDTO));
    }

}
