package tiba.twitter.web.core;

import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class TwitEventDispatcherService {

   private final Channel channel;
   private final SimpMessageSendingOperations messagingTemplate;

   @Autowired
   public TwitEventDispatcherService(Channel channel, SimpMessageSendingOperations messagingTemplate) {
      this.channel = channel;
      this.messagingTemplate = messagingTemplate;
   }

   public void registerToFeed(String username , SimpMessageHeaderAccessor headerAccessor) {

      Consumer consumer = new DefaultConsumer(channel) {
         @Override
         public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            System.out.println("got new twitter msg from the queue ,time:" + LocalDateTime.now().toLocalTime());
            messagingTemplate.convertAndSend("/topic/twit/" + username,new String(body, "UTF-8"));
         }
      };


      try {
         channel.queueDeclare(username, true, false, false, null);
         headerAccessor.getSessionAttributes().put("rabbitConsumerId" ,channel.basicConsume(username, true, consumer));
      } catch (IOException theE) {
         theE.printStackTrace();
      }

   }

//   @Scheduled(fixedDelay = 1000)
//   public void scheduleFixedDelayTask() {
//      System.out.println( "Fixed delay task - " + System.currentTimeMillis() / 1000);
//      messagingTemplate.convertAndSend("/topic/twit/eladh" ,"fasfsafsaf");
//
//   }


}