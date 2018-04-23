package tiba.twitter.web.config;


import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactory implements FactoryBean<RestTemplate>, InitializingBean {

   private RestTemplate myRestTemplate;

   @Override
   public void afterPropertiesSet() throws Exception {
      this.myRestTemplate = new RestTemplate(getClientHttpRequestFactory());

   }

   @Nullable
   @Override
   public RestTemplate getObject() {
      return this.myRestTemplate;
   }

   @Nullable
   @Override
   public Class<?> getObjectType() {
      return RestTemplate.class;
   }

   private ClientHttpRequestFactory getClientHttpRequestFactory() {
      int timeout = 5000;
      HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
            = new HttpComponentsClientHttpRequestFactory();
      clientHttpRequestFactory.setConnectTimeout(timeout);
      return clientHttpRequestFactory;
   }
}
