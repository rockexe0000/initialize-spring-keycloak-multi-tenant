package com.czetsuyatech.config;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 */
public class MultitenantKeycloakAuthenticationEntryPoint extends KeycloakAuthenticationEntryPoint {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public MultitenantKeycloakAuthenticationEntryPoint(
      AdapterDeploymentContext adapterDeploymentContext) {
    super(adapterDeploymentContext);
  }

  public MultitenantKeycloakAuthenticationEntryPoint(
      AdapterDeploymentContext adapterDeploymentContext, RequestMatcher apiRequestMatcher) {
    super(adapterDeploymentContext, apiRequestMatcher);
  }

  @Override
  protected void commenceLoginRedirect(HttpServletRequest request, HttpServletResponse response)
      throws IOException {


    /**
     * String path = request.getRequestURI(); int multitenantIndex = path.indexOf("tenant/"); if
     * (multitenantIndex == -1) { throw new IllegalStateException("Not able to resolve realm from
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


    String contextAwareLoginUri = request.getContextPath() + "/tenant/" + realm + DEFAULT_LOGIN_URI;
    logger.debug("contextAwareLoginUri=[" + contextAwareLoginUri + "]"); // NOSONAR

    response.sendRedirect(contextAwareLoginUri);
  }
}
