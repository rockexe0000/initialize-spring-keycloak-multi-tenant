package com.example;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.czetsuyatech.config.PathBasedConfigResolver;

@SpringBootApplication
@EnableFeignClients
@ComponentScan({"com.example.*", "com.czetsuyatech.*"})
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }


  /**
   * Required to handle spring boot configurations
   * 
   * @return
   */
  @Bean
  @ConditionalOnMissingBean(PathBasedConfigResolver.class)
  public KeycloakConfigResolver keycloakConfigResolver() {
    return new PathBasedConfigResolver();
  }
}
