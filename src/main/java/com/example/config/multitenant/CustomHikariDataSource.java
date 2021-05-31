package com.example.config.multitenant;

import java.sql.Connection;
import java.sql.SQLException;
import com.example.config.TenantContext;
import com.zaxxer.hikari.HikariDataSource;

public class CustomHikariDataSource extends HikariDataSource {
  @Override
  public Connection getConnection() throws SQLException {

    Connection connection = super.getConnection();
    connection.setSchema(TenantContext.getCurrentTenant());
    return connection;
  }
}
