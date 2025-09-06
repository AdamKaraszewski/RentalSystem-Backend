package com.rental_manager.roomie.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.rental_manager.roomie.account_module.repositories",
        entityManagerFactoryRef = "accountModuleEntityManger",
        transactionManagerRef = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
@Profile({"dev", "test"})
public class AccountModuleDataSourceConfig {

    @Value("${datasource.database.url}")
    private String url;

    @Value("${datasource.account-module.username}")
    private String username;

    @Value("${datasource.account-module.password}")
    private String password;

    @Value("${datasource.account-module.show-sql}")
    private boolean showSql;

    public static final String ACCOUNT_MODULE_DS_NAME = "accountModuleDataSource";

//    @Primary
    @Bean(name = ACCOUNT_MODULE_DS_NAME)
    public DataSource accountModuleDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        return new HikariDataSource(hikariConfig);
    }

//    @Primary
    @Bean(name = "accountModuleEntityManger")
    public LocalContainerEntityManagerFactoryBean accountModuleEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier(ACCOUNT_MODULE_DS_NAME) DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = builder.dataSource(dataSource)
                .persistenceUnit("accountModulePU")
                .packages("com.rental_manager.roomie.entities")
                .build();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(showSql);
        em.setJpaVendorAdapter(vendorAdapter);
        return em;
    }

//    @Primary
    @Bean(name = TransactionManagersIds.ACCOUNT_MODULE_TX_MANAGER)
    public PlatformTransactionManager accountModulePlatformTransactionManager(
            @Qualifier("accountModuleEntityManger") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}