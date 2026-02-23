package top.daisyflows.shoppingwithtechie.business.product_service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;
import tools.jackson.databind.ObjectMapper;
import top.daisyflows.shoppingwithtechie.business.product_service.persistence.document.ProductDocument;
import top.daisyflows.shoppingwithtechie.business.product_service.persistence.repository.ProductRepository;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInCreateDTO;
import top.daisyflows.shoppingwithtechie.business.product_service.rest.dto.ProductToInListDTO;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static top.daisyflows.shoppingwithtechie.business.product_service.utils.DummyMock.*;

@Slf4j
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    /**
     * It ups a Docker Container with MongoDB, generating a Dynamic URI that is later Autowired,
     * when the tests end, the container does.
     */
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    /**
     * To fetch the URL property assigned to our MongoDB Container, we have to set the necessaries
     * properties, passing the replicated URL we set in the application properties file.
     *
     * <b>DynamicPropertySource</b> Annotation indicates the property will be registered at the
     * Spring Framework Context.
     *
     * @param registry Allow us to set Properties in an Integrating Testing Environment.
     */
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

	@Test
    @SneakyThrows
    void shouldCreateProduct() {
        ProductToInCreateDTO productRequest = getProductRequestPOST();
        String productRequestJSON = objectMapper.writeValueAsString(productRequest);

        log.info("[PRODUCT-SERVICE TEST] ===== Product Request POST -----> {}", productRequestJSON);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/v0/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestJSON))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    @Test
    @SneakyThrows
    void shouldListProducts() {

        ProductDocument productToBeListed = getProductDocumentDummy();
        String id = productRepository.save(productToBeListed).getId();
        log.info("[PRODUCT-SERVICE TEST] ===== Product Saved on BD -----> {}", productToBeListed);

        ProductToInListDTO productRequest = getListProductRequestGET();
        log.info("[PRODUCT-SERVICE TEST] ===== Product Request GET -----> {}", productRequest);
        String productRequestJSON = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/v0/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestJSON))
                .andExpect(status().isOk());

        Assertions.assertEquals(1, productRepository.findById((id)).stream().toList().size());
    }

}