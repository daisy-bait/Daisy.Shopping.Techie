package top.daisyflows.shoppingwithtechie.business.product_service.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import top.daisyflows.shoppingwithtechie.business.product_service.persistence.document.ProductDocument;

public interface ProductRepository extends MongoRepository<ProductDocument, String> {
}