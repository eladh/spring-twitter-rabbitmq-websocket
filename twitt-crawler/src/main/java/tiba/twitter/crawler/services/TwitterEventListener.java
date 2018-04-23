package tiba.twitter.crawler.services;

import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TwitterEventListener {

   private final Twitter twitter;
   private final UserRegistrationService userRegistrationService;
   private final Channel myChannel;

   @Autowired
   public TwitterEventListener(Twitter twitter, UserRegistrationService userRegistrationService , Channel myChannel) {
      this.twitter = twitter;
      this.userRegistrationService = userRegistrationService;
      this.myChannel = myChannel;
   }

   @PostConstruct
   public void init() {
      if (!twitter.isAuthorized()) {
         //todo - throw exception
         return;
      }

      List<StreamListener> listeners = new ArrayList<>();

      listeners.add(new StreamListener() {
         @Override
         public void onTweet(Tweet tweet) {
            List<HashTagEntity> hashTags = tweet.getEntities().getHashTags();

            hashTags.forEach(hashTagEntity -> {
               String text = hashTagEntity.getText();
               List<String> usersQueues = userRegistrationService.getTagQueues(text);

               for (String user : usersQueues) {
                  try {
                     // TODO: 19/04/2018 in a case when a full Tweet Object need to be sent use Jackson Serialization
                     myChannel.basicPublish("", user, null, tweet.getText().getBytes());
                  } catch (IOException theE) {
                     theE.printStackTrace();
                  }
               }
            });
         }

         @Override
         public void onDelete(StreamDeleteEvent deleteEvent) {

         }

         @Override
         public void onLimit(int numberOfLimitedTweets) {

         }

         @Override
         public void onWarning(StreamWarningEvent warningEvent) {

         }
      });

      twitter.streamingOperations().sample(listeners);
   }

}
