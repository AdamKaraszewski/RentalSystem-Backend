package com.rental_manager.roomie.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@Profile({"dev", "test"})
public class AuthenticationModuleDataSourceConfig {

    @Value("${datasource.database.url}")
    private String url;

    @Value("${datasource.authentication-module.username}")
    private String username;

    @Value("${datasource.authentication-module.password}")
    private String password;

    @Value("${datasource.authentication-module.show-sql}")
    private boolean showSql;

    @Bean(name = "authenticationModuleDataSource")
    public DataSource authenticationModuleDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "authenticationModuleEntityManager")
    public LocalContainerEntityManagerFactoryBean authenticationModuleEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("authenticationModuleDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = builder.dataSource(dataSource)
                .persistenceUnit("authenticationModulePU")
                .packages("com.rental_manager.roomie.authentication_module")
                .build();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(showSql);
        em.setJpaVendorAdapter(vendorAdapter);
        return em;
    }

    @Bean(name = TransactionManagersIds.AUTHENTICATION_MODULE_TX_MANAGER)
    public PlatformTransactionManager authenticationModuleTransactionManager(
            @Qualifier("authenticationModuleEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
