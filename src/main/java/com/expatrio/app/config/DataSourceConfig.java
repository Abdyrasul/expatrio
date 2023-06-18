package com.expatrio.app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public HikariDataSource hikariDataSource(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/resul");
        hikariConfig.setUsername("resul");
        hikariConfig.setPassword("expatrio");

        return new HikariDataSource(hikariConfig);
    }


    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();

        // Optionally, you can customize Flyway configuration further
        // flyway.setBaselineOnMigrate(true);
        // flyway.setValidateOnMigrate(false);
        // ...

        flyway.migrate();

        return flyway;
    }

    public JdbcTemplate jdbcTemplate(HikariDataSource hikariDataSource){return new JdbcTemplate(hikariDataSource);}
}
