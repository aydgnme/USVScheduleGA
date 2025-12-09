package com.dm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("oracle.jdbc.OracleDriver");
        ds.setUrl(System.getProperty("ORACLE_URL"));
        ds.setUsername(System.getProperty("ORACLE_USER"));
        ds.setPassword(System.getProperty("ORACLE_PASSWORD"));
        return ds;
    }
}