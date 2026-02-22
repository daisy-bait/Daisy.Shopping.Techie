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
import java.math.BigDecimal;
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
    private static final String MIN = "min";
    private static final String MAX = "max";
    private static final String BLANK = "";
    private static final int INDEX_CRITERIA = 0;
    private static final String STRING_CLASS = "String";
    private static final String BIGDECIMAL_CLASS = "BigDecimal";

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
        attachFields(criteriaList, ProductDocument.class, ProductToInListDTO.class, listRequest);

        log.info("=====[PRODUCT_SERVICE] INITIALIZING QUERY WITH PARAMS -------> {}", listRequest.toString());

        if (!criteriaList.isEmpty()) customSearch.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[INDEX_CRITERIA])));

        List<ProductDocument> productsDocument = complexProductRepository.find(customSearch, ProductDocument.class);

        Page<ProductDocument> pageProductDocument = PageableExecutionUtils.getPage(
                productsDocument,
                pageable,
                () -> complexProductRepository.count(Query.of(customSearch).limit(-1).skip(-1), ProductDocument.class)
        );

        log.info("=====[PRODUCT_SERVICE] RESULT FROM PRODUCTS COLLECTION -------> {}", pageProductDocument.getContent());

        return pageProductDocument.map(res -> new ProductToOutListDTO(res.getId(), res.getName(), res.getDescription(), res.getPrice()));
    }

    @SneakyThrows
    private void attachFields(List<Criteria> criteriaList, Class documentClass, Class dtoClass, Object listRequest) {

        List<String> documentFields = Arrays.stream(documentClass.getDeclaredFields()).map(Field::getName).toList();
        Field[] fields = dtoClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(listRequest) == null) continue;
            log.info("=====[UTILITY_PREPARE_CRITERIA] Field Name: {}, Field Value: {}=====", field.getName(), field.get(listRequest));

            switch (field.getType().getSimpleName()) {
                case STRING_CLASS:
                    logTypeField(STRING_CLASS);
                    String fieldAsString = String.valueOf(field.get(listRequest));
                    if (fieldAsString != null && !fieldAsString.isBlank() && documentFields.contains(field.getName()))
                        criteriaList.add(Criteria.where(field.getName()).regex(fieldAsString, ILIKE));
                    break;
                case BIGDECIMAL_CLASS:
                    logTypeField(BIGDECIMAL_CLASS);
                    BigDecimal fieldAsNumber = (BigDecimal) field.get(listRequest);
                    log.info(String.valueOf(fieldAsNumber));
                    if (fieldAsNumber != null)
                        if (field.getName().startsWith(MIN)) criteriaList.add(Criteria.where(
                                lowerCaseFirstLetter(field.getName().replace(MIN, BLANK))
                        ).gte(fieldAsNumber));
                        else if (field.getName().startsWith(MAX)) criteriaList.add(Criteria.where(
                                lowerCaseFirstLetter(field.getName().replace(MAX, BLANK))
                        ).lte(fieldAsNumber));
                        else criteriaList.add(Criteria.where(field.getName()).is(fieldAsNumber));
                    break;
            }
        }
    }

    private String lowerCaseFirstLetter(String word) {
        return word.substring(0, 1).toLowerCase() + word.substring(1);
    }

    private void logTypeField(String fieldType) {
        log.info("=====[UTILITY_PREPARE_CRITERIA] Field is {} Class=====", fieldType);
    }

}