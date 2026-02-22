package top.daisyflows.shoppingwithtechie.business.product_service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import top.daisyflows.shoppingwithtechie.business.product_service.persistence.document.ProductDocument;
import top.daisyflows.shoppingwithtechie.business.product_service.persistence.repository.ProductRepository;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInCreateDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInListDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToOutCreateDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToOutListDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.service.contracts.ProductServiceContract;

import java.util.ArrayList;
import java.util.List;

import static top.daisyflows.shoppingwithtechie.business.product_service.service.utils.ReflectionCriteriaBuilder.buildCriteriaFromDTO;

@AllArgsConstructor
@Service
@Slf4j
public class ProductServiceImpl implements ProductServiceContract {

    private final ProductRepository productRepository;
    MongoTemplate complexProductRepository;

    private static final int INDEX_CRITERIA = 0;

    public ProductToOutCreateDTO createProduct(ProductToInCreateDTO productRequest) {
        ProductDocument productDocument = ProductDocument.builder()
                .price(productRequest.getPrice())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .build();

        String productId = productRepository.save(productDocument).getId();
        log.info("=====[PRODUCT_SERVICE] PRODUCT SAVED | WITH ID -------> {}====", productId);

        return new ProductToOutCreateDTO(productId);
    }

    @Override
    public Page<ProductToOutListDTO> listPageableProductsByCustomSearch(ProductToInListDTO listRequest, Pageable pageable) {
        Query customSearch = new Query().with(pageable);

        List<Criteria> criteriaList = new ArrayList<>();
        buildCriteriaFromDTO(criteriaList, ProductDocument.class, ProductToInListDTO.class, listRequest);

        log.info("=====[PRODUCT_SERVICE] INITIALIZING QUERY WITH PARAMS -------> {}", listRequest.toString());

        if (!criteriaList.isEmpty())
            customSearch.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[INDEX_CRITERIA])));

        List<ProductDocument> productsDocument = complexProductRepository.find(customSearch, ProductDocument.class);

        Page<ProductDocument> pageProductDocument = PageableExecutionUtils.getPage(
                productsDocument,
                pageable,
                () -> complexProductRepository.count(Query.of(customSearch).limit(-1).skip(-1), ProductDocument.class)
        );

        log.info("=====[PRODUCT_SERVICE] RESULT FROM PRODUCTS COLLECTION -------> {}", pageProductDocument.getContent());

        return pageProductDocument.map(res -> new ProductToOutListDTO(res.getId(), res.getName(), res.getDescription(), res.getPrice()));
    }

}