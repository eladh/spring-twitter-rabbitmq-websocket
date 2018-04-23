package tiba.twitter.web.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Configuration
public class RabbitMQConfiguration {

   @Value("${spring.rabbitmq.host}")
   private String host;

   @Value("${spring.rabbitmq.port}")
   private Integer port;

   @Value("${spring.rabbitmq.username}")
   private String username;

   @Value("${spring.rabbitmq.password}")
   private String password;

   @Bean
   Channel getRabbitMqConnection() throws IOException, TimeoutException {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setUsername(username);
      factory.setPassword(password);
      factory.setHost(host);
      factory.setPort(port);

      return factory.newConnection().createChannel();
   }

}
