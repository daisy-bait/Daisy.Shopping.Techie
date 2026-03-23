package top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.rest.dto.InventoryToVerifyInDTO;
import top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.rest.dto.InventoryToVerifyOutDTO;
import top.daisyflows.shoppingwithtechie.inventory.business.inventory_service.service.contracts.InventoryServiceContract;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products/v0/inventory")
public class InventoryController {

    private final InventoryServiceContract inventoryService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<InventoryToVerifyOutDTO> verifyInventory(@RequestBody InventoryToVerifyInDTO inventoryToVerifyInDTO) {
        return ResponseEntity.ok(inventoryService.verifyProductStock(inventoryToVerifyInDTO));
    }

}
