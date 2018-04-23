package tiba.twitter.web.core;

import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.IOException;

@Component
public class WebSocketSessionDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

   private final Channel channel;

   @Autowired
   public WebSocketSessionDisconnectListener(Channel channel) {
      this.channel = channel;
   }

   @Override
   public void onApplicationEvent(SessionDisconnectEvent event) {
      StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
      String rabbitConsumerId = (String) headerAccessor.getSessionAttributes().get("rabbitConsumerId");

      try {
         channel.basicCancel(rabbitConsumerId);
      } catch (IOException theE) {
         theE.printStackTrace();
      }

   }
}