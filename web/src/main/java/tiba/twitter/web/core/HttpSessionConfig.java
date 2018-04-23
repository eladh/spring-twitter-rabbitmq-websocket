package tiba.twitter.web.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Configuration
public class HttpSessionConfig {

   @Bean
   public HttpSessionListener httpSessionListener() {
      return new HttpSessionListener() {
         @Override
         public void sessionCreated(HttpSessionEvent se) {
            System.out.println("Session Created with session id+" + se.getSession().getId());
         }
         @Override
         public void sessionDestroyed(HttpSessionEvent se) {         // This method will be automatically called when session destroyed
            System.out.println("Session Destroyed, Session id:" + se.getSession().getId());
         }
      };
   }



}
