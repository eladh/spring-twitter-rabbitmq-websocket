package tiba.twitter.crawler.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tiba.twitter.crawler.services.UserRegistrationService;

@RestController
@RequestMapping("/api/twitter/")
public class TwitterController {


   private final UserRegistrationService myUserRegistrationService;

   @Autowired
   public TwitterController(UserRegistrationService myUserRegistrationService) {
      this.myUserRegistrationService = myUserRegistrationService;
   }

   @PutMapping("/add/{userId}/{tag1}/{tag2}/{tag3}")
   public ResponseEntity add(@PathVariable String userId , @PathVariable String tag1 ,
                             @PathVariable String tag2, @PathVariable String tag3) {

      myUserRegistrationService.addUser(userId ,tag1 ,tag2 ,tag3);
      return new ResponseEntity(HttpStatus.ACCEPTED);
   }

   @DeleteMapping("/remove/{userId}")
   public ResponseEntity remove(@PathVariable String userId) {
      myUserRegistrationService.removeUser(userId);
      return new ResponseEntity(HttpStatus.ACCEPTED);
   }

}