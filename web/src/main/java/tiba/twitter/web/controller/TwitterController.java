package tiba.twitter.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import tiba.twitter.web.config.RestTemplateFactory;
import tiba.twitter.web.core.TwitEventDispatcherService;

@Controller
@RequestMapping("/api/")
public class TwitterController {

   @Value("${handler.api.address}")
   private String handlerHostAddress;

   private final RestTemplateFactory restTemplateFactory;
   private final SimpMessageSendingOperations messagingTemplate;
   private final TwitEventDispatcherService twitEventDispatcherService;

   @Autowired
   public TwitterController(SimpMessageSendingOperations messagingTemplate, TwitEventDispatcherService twitEventDispatcherService , RestTemplateFactory restTemplateFactory) {
      this.messagingTemplate = messagingTemplate;
      this.twitEventDispatcherService = twitEventDispatcherService;
      this.restTemplateFactory = restTemplateFactory;
   }

   @MessageMapping("/feed/listen/{userId}")
    public void listenToUserFeed(@DestinationVariable String userId ,SimpMessageHeaderAccessor headerAccessor) {
      twitEventDispatcherService.registerToFeed(userId ,headerAccessor);
       messagingTemplate.convertAndSend("/topic/twit",userId);
    }

   @GetMapping("/register/{userId}/{tag1}/{tag2}/{tag3}")
   public ResponseEntity registerToTwitter(@PathVariable String userId , @PathVariable String tag1 ,
                             @PathVariable String tag2, @PathVariable String tag3) {

      // TODO: Handle with service discovery (Spring Cloud)

      try{
         restTemplateFactory.getObject().put(handlerHostAddress + "/add/" + userId + "/" + tag1 + "/" + tag2 + "/" + tag3 ,null );
      } catch (Exception e) {
         e.printStackTrace();
         return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
      }

      return new ResponseEntity(HttpStatus.ACCEPTED);
   }

}