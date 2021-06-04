package com.czetsuyatech.config;

import java.util.concurrent.ConcurrentHashMap;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.OIDCHttpFacade;
import org.keycloak.representations.adapters.config.AdapterConfig;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 */
public class PathBasedConfigResolver implements KeycloakConfigResolver {

  private final ConcurrentHashMap<String, KeycloakDeployment> cache = new ConcurrentHashMap<>();

  @SuppressWarnings("unused")
  private static AdapterConfig adapterConfig;

  @Override
  public KeycloakDeployment resolve(OIDCHttpFacade.Request request) {


    /**
     * String path = request.getURI(); int multitenantIndex = path.indexOf("tenant/");
     * 
     * if (multitenantIndex == -1) { throw new IllegalStateException("Not able to resolve realm from
     * the request path!"); }
     * 
     * String realm = path.substring(path.indexOf("tenant/")).split("/")[1]; if
     * (realm.contains("?")) { realm = realm.split("\\?")[0]; }
     */


    String tenantID = request.getHeader("X-TenantID");



    if (tenantID.isBlank()) {
      throw new IllegalStateException("Not able to resolve realm from the request path!");
    }
    String realm = tenantID;



    if (!cache.containsKey(realm)) {
      /**
       * InputStream is = getClass().getResourceAsStream("/" + realm + "-keycloak.json");
       * cache.put(realm, KeycloakDeploymentBuilder.build(is));
       */

      adapterConfig = new AdapterConfig();
      adapterConfig.setRealm(realm);
      adapterConfig.setAuthServerUrl("http://localhost:8081/auth/");
      adapterConfig.setSslRequired("external");
      adapterConfig.setResource("web");
      adapterConfig.setPublicClient(true);
      adapterConfig.setConfidentialPort(0);

      // adapterConfig.setVerifyTokenAudience(true);


      cache.put(realm, KeycloakDeploymentBuilder.build(adapterConfig));


    }

    return cache.get(realm);
  }

  static void setAdapterConfig(AdapterConfig adapterConfig) {
    PathBasedConfigResolver.adapterConfig = adapterConfig;
  }

}
