package com.example.config.multitenant;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@FeignClient(value = "API", url = "http://localhost:8081",
    configuration = MyClientConfiguration.class)
public interface TenantHttpClient {



  @PostMapping(value = "/auth/realms/test1/protocol/openid-connect/token",
      produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  ResponseEntity<?> getIdToken(@RequestHeader Map<String, Object> headers,
      @RequestBody Map<String, Object> body);



}
