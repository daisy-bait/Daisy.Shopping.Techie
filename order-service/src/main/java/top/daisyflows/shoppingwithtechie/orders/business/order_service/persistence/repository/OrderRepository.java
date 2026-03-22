package top.daisyflows.shoppingwithtechie.orders.business.order_service.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.persistence.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
