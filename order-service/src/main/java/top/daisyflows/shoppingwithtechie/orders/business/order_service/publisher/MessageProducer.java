package top.daisyflows.shoppingwithtechie.orders.business.order_service.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import top.daisyflows.shoppingwithtechie.orders.business.order_service.config.message.RabbitMQConfig;

@RequiredArgsConstructor
@Service
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_QUEUE, message);
    }

}
