package top.daisyflows.shoppingwithtechie.business.product_service.service.contracts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInCreateDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInListDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToOutCreateDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToOutListDTO;

import java.math.BigDecimal;

public interface ProductServiceContract {

    ProductToOutCreateDTO createProduct(ProductToInCreateDTO productRequest);

    Page<ProductToOutListDTO> listPageableProductsByCustomSearch(ProductToInListDTO listRequest, Pageable pageable);

}
