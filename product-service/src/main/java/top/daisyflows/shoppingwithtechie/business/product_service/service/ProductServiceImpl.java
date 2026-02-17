package top.daisyflows.shoppingwithtechie.business.product_service.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class ProductServiceImpl implements ProductServiceContract {

    private final ProductRepository productRepository;
    MongoTemplate complexProductRepository;

    private static final String ILIKE = "i";
    private static final int INDEX_CRITERIA = 0;
    private static final String STRING_CLASS = "String";

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
    @SneakyThrows
    public Page<ProductToOutListDTO> listPageableProductsByCustomSearch(ProductToInListDTO listRequest, Pageable pageable) {
        Query customSearch = new Query().with(pageable);

        List<Criteria> criteriaList = new ArrayList<>();
        List<String> documentFields = Arrays.stream(ProductDocument.class.getDeclaredFields()).map(Field::getName).toList();
        Field[] fields = ProductToInListDTO.class.getDeclaredFields();

        for (Field field : fields) {
            log.info("=====[PRODUCT_SERVICE] Field Name: {}, Field Value: {}=====", field.getName(), field.get(listRequest));

            switch (field.getType().getSimpleName()) {
                case STRING_CLASS:
                    log.info("Pasando por aquí");
                    String fieldAsString = String.valueOf(field.get(listRequest));
                    if (fieldAsString != null && !fieldAsString.isBlank() && documentFields.contains(fieldAsString))
                        criteriaList.add(Criteria.where(field.getName()).regex(fieldAsString));
            }
        }

        log.info("=====[PRODUCT_SERVICE] INITIALIZING QUERY WITH PARAMS -------> {}", listRequest.toString());

        //if (name != null && !name.isBlank()) criteriaList.add(Criteria.where("name").regex(name, ILIKE));
        //if (minPrice != null) criteriaList.add(Criteria.where("price").gte(minPrice));
        //if (maxPrice != null) criteriaList.add(Criteria.where("price").lte(maxPrice));

        if (!criteriaList.isEmpty()) {
            customSearch.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[INDEX_CRITERIA])));
        }

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