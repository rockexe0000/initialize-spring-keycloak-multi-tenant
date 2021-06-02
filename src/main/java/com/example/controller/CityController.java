package com.example.controller;

import java.net.MalformedURLException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.config.multitenant.TenantHttpClient;
import com.example.entity.City;
import com.example.service.CityService;

@RestController
public class CityController {



  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private CityService cityService;

  @Autowired
  TenantHttpClient tenantHttpClient;

  @RequestMapping(value = "/signin")
  public ResponseEntity<?> createAuthenticationToken(HttpServletRequest request)
      throws MalformedURLException {

    String tenantID = request.getHeader("X-TenantID");
    if (tenantID.isBlank()) {
      throw new IllegalStateException("Not able to resolve realm from the request path!");
    }
    String realm = tenantID;


    /**
     * var jsonObject = (JSONObject) JSONObject.toJSON(request.getParameterMap()); // NOSONAR
     * 
     * Map<String, Object> headers = new HashMap<>(); headers.put(HttpHeaders.CONTENT_TYPE,
     * MediaType.APPLICATION_JSON_VALUE); headers.put(HttpHeaders.HOST, new
     * URL("http://localhost:8081").getHost()); headers.put("X-TenantID", tenantID);
     * 
     * 
     * Map<String, Object> body = new HashMap<>();
     * 
     * body.put("client_id", tenantID); body.put("username", request.getParameter("username"));
     * body.put("password", request.getParameter("password")); body.put("grant_type", "password");
     * 
     * 
     * var feignClient = tenantHttpClient.getIdToken(headers, body); logger.debug("feignClient=[" +
     * jsonObject.toJSON(feignClient) + "]"); // NOSONAR
     * 
     * logger.debug("feignClient.StatusCode=[" + feignClient.getStatusCodeValue() + "]"); // NOSONAR
     * logger.debug("feignClient.getBody()=[" + feignClient.getBody() + "]"); // NOSONAR
     */


    return ResponseEntity.ok(getUserInfo());
  }



  @SuppressWarnings("unchecked")
  private Map getUserInfo() {

    KeycloakAuthenticationToken authentication =
        (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

    final Principal principal = (Principal) authentication.getPrincipal();

    HashMap<String, Object> map = new HashMap<>();

    String tokenInfo = null;
    if (principal instanceof KeycloakPrincipal) {

      KeycloakPrincipal<KeycloakSecurityContext> kPrincipal =
          (KeycloakPrincipal<KeycloakSecurityContext>) principal;
      KeycloakSecurityContext ksc = kPrincipal.getKeycloakSecurityContext();
      IDToken token = ksc.getIdToken();
      AccessToken accessToken = kPrincipal.getKeycloakSecurityContext().getToken();
      tokenInfo = accessToken.getSubject();

      // this value is the one use to call another service as bearer token
      // Authorization : Bearer kcs.getTokenString()
      // use this link to read the token https://jwt.io
      System.out.println(ksc.getTokenString());
      System.out.println(accessToken.getGivenName());
      System.out.println(accessToken.getFamilyName());


      map.put("userInfo", tokenInfo);
      map.put("ksc.getTokenString()", ksc.getTokenString());
      map.put("accessToken.getGivenName()", accessToken.getGivenName());
      map.put("accessToken.getFamilyName()", accessToken.getFamilyName());
    }



    return map;

    // return "userInfo " + tokenInfo;
  }



  @RequestMapping(value = "/city", method = RequestMethod.POST)
  public ResponseEntity<?> save(@RequestBody City city) {
    cityService.save(city);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/city", method = RequestMethod.GET)
  public ResponseEntity<List<City>> getAll() throws SQLException {
    List<City> cities = cityService.getAll();
    return new ResponseEntity<>(cities, HttpStatus.OK);
  }

  @RequestMapping(value = "/city/{id}", method = RequestMethod.GET)
  public ResponseEntity<City> get(@PathVariable(value = "id") Long id) {
    City city = cityService.get(id);
    return new ResponseEntity<>(city, HttpStatus.OK);
  }

  @RequestMapping(value = "/city/{name}", method = RequestMethod.GET)
  public ResponseEntity<City> get(@PathVariable(value = "name") String name) {
    City city = cityService.getByName(name);
    return new ResponseEntity<>(city, HttpStatus.OK);
  }

  @RequestMapping(value = "/city/{name}", method = RequestMethod.DELETE)
  public ResponseEntity<City> delete(@PathVariable(value = "name") String name) {
    cityService.delete(name);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
