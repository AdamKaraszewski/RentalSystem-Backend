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
import java.util.Properties;

@Configuration
@Profile("dev")
@EnableJpaRepositories(entityManagerFactoryRef = "initEntityMangerFactory")
public class InitDataSourceConfig {

    @Value("${datasource.database.url}")
    private String url;

    @Value("${datasource.database-init.username}")
    private String username;

    @Value("${datasource.database-init.password}")
    private String password;

    @Value("${datasource.database-init.max-pool-size}")
    private int maxPoolSize;

    @Value("${datasource.database-init.ddl-auto}")
    private String ddlAuto;

    @Value("${datasource.database-init.init-data-script}")
    private String initDataSql;

    @Primary
    @Bean(name = "initDataSource")
    public DataSource initDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(maxPoolSize);
        return new HikariDataSource(config);
    }

    @Primary
    @Bean(name = "initEntityMangerFactory")
    public LocalContainerEntityManagerFactoryBean initEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("initDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = builder.dataSource(dataSource)
                .persistenceUnit("initPU")
                .packages("com.rental_manager.roomie.entities")
                .build();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        properties.put("hibernate.hbm2ddl.import_files", initDataSql);
        em.setJpaProperties(properties);
        return em;
    }

    @Primary
    @Bean(name = "initTransactionManager")
    public PlatformTransactionManager initTransactionManager(
            @Qualifier("initEntityMangerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
