package com.npci.integration.config;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${npci.database.driver}")
    private String databaseDriver;

    @Value("${npci.database.url}")
    private String databaseUrl;

    @Value("${npci.database.username}")
    private String databaseUsername;

    @Value("${npci.database.password}")
    private String databasePassword;

    @Value("${npci.database.schema}")
    private String databaseSchema;

    String schema = "?currentSchema=";

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(databaseDriver)
                .url(databaseUrl +
                        schema + databaseSchema)
                .username(databaseUsername)
                .password(databasePassword)
                .build();
    }
}
