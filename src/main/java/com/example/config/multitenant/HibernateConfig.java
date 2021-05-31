package com.example.config.multitenant;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class HibernateConfig {
  @Autowired
  private JpaProperties jpaProperties;

  @Autowired
  private DataSource dataSource;

  @Bean
  JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
      MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
      CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl) {
    // Properties related to MultiTenancyStrategy Schema
    Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
    jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
    jpaPropertiesMap.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER,
        multiTenantConnectionProviderImpl);
    jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,
        currentTenantIdentifierResolverImpl);

    // Properties related to MultiTenancyStrategy Database
    // jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
    // jpaPropertiesMap.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER,
    // multiTenantConnectionProviderImpl);
    // jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,
    // currentTenantIdentifierResolverImpl);

    jpaPropertiesMap.put(Environment.FORMAT_SQL, true);
    jpaPropertiesMap.put(Environment.SHOW_SQL, true);

    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("com.example*");
    em.setJpaVendorAdapter(this.jpaVendorAdapter());
    em.setJpaPropertyMap(jpaPropertiesMap);
    return em;
  }


  /**
   * @Bean
   * @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode =
   *              ScopedProxyMode.TARGET_CLASS) public JdbcTemplate jdbcTemplate() { JdbcTemplate
   *              jdbcTemplate = new JdbcTemplate(dataSource); SimpleNativeJdbcExtractor
   *              simpleNativeJdbcExtractor = new SimpleNativeJdbcExtractor() {
   * @Override public Connection getNativeConnection(Connection con) throws SQLException {
   *           con.setSchema(TenantContext.getCurrentTenant()); return
   *           super.getNativeConnection(con); }
   * 
   * @Override public Connection getNativeConnectionFromStatement(Statement stmt) throws
   *           SQLException { return super.getNativeConnectionFromStatement(stmt); } };
   * 
   *           simpleNativeJdbcExtractor.setNativeConnectionNecessaryForNativeStatements(true);
   * 
   *           jdbcTemplate.setNativeJdbcExtractor(simpleNativeJdbcExtractor);
   * 
   *           return jdbcTemplate; }
   */


  @Bean
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public JdbcTemplate jdbcTemplate() throws SQLException {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate;
  }


  @Bean
  public DataSource customDataSource(DataSourceProperties properties) {

    final CustomHikariDataSource dataSource = (CustomHikariDataSource) properties
        .initializeDataSourceBuilder().type(CustomHikariDataSource.class).build();
    if (properties.getName() != null) {
      dataSource.setPoolName(properties.getName());
    }
    return dataSource;
  }



}
