package com.inventario.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.retry.annotation.EnableRetry;

import javax.sql.DataSource;

@Configuration
//@EnableRetry
@Slf4j
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url("jdbc:mysql://mysql:3306/inventario_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC")
                .username("inventario")
                .password("inventario123")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();

        dataSource.setConnectionTimeout(60000);
        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(2);
        dataSource.setIdleTimeout(300000);
        dataSource.setMaxLifetime(1200000);
        dataSource.setInitializationFailTimeout(0);

        log.info("✅ DataSource configurado con HikariCP");
        return dataSource;
    }
}