package top.daisyflows.shoppingwithtechie.notifications.business.notification_service.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static top.daisyflows.shoppingwithtechie.notifications.business.notification_service.config.message.RabbitMQConfig.NOTIFICATION_QUEUE;

@RequiredArgsConstructor
@Service
public class QueueListener {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = NOTIFICATION_QUEUE)
    public void listen(String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("kadanarpa@gmail.com");
        mailMessage.setSubject("Testing Notification Service");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

}
