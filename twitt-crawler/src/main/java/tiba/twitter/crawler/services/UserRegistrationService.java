package tiba.twitter.crawler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.rabbitmq.client.Channel;

@Service
public class UserRegistrationService {

   private Map<String ,List<String>> tagsToQueues = new ConcurrentHashMap<>();

   private final Channel rabbitMqChannel;

   @Autowired
   public UserRegistrationService(Channel rabbitMqChannel) {
      this.rabbitMqChannel = rabbitMqChannel;
   }

   public void addUser(String userId ,String hash1 ,String hash2 ,String hash3) {
      List<String> tags = Arrays.asList(hash1, hash2, hash3);
      try {
         rabbitMqChannel.queueDeclare(userId, true, false, false, null);
      } catch (IOException theE) {
         theE.printStackTrace();
      }
      for (String tag : tags) {
         if (!tagsToQueues.containsKey(tag)) {
            tagsToQueues.putIfAbsent(tag ,new ArrayList<>());
         }
         tagsToQueues.get(tag).add(userId);
      }
   }

   public void removeUser(String userId) {
      try {
         rabbitMqChannel.queueDelete(userId);
      } catch (IOException theE) {
         theE.printStackTrace();
      }
      tagsToQueues.forEach((tag, users) -> {
         users.remove(userId);
      });
   }

   public List<String> getTagQueues(String tag) {
      return tagsToQueues.get(tag);
   }
}
